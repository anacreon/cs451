
public class ImageCompress {
	private Image original;
	private Image paddedimage;
	
	private double[][] y;
	private double[][] cr,cb;
	
	private double[][] subcr,subcb;
	
	private double[][] dcty;
	private double[][] dctcr, dctcb;
	
	long[][] quantizedy;
	long[][] quantizedcb, quantizedcr;
	public ImageCompress(String filename)
	{
		original = new Image(filename);

	}
	public ImageCompress(Image image)
	{
		original = image;
	}
	public void compress(int n)
	{
		displayOriginal();
		createPaddedImage();
		colorSpaceTransform();
		subsample();
		dctAll();
		quantization(n);
		compressionRatio(n);
	}
	private void compressionRatio(int n) {
		// TODO Auto-generated method stub
		int ybits = bitsForY(n);
		int cbbits = bitsForCb(n);
		int crbits = bitsForCr(n);
		
		int totalbits = ybits + cbbits + crbits;
		int originalfilesize = original.getW() * original.getH() * 24;
		
		double ratio = originalfilesize * 1.0 / totalbits;
		
		System.out.println("The compression ratio is " + ratio);
		
		
	}
	private int bitsForCr(int n) {
		// TODO Auto-generated method stub
		long[][] block = new long[8][8];
		long[] zigzag = new long[64];
		int counter = 0;
		for(int i = 0; i < quantizedcr.length; i = i+8)
		{
			for(int j = 0; j < quantizedcr[0].length; j = j+8)
			{
				
				for(int k = i; k < (i+8); k++)
				{
					for(int l = j; l <(j+8);l++)
					{
						block[k%8][l%8] = quantizedcr[k][l];
					}
				}
				toOneDArray(block,zigzag);
				counter += countbits(zigzag, 9-n);		
			}
		}
		
		return counter;
	}
	private int bitsForCb(int n) {
		// TODO Auto-generated method stub
		long[][] block = new long[8][8];
		long[] zigzag = new long[64];
		int counter = 0;
		for(int i = 0; i < quantizedcb.length; i = i+8)
		{
			for(int j = 0; j < quantizedcb[0].length; j = j+8)
			{
				
				for(int k = i; k < (i+8); k++)
				{
					for(int l = j; l <(j+8);l++)
					{
						block[k%8][l%8] = quantizedcb[k][l];
					}
				}
				toOneDArray(block,zigzag);
				counter += countbits(zigzag, 9-n);		
			}
		}
		
		return counter;
	}
	private int bitsForY(int n) {
		// TODO Auto-generated method stub
		long[][] block = new long[8][8];
		long[] zigzag = new long[64];
		int counter = 0;
		for(int i = 0; i < quantizedy.length; i = i+8)
		{
			for(int j = 0; j < quantizedy[0].length; j = j+8)
			{
				
				for(int k = i; k < (i+8); k++)
				{
					for(int l = j; l <(j+8);l++)
					{
						block[k%8][l%8] = quantizedy[k][l];
					}
				}
				toOneDArray(block,zigzag);
				counter += countbits(zigzag, 10-n);		
			}
		}
		
		return counter;
	}
	private int countbits(long[] zigzag, int n) {
		// TODO Auto-generated method stub
		//DC value
		int dc = n;
		int bits = 0;
		long[] touple = new long[2];
		for(int i = 1; i < zigzag.length; i++)
		{
			touple[0] = zigzag[i];
			int j = i;
			while((j < zigzag.length) && (touple[0] == zigzag[j]))
			{
				touple[1]++;
				j++;
			}
			i = j;
			bits+= n + 6;
		}
		bits = bits+dc;
		
		
		
		return bits;
	}
	private void toOneDArray(long[][] block, long[] zigzag) {
		// TODO Auto-generated method stub
		int[] indexes = {
				  0,  1,  8, 16,  9,  2,  3, 10, 
				 17, 24, 32, 25, 18, 11,  4,  5,
				 12, 19, 26, 33, 40, 48, 41, 34,
				 27, 20, 13,  6, 7,  14, 21, 28,
				 35, 42, 49, 56, 57, 50, 43, 36, 
				 29, 22, 15, 23, 30, 37, 44, 51, 
				 58, 59, 52, 45, 38, 31, 39, 46, 
				 53, 60, 61, 54, 47, 55, 62, 63
				
		};
		/*  0, 1,  2,  3,  4,  5,  6,  7,
		 *  8,  9, 10, 11, 12, 13, 14, 15,
		 * 16, 17, 18, 19, 20, 21, 22, 23,
		 * 24, 25, 26, 27, 28, 29, 30, 31,
		 * 32, 33, 34, 35, 36, 37, 38, 39,
		 * 40, 41, 42, 43, 44, 45, 46, 47,
		 * 48, 49, 50, 51, 52, 53, 54, 55,
		 * 56, 57, 58, 59, 60, 61, 62, 63
		 */
		long[] order = new long[zigzag.length];
		for(int i = 0; i < order.length; i++)
		{
			order[i] = block[i%8][i/8];
		}
		
		for(int i =0; i < indexes.length; i++)
		{
			zigzag[i] = order[indexes[i]];
		}
		
		
	}
	public void createPaddedImage() {
		// TODO Auto-generated method stub
		int w;
		int h;
		
		w = (8 - original.getW() % 8) + original.getW();
		h = (8 - original.getH() % 8) + original.getH();
		paddedimage = new Image(w,h);
		int[] rgb = new int[3];
		
		for(int y = 0; y < original.getH(); y++)
		{
			for(int x = 0; x < original.getW(); x++)
			{
				original.getPixel(x, y, rgb);
				paddedimage.setPixel(x, y, rgb);
			}
		}
		
	}
	public void colorSpaceTransform()
	{
		int[] rgb = new int[3];
		y = new double[paddedimage.getW()][paddedimage.getH()];
		cr = new double[paddedimage.getW()][paddedimage.getH()];
		cb = new double[paddedimage.getW()][paddedimage.getH()];
		for(int i = 0; i < paddedimage.getH(); i++)
		{
			for(int j = 0; j < paddedimage.getW(); j++)
			{
				paddedimage.getPixel(j, i, rgb);
				y[j][i] = rgb[0] * 0.2990 + rgb[1] *0.5870 + rgb[2] *0.1140;
				if(y[j][i] > 255)
				{
					y[j][i] = 255;
				}
				if(y[j][i] < 0)
				{
					y[j][i] = 0;
				}
				y[j][i] = y[j][i] - 128;
			}
		}
		
		for(int i = 0; i < paddedimage.getH(); i++)
		{
			for(int j = 0; j < paddedimage.getW(); j++)
			{
				paddedimage.getPixel(j, i, rgb);
				cb[j][i] = rgb[0] * -0.1687 + rgb[1] * -0.3313 + rgb[2] * 0.5000;
				if(cb[j][i] > 127.5)
				{
					cb[j][i] = 127.5;
				}
				if(cb[j][i] < -127.5)
				{
					cb[j][i] = -127.5;
				}
				cb[j][i] = cb[j][i] - 0.5;
			}
		}
		
		for(int i = 0; i < paddedimage.getH(); i++)
		{
			for(int j = 0; j < paddedimage.getW(); j++)
			{
				paddedimage.getPixel(j, i, rgb);
				cr[j][i] = rgb[0] * 0.5000 + rgb[1] * -0.4187 + rgb[2] * -0.0813;
				if(cr[j][i] > 127.5)
				{
					cr[j][i] = 127.5;
				}
				if(cr[j][i] < -127.5)
				{
					cr[j][i] = -127.5;
				}
				cr[j][i] = cr[j][i] - 0.5;
			}
		}
		
	}
	
	public void subsample()
	{
		int subw = (8 - (cr.length / 2) % 8) + cr.length/2; 
		int subh = (8 - (cr[0].length / 2) % 8) + cr[0].length/2;
		this.subcr = new double[subw][subh];
		this.subcb = new double[subw][subh];
		
		for(int i = 0; i < subcr.length; i++)
		{
			for(int j = 0; j < subcr[0].length; j++)
			{
				this.subcr[i][j] = 0;
				this.subcb[i][j] = 0;
			}
		}
		
		//cr and cb have the exact same dimensions
		//so sub sampled cr and sub sampled cb should have the exact same dimensions as well.
		for(int i = 0; i < cr.length; i = i+2)
		{
			for(int j = 0; j < cr[0].length; j = j+2)
			{
				this.subcr[i/2][j/2] = (cr[i][j] + cr[i+1][j] + cr[i][j+1] + cr[i+1][j+1]) / 4;
				this.subcb[i/2][j/2] = (cb[i][j] + cb[i+1][j] + cb[i][j+1] + cb[i+1][j+1]) / 4;
			}
		}
	}
	public void dctAll()
	{
		getDCTy();
		getDCTcb();
		getDCTcr();
	}
		
	public void getDCTy()
	{
		double[][] dctin = new double[8][8];
		double[][] dctout = new double[8][8];
		dcty = new double[y.length][y[0].length];
		int[] dimensions = {8,8};
		
		for(int i = 0; i < dctin.length; i++)
		{
			for(int j = 0; j < dctin[0].length; j++)
			{
				dctin[i][j] = 0;
				dctout[i][j] = 0;
			}
		}
		
		
		for(int i = 0; i < y.length; i = i+8)
		{
			for(int j = 0; j < y[0].length; j = j+8)
			{
				
				for(int k = i; k < (i+8); k++)
				{
					for(int l = j; l <(j+8);l++)
					{
						dctin[k%8][l%8] = y[k][l];
					}
				}
				dct(dctin,dimensions,dctout);
				
				for(int k = i; k < (i+8); k++)
				{
					for(int l = j; l <(j+8);l++)
					{
						dcty[k][l] = dctout[k%8][l%8];
					}
				}
				
				//initialize dctout
				for(int o = 0; o < dctout.length; o++)
				{
					for(int p = 0; p < dctout[0].length; p++)
					{
						dctout[o][p] = 0;
					}
				}
				
				
				
			}
		}
	}
	public void getDCTcb()
	{
		double[][] dctin = new double[8][8];
		double[][] dctout = new double[8][8];
		dctcb = new double[subcb.length][subcb[0].length];
		int[] dimensions = {8,8};
		for(int i = 0; i < dctin.length; i++)
		{
			for(int j = 0; j < dctin[0].length; j++)
			{
				dctin[i][j] = 0;
				dctout[i][j] = 0;
			}
		}
		
		for(int i = 0; i < subcb.length; i = i+8)
		{
			for(int j = 0; j < subcb[0].length; j = j+8)
			{
				
				for(int k = i; k < (i+8); k++)
				{
					for(int l = j; l <(j+8);l++)
					{
						dctin[k%8][l%8] = subcb[k][l];
					}
				}
				dct(dctin,dimensions,dctout);
				
				for(int k = i; k < (i+8); k++)
				{
					for(int l = j; l <(j+8);l++)
					{
						dctcb[k][l] = dctout[k%8][l%8];
					}
				}
				
				for(int o = 0; o < dctout.length; o++)
				{
					for(int p = 0; p < dctout[0].length; p++)
					{
						dctout[o][p] = 0;
					}
				}
				
				
			}
		}
	}
	public void getDCTcr()
	{
		double[][] dctin = new double[8][8];
		double[][] dctout = new double[8][8];
		
		for(int i = 0; i < dctin.length; i++)
		{
			for(int j = 0; j < dctin[0].length; j++)
			{
				dctin[i][j] = 0;
				dctout[i][j] = 0;
			}
		}
		
		
		
		dctcr = new double[subcr.length][subcr[0].length];
		int[] dimensions = {8,8};
		for(int i = 0; i < subcr.length; i = i+8)
		{
			for(int j = 0; j < subcr[0].length; j = j+8)
			{
				
				for(int k = i; k < (i+8); k++)
				{
					for(int l = j; l <(j+8);l++)
					{
						dctin[k%8][l%8] = subcr[k][l];
					}
				}
				dct(dctin,dimensions,dctout);
				
				for(int k = i; k < (i+8); k++)
				{
					for(int l = j; l <(j+8);l++)
					{
						dctcr[k][l] = dctout[k%8][l%8];
					}
				}
				
				for(int o = 0; o < dctout.length; o++)
				{
					for(int p = 0; p < dctout[0].length; p++)
					{
						dctout[o][p] = 0;
					}
				}
				
			}
		}
	}
	public void dct(double[][] input, int[] dimensions, double[][] output)
	{
		  
		  for(int u = 0; u < dimensions[0]; u++)
		  {
			  for(int v = 0; v < dimensions[1]; v++)
			  {
				  for(int i = 0; i < dimensions[0]; i++)
				  {
					  for(int j = 0; j < dimensions[1]; j++)
					  {
						  output[u][v] += Math.cos(((2*i + 1) * u * Math.PI)/ 16) * Math.cos(((2*j +1)* v * Math.PI)/16)*input[i][j];
					  }
				  }
			  }
		  }
		  for(int u = 0; u < dimensions[0]; u++)
		  {
			  for(int v = 0; v < dimensions[1]; v++)
			  {
				  output[u][v] = output[u][v] * c(u) *c(v) / 4.0;
				  double max = Math.pow(2, 10);
				  double min = -max;
				  if(output[u][v] < min)
				  {
					  output[u][v] = min;
				  }
				  if(output[u][v] > max)
				  {
					  output[u][v] = max;
				  }
			  }		 
		  }
		  
	}
	public void quantization(int n)
	{
		quantizeY(n);
		quantizeCb(n);
		quantizeCr(n);
	}
	private void quantizeCr(int n) {
		// TODO Auto-generated method stub
		int[][] quant = {
				{ 8,  8,  8, 16, 32, 32, 32, 32},
				{ 8,  8,  8, 16, 32, 32, 32, 32},
				{ 8,  8, 16, 32, 32, 32, 32, 32},
				{16, 16, 32, 32, 32, 32, 32, 32},
				{32, 32, 32, 32, 32, 32, 32, 32},
				{32, 32, 32, 32, 32, 32, 32, 32},
				{32, 32, 32, 32, 32, 32, 32, 32},
				{32, 32, 32, 32, 32, 32, 32, 32}
		
		};
		quantizedcr = new long[dctcr.length][dctcr[0].length];
		for(int i = 0; i < quant.length; i++)
		{
			for(int j = 0; j < quant[0].length; j++)
			{
				quant[i][j] = quant[i][j] * (int)Math.pow(2, n);
			}
		}
		
		
		for(int i = 0; i < dctcr.length; i = i+8)
		{
			for(int j = 0; j < dctcr[0].length; j = j+8)
			{
				
				for(int k = i; k < (i+8); k++)
				{
					for(int l = j; l <(j+8);l++)
					{
						quantizedcr[k][l] = Math.round(dctcr[k][l] / quant[k%8][l%8]);
					}
				}
			}
		}	

		
	}
	private void quantizeCb(int n) {
		// TODO Auto-generated method stub
		int[][] quant = {
				{ 8,  8,  8, 16, 32, 32, 32, 32},
				{ 8,  8,  8, 16, 32, 32, 32, 32},
				{ 8,  8, 16, 32, 32, 32, 32, 32},
				{16, 16, 32, 32, 32, 32, 32, 32},
				{32, 32, 32, 32, 32, 32, 32, 32},
				{32, 32, 32, 32, 32, 32, 32, 32},
				{32, 32, 32, 32, 32, 32, 32, 32},
				{32, 32, 32, 32, 32, 32, 32, 32}
		
		};
		quantizedcb = new long[dctcb.length][dctcb[0].length];
		for(int i = 0; i < quant.length; i++)
		{
			for(int j = 0; j < quant[0].length; j++)
			{
				quant[i][j] = quant[i][j] * (int)Math.pow(2, n);
			}
		}
		
		
		for(int i = 0; i < dctcb.length; i = i+8)
		{
			for(int j = 0; j < dctcb[0].length; j = j+8)
			{
				
				for(int k = i; k < (i+8); k++)
				{
					for(int l = j; l <(j+8);l++)
					{
						quantizedcb[k][l] = Math.round(dctcb[k][l] / quant[k%8][l%8]);
					}
				}
			}
		}
		
	}
	private void quantizeY(int n) {
		// TODO Auto-generated method stub
		int[][] quant = {
				{ 4,  4,  4,  8,  8, 16, 16, 32},
				{ 4,  4,  4,  8,  8, 16, 16, 32},
				{ 4,  4,  8,  8, 16, 16, 32, 32},
				{ 8,  8,  8, 16, 16, 32, 32, 32},
				{ 8,  8, 16, 16, 32, 32, 32, 32},
				{16, 16, 16, 32, 32, 32, 32, 32},
				{16, 16, 32, 32, 32, 32, 32, 32},
				{32, 32, 32, 32, 32, 32, 32, 32}
		
		};
		quantizedy = new long[dcty.length][dcty[0].length];
		for(int i = 0; i < quant.length; i++)
		{
			for(int j = 0; j < quant[0].length; j++)
			{
				quant[i][j] = quant[i][j] * (int)Math.pow(2, n);
			}
		}
		
		
		for(int i = 0; i < dcty.length; i = i+8)
		{
			for(int j = 0; j < dcty[0].length; j = j+8)
			{
				
				for(int k = i; k < (i+8); k++)
				{
					for(int l = j; l <(j+8);l++)
					{
						quantizedy[k][l] = Math.round(dcty[k][l] / quant[k%8][l%8]);
					}
				}
			}
		}	
	}
	private double c(int x) {
		// TODO Auto-generated method stub
		if(x == 0)
		{
			return 1 / Math.sqrt(2);
		}
		else
			return 1;
	}
	public void displayPadded()
	{
		paddedimage.display("Padded");
	}
	public void displayOriginal()
	{
		original.display("Original");
	}
	public Image getPaddedImage() {
		// TODO Auto-generated method stub
		return paddedimage;
	}
}
