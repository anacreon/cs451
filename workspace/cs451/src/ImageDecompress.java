
public class ImageDecompress {
	private Image paddedimage;
	private Image original;
	private double[][] y;
	private double[][] cr,cb;
	private double[][] subcr,subcb;
	double[][] dcty;
	double[][] dctcr, dctcb;
	
	long[][] quantizedy;
	long[][] quantizedcb, quantizedcr;
	
	public ImageDecompress(Image image)
	{
		paddedimage = image;
	}
	public ImageDecompress(double[][] dcty, double[][] dctcr, double[][] dctcb)
	{
		this.dcty = dcty;
		this.dctcr = dctcr;
		this.dctcb = dctcb;
	}
	public ImageDecompress(long[][] quantizedy, long[][] quantizedcr, long[][] quantizedcb)
	{
		this.quantizedy = quantizedy;
		this.quantizedcr = quantizedcr;
		this.quantizedcb = quantizedcb;
	}
	public void decompress(int n)
	{
		dequantize(n);
		idctALL();
		superSample();
		offset();
		colorSpaceTransform();
		removeImagePadding();
		displayOriginal();
	}
	public void removeImagePadding()
	{
		int wcounter = 0;
		int hcounter = 0;
		int[] rgb = {0,0,0};
		int x = this.paddedimage.getW()-1;
		int y = this.paddedimage.getH()-1;
		while((rgb[0] <= 75) && (rgb[1] <= 75) && (rgb[2] <= 75))
		{
			this.paddedimage.getPixel(x, 0, rgb);
			if((rgb[0] <= 75) && (rgb[1] <= 75) && (rgb[2] <= 75))
			{
				x--;
			}
		}
		rgb[0] = 0;
		rgb[1] = 0;
		rgb[2] = 0;
		while((rgb[0] <= 75) && (rgb[1] <= 75) && (rgb[2] <= 75))
		{
			this.paddedimage.getPixel(0, y, rgb);
			if((rgb[0] <= 75) && (rgb[1] <= 75) && (rgb[2] <= 75))
			{
				y--;
			}
		}
		this.original = new Image(x+1, y+1);
		for(int i = 0; i < this.original.getH(); i++)
		{
			for(int j = 0; j < this.original.getW(); j++)
			{
				this.paddedimage.getPixel(j, i, rgb);
				
				this.original.setPixel(j, i, rgb);
			}
		}

	}
	public void superSample()
	{

		
		cb = new double[y.length][y[0].length];
		cr = new double[y.length][y[0].length];
		
		for(int i = 0; i < cr.length; i = i+2)
		{
			for(int j = 0; j < cr[0].length; j = j+2)
			{
				this.cr[i][j] = this.subcr[i/2][j/2];
				this.cr[i+1][j] = this.subcr[i/2][j/2];
				this.cr[i][j+1] = this.subcr[i/2][j/2];
				this.cr[i+1][j+1] = this.subcr[i/2][j/2];
				
				this.cb[i][j] = this.subcb[i/2][j/2];
				this.cb[i+1][j] = this.subcb[i/2][j/2];
				this.cb[i][j+1] = this.subcb[i/2][j/2];
				this.cb[i+1][j+1] = this.subcb[i/2][j/2];

			}
		}

		
	}
	public void offset()
	{
		for(int i = 0; i < y.length; i++)
		{
			for(int j = 0; j < y[0].length; j++)
			{
				y[i][j] = y[i][j] + 128;
				cr[i][j] = cr[i][j] + 0.5;
				cb[i][j] = cb[i][j] + 0.5;

			}
		}
	}
	public void colorSpaceTransform()
	{
		paddedimage = new Image(y.length,y[0].length);
		int[] rgb = new int[3];
		for(int i = 0; i < paddedimage.getH(); i++)
		{
			for(int j = 0; j <paddedimage.getW(); j++)
			{
				rgb[0] = (int)(1.0000*y[j][i] + 0*cb[j][i] + 1.4020*cr[j][i]);
				rgb[1] = (int)(1.0000*y[j][i] + -0.344136*cb[j][i] + -0.714136*cr[j][i]);
				rgb[2] = (int)(1.0000*y[j][i] + 1.7720*cb[j][i] + 0*cr[j][i]);
				
				for(int k =0; k < rgb.length; k++)
				{
					if(rgb[k] < 0)
					{
						rgb[k] = 0;
					}
					if(rgb[k] > 255)
					{
						rgb[k] = 255;
					}
				}
				
				paddedimage.setPixel(j, i, rgb);
				
			}
		}
	}
	public void idctALL()
	{
		subcb = new double[dctcb.length][dctcb[0].length];
		subcr = new double[dctcr.length][dctcr[0].length];
		getIDCTy();
		getIDCTcb();
		getIDCTcr();
	}
	private void getIDCTcr() {
		// TODO Auto-generated method stub
		double[][] idctin = new double[8][8];
		double[][] idctout = new double[8][8];
		subcr = new double[dctcr.length][dctcr[0].length];
		int[] dimensions = {8,8};
		for(int i = 0; i < dctcr.length; i = i+8)
		{
			for(int j = 0; j < dctcr[0].length; j = j+8)
			{
				
				for(int k = i; k < (i+8); k++)
				{
					for(int l = j; l <(j+8);l++)
					{
						idctin[k%8][l%8] = dctcr[k][l];
					}
				}
				idct(idctin,dimensions,idctout);
				
				for(int k = i; k < (i+8); k++)
				{
					for(int l = j; l <(j+8);l++)
					{
						subcr[k][l] = idctout[k%8][l%8];
					}
				}
				
				for(int o = 0; o < idctout.length; o++)
				{
					for(int p = 0; p < idctout[0].length; p++)
					{
						idctout[o][p] = 0;
					}
				}
				
			}
		}
	}
	private void getIDCTcb() {
		// TODO Auto-generated method stub
		double[][] idctin = new double[8][8];
		double[][] idctout = new double[8][8];
		subcb = new double[dctcb.length][dctcb[0].length];
		int[] dimensions = {8,8};
		for(int i = 0; i < dctcb.length; i = i+8)
		{
			for(int j = 0; j < dctcb[0].length; j = j+8)
			{
				
				for(int k = i; k < (i+8); k++)
				{
					for(int l = j; l <(j+8);l++)
					{
						idctin[k%8][l%8] = dctcb[k][l];
					}
				}
				idct(idctin,dimensions,idctout);
				
				for(int k = i; k < (i+8); k++)
				{
					for(int l = j; l <(j+8);l++)
					{
						subcb[k][l] = idctout[k%8][l%8];
					}
				}
				for(int o = 0; o < idctout.length; o++)
				{
					for(int p = 0; p < idctout[0].length; p++)
					{
						idctout[o][p] = 0;
					}
				}
				
			}
		}
		
	}
	public void getIDCTy()
	{
		double[][] idctin = new double[8][8];
		double[][] idctout = new double[8][8];
		y = new double[dcty.length][dcty[0].length];
		int[] dimensions = {8,8};
		
		for(int i = 0; i < idctin.length; i++)
		{
			for(int j = 0; j < idctin[0].length; j++)
			{
				idctin[i][j] = 0;
				idctout[i][j] = 0;
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
						idctin[k%8][l%8] = dcty[k][l];
					}
				}
				idct(idctin,dimensions,idctout);
				
				for(int k = i; k < (i+8); k++)
				{
					for(int l = j; l <(j+8);l++)
					{
						y[k][l] = idctout[k%8][l%8];
					}
				}
				
				for(int o = 0; o < idctout.length; o++)
				{
					for(int p = 0; p < idctout[0].length; p++)
					{
						idctout[o][p] = 0;
					}
				}
				
			}
		}
	}
	public void idct(double[][] input, int[] dimensions, double[][] output)
	{
		  
		for(int i = 0; i < dimensions[0]; i++)
		{
			for(int j = 0; j < dimensions[1]; j++)
			{
				for(int u = 0; u < dimensions[0]; u++)
				{
					for(int v = 0; v < dimensions[1]; v++)
					{
						output[i][j] += c(u) * c(v) * input[u][v] * Math.cos(((2*i+1)*u*Math.PI)/16)*Math.cos(((2*j+1)*v*Math.PI)/16);
					}
				}
			  }
		}
		
		for(int i = 0; i < dimensions[0]; i++)
		{
			for(int j = 0; j < dimensions[1]; j++)
			{
				output[i][j] = output[i][j] / 4.0;
				if(output[i][j] < -128)
				{
					output[i][j] = -128;
				}
				if(output[i][j] > 127)
				{
					output[i][j] = 127;
				}
			}
		}
		
	}
	public void dequantize(int n){
		dctcr = new double[quantizedcr.length][quantizedcr[0].length];
		dctcb = new double[quantizedcb.length][quantizedcb[0].length];
		dcty = new double[quantizedy.length][quantizedy[0].length];
		dequantizeY(n);
		dequantizeCb(n);
		dequantizeCr(n);
	}
	private void dequantizeCr(int n) {
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
						dctcr[k][l] = quantizedcr[k][l] * quant[k%8][l%8];
					}
				}
			}
		}
	}
	private void dequantizeCb(int n) {
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
						dctcb[k][l] = quantizedcb[k][l] * quant[k%8][l%8];
					}
				}
			}
		}
	}
	private void dequantizeY(int n) {
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
						dcty[k][l] = quantizedy[k][l] * quant[k%8][l%8];
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
		paddedimage.display("Compressed");
	}
	public void displayOriginal()
	{
		original.display("Decompressed");
	}
}
