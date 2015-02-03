public class ImageManipulation {
	
	
	private Image input;
	private Image output;
	
	
	ImageManipulation(String filepath)
	{
		this.input = new Image(filepath);
		this.output = new Image(input.getW(), input.getH());
	}
	public ImageManipulation(Image image) {
		// TODO Auto-generated constructor stub
		this.input = image;
	}
	public void displayOut(String title)
	{
		output.display(title);
	}
	public void displayIn(String title)
	{
		input.display(title);
	}
	public void greyScale() {
		int[] rgb = new int[3];
		double gr;
		for(int i = 0; i < output.getH(); i ++)
		{
			for(int j = 0; j < output.getW(); j++)
			{
				input.getPixel(j, i, rgb);
				gr = 0.299*rgb[0] + 0.587*rgb[1] + 0.114*rgb[2];
				int grey = (int)Math.round(gr);
				if(grey < 0)
					grey = 0;
				if(grey > 255)
					grey = 255;
				
				//set all rgb values to the grey scale value
				rgb[0] = grey;
				rgb[1] = grey;
				rgb[2] = grey;
				output.setPixel(j, i, rgb);
			}
		}		
		
	}
	public void biLevelavg() {
		this.greyScale();
		double avg = greyAverage();
		
		int[] rgb = new int[3];
		for(int i = 0; i < output.getH(); i ++)
		{
			for(int j = 0; j < output.getW(); j++)
			{
				output.getPixel(j, i, rgb);
				if(rgb[0] >= avg)
				{
					rgb[0] = 255;
					rgb[1] = 255;
					rgb[2] = 255;
				}
				else
				{
					rgb[0] = 0;
					rgb[1] = 0;
					rgb[2] = 0;
				}
				output.setPixel(j, i, rgb);
			}
		}


		
	}

	private double greyAverage() {
		double avg =0;
		int[] rgb = new int[3];
		for(int i = 0; i < output.getH(); i ++)
		{
			for(int j = 0; j < output.getW(); j++)
			{
				output.getPixel(j, i, rgb);
				avg = avg + rgb[0];
			}
		}
		avg = avg / (output.getH() * output.getW());
		return avg;
		
	}

	public void biLevelErrDif() {
		this.greyScale();
		int rgb[] = new int[3];
		int rgbad[] = new int[3];
		int error;
		int q;
		for(int i = 0; i < output.getH(); i ++)
		{
			for(int j = 0; j < output.getW(); j++)
			{
				output.getPixel(j, i, rgb);
				int upper;
				upper = 255 - rgb[0];
				if(upper < rgb[0])
				{
					q = 255;
				}
				else
				{
					q = 0;
				}
				error = rgb[0]-q;
				rgb[0] = q;
				rgb[1] = q;
				rgb[2] = q;
				
				output.setPixel(j, i, rgb);
				if(j+1 < output.getW())
				{
					output.getPixel(j+1, i, rgbad);
					int errdiff = rgbad[0] + (int)(error * (7/16.0));
					if(errdiff > 255)
					{
						errdiff = 255;
					}
					if(errdiff < 0)
					{
						errdiff = 0;
					}
					rgbad[0] = errdiff;
					rgbad[1] = errdiff;
					rgbad[2] = errdiff;
					output.setPixel(j+1, i, rgbad);
					
				}
				
				
				if((j-1 >= 0) && (i+1 < output.getH()))
				{
					output.getPixel(j-1, i+1, rgbad);
					int errdiff = rgbad[0] + (int)(error * (3/16.0));
					if(errdiff > 255)
					{
						errdiff = 255;
					}
					if(errdiff < 0)
					{
						errdiff = 0;
					}
					rgbad[0] = errdiff;
					rgbad[1] = errdiff;
					rgbad[2] = errdiff;
					
					output.setPixel(j-1, i+1, rgbad);
					
				}
				
				if(i+1 < output.getH())
				{
					output.getPixel(j, i+1, rgbad);
					int errdiff = rgbad[0] + (int)(error * (5/16.0));
					if(errdiff > 255)
					{
						errdiff = 255;
					}
					if(errdiff < 0)
					{
						errdiff = 0;
					}
					rgbad[0] = errdiff;
					rgbad[1] = errdiff;
					rgbad[2] = errdiff;
					
					output.setPixel(j, i+1, rgbad);
					
				}
				
				if((j+1 < output.getW()) && (i+1 < output.getH()))
				{
					output.getPixel(j+1, i+1, rgbad);
					int errdiff = rgbad[0] + (int)(error * (1/16.0));
					if(errdiff > 255)
					{
						errdiff = 255;
					}
					if(errdiff < 0)
					{
						errdiff = 0;
					}
					rgbad[0] = errdiff;
					rgbad[1] = errdiff;
					rgbad[2] = errdiff;
					
					output.setPixel(j+1, i+1, rgbad);
					
				}
				
				
			}
		}
		
	}

	public void nLevel(int n) {
		this.greyScale();
		int step = 255 /(n-1);
		int level[] = new int[n];
		for(int i = 0; i < level.length; i++)
		{
			level[i] = i*step;
		}
		level[n-1] = 255;
		int error;
		int q;
		int rgb[] = new int[3];
		int rgbad[] = new int[3];
		for(int i = 0; i < output.getH(); i ++)
		{
			for(int j = 0; j < output.getW(); j++)
			{
				output.getPixel(j, i, rgb);
				q = closest(rgb, level);
				error = rgb[0]-q;
				rgb[0] = q;
				rgb[1] = q;
				rgb[2] = q;
				
				output.setPixel(j, i, rgb);
				if(j+1 < output.getW())
				{
					output.getPixel(j+1, i, rgbad);
					int errdiff = rgbad[0] + (int)(error * (7/16.0));
					if(errdiff > 255)
					{
						errdiff = 255;
					}
					if(errdiff < 0)
					{
						errdiff = 0;
					}
					rgbad[0] = errdiff;
					rgbad[1] = errdiff;
					rgbad[2] = errdiff;
					output.setPixel(j+1, i, rgbad);
					
				}
				
				
				if((j-1 >= 0) && (i+1 < output.getH()))
				{
					output.getPixel(j-1, i+1, rgbad);
					int errdiff = rgbad[0] + (int)(error * (3/16.0));
					if(errdiff > 255)
					{
						errdiff = 255;
					}
					if(errdiff < 0)
					{
						errdiff = 0;
					}
					rgbad[0] = errdiff;
					rgbad[1] = errdiff;
					rgbad[2] = errdiff;
					
					output.setPixel(j-1, i+1, rgbad);
					
				}
				
				if(i+1 < output.getH())
				{
					output.getPixel(j, i+1, rgbad);
					int errdiff = rgbad[0] + (int)(error * (5/16.0));
					if(errdiff > 255)
					{
						errdiff = 255;
					}
					if(errdiff < 0)
					{
						errdiff = 0;
					}
					rgbad[0] = errdiff;
					rgbad[1] = errdiff;
					rgbad[2] = errdiff;
					
					output.setPixel(j, i+1, rgbad);
					
				}
				
				if((j+1 < output.getW()) && (i+1 < output.getH()))
				{
					output.getPixel(j+1, i+1, rgbad);
					int errdiff = rgbad[0] + (int)(error * (1/16.0));
					if(errdiff > 255)
					{
						errdiff = 255;
					}
					if(errdiff < 0)
					{
						errdiff = 0;
					}
					rgbad[0] = errdiff;
					rgbad[1] = errdiff;
					rgbad[2] = errdiff;

					
					output.setPixel(j+1, i+1, rgbad);
					
				}
				
				
			}
		}
	}

	private int closest(int[] rgb, int[] level) {
		int closest = 500 ;
		int index = level.length-1;
		for(int i = 0; i < level.length; i++)
		{
			int temp = Math.abs(rgb[0] - level[i]);
			if(temp <  closest)
			{
				closest = temp;
				index = i;
			}
		}
		return level[index];
	}

	public void eightBitColor(String args) {
		lutGeneration(args+"-index.ppm");
		Image bit = new Image(args+"-index.ppm");
		EightBitImage img = new EightBitImage(input, bit);
		this.output = img;
	}
	public void lutGeneration(String filename) {
		Image img = new Image(256, 1);
		int[][] index = new int[256][3];
		int x = 0;
		System.out.println("Index  |  R  |  G  |  B");
		System.out.println("_____________________________");
		for(int r = 0; r < 8; r++)
		{
			for(int g = 0; g < 8; g++)
			{
				for(int b = 0; b < 4; b++)
				{
					index[x][0] = r*32 + 16;
					index[x][1] = g*32 + 16;
					index[x][2] = b*64 + 32;
					img.setPixel(x, 0, index[x]);
					System.out.println(x + "  |  "+ index[x][0] + " | " + index[x][1] + " | "+ index[x][2]);
					x++;
				}
			}
		}
		img.write2PPM(filename);
	}
	public void drawCircles(int n, int m) {
		// TODO Auto-generated method stub
		whiteImage(input);
		int[] black = {0,0,0};
		int cenx = input.getW() / 2;
		int ceny = input.getH() / 2;
		int radius = n-1;
		while((radius + m) < cenx)
		{
			for(int r = 0; r <= m; r++)
			{
				radius++;
				double increment = Math.PI*2 / (50* radius);
				for(double theta = 0; theta <= (2*Math.PI); theta += increment)
				{
					int x = (int)(cenx + radius * Math.cos(theta));
					int y = (int)(ceny + radius * Math.sin(theta));
					input.setPixel(x, y, black);
				}
			}
			radius = radius+n;
		}
	}
	private void whiteImage(Image img) {
		// TODO Auto-generated method stub
		int[] white = {255,255,255};
		for(int y = 0; y < img.getH(); y++)
		{
			for(int x = 0; x < img.getW(); x++)
			{
				img.setPixel(x, y, white);
			}
		}
		
	}
	public void resize(int k) {
		// TODO Auto-generated method stub
		noFilter(k);
		averageFilter(k);
		ninthFilter(k);
		gaussianFilter(k);
		
	}
	private void gaussianFilter(int k) {
		// TODO Auto-generated method stub
		Image gaussianfilter = new Image(input.getW()/ k, input.getH() / k);
		whiteImage(gaussianfilter);
		int[] rgb = new int[3];
		for(int y = 0; y < gaussianfilter.getH(); y++)
		{
			for(int x = 0; x < gaussianfilter.getW(); x++)
			{
				//we are ignoring the edge pixels
				int edgex = 0;
				int edgey = 0;
				if(x*k == 0)
				{
					edgex = 1;
				}
				if(y*k == 0)
				{
					edgey = 1;
				}
				int i = x*k + edgex;
				int j = y*k + edgey;
				double filter = 0;
				
				//apply the filter
				input.getPixel(i-1, j-1, rgb);
				filter += rgb[0] * 1.0 / 16.0;
				
				input.getPixel(i, j-1, rgb);
				filter += rgb[0] * 2.0 / 16.0;
				
				input.getPixel(i+1, j-1, rgb);
				filter += rgb[0] * 1.0 / 16.0;
				
				input.getPixel(i-1, j, rgb);
				filter += rgb[0] * 2.0 / 16.0;
				
				input.getPixel(i, j, rgb);
				filter += rgb[0] * 4.0 / 16.0;
				
				input.getPixel(i+1, j, rgb);
				filter += rgb[0] * 2.0 / 16.0;
				
				input.getPixel(i-1, j+1, rgb);
				filter += rgb[0] * 1.0 / 16.0;
				
				input.getPixel(i, j+1, rgb);
				filter += rgb[0] * 2.0 / 16.0;
				
				input.getPixel(i+1, j+1, rgb);
				filter += rgb[0] * 1.0 / 16.0;
				
				
				
				
				

				if(filter > 255)
				{
					filter = 255;
				}
				if(filter < 0)
				{
					filter = 0;
				}
				rgb[0] = (int)filter;
				rgb[1] = (int)filter;
				rgb[2] = (int)filter;
						
				
				
				
				
				
				gaussianfilter.setPixel(x, y, rgb);
			}
		}
		gaussianfilter.display("Gaussian Filter");
		
	}
	private void ninthFilter(int k) {
		// TODO Auto-generated method stub
		Image ninthfilter = new Image(input.getW()/ k, input.getH() / k);
		whiteImage(ninthfilter);
		int[] rgb = new int[3];
		for(int y = 0; y < ninthfilter.getH(); y++)
		{
			for(int x = 0; x < ninthfilter.getW(); x++)
			{
				//we are ignoring the edge pixels
				int edgex = 0;
				int edgey = 0;
				if(x*k == 0)
				{
					edgex = 1;
				}
				if(y*k == 0)
				{
					edgey = 1;
				}
				int i = x*k + edgex;
				int j = y*k + edgey;
				double filter = 0;
				for(int l = j-1; l <=j+1; l++)
				{
					for(int m = i-1; m <= i+1; m++)
					{
						input.getPixel(m, l, rgb);
						filter += rgb[0] * 1.0 / 9.0;
					}
					
				}
				if(filter > 255)
				{
					filter = 255;
				}
				if(filter < 0)
				{
					filter = 0;
				}
				rgb[0] = (int)filter;
				rgb[1] = (int)filter;
				rgb[2] = (int)filter;
						
				
				
				
				
				
				ninthfilter.setPixel(x, y, rgb);
			}
		}
		ninthfilter.display("1/9 Filter");
	}
		
	
	private void averageFilter(int k) {
		// TODO Auto-generated method stub
		Image averagefilter = new Image(input.getW()/ k, input.getH() / k);
		whiteImage(averagefilter);
		int[] rgb = new int[3];
		for(int y =0; y < averagefilter.getH(); y++)
		{
			for(int x = 0; x < averagefilter.getW(); x++)
			{
				
				int average = 0;
				for(int i = 0; i < k; i++)
				{
					for(int j = 0; j < k; j++)
					{
						input.getPixel(x*k+j, y*k+i, rgb);
						average += rgb[0];
					}
				}
				average = average / (k*k);
				rgb[0] = average;
				rgb[1] = average;
				rgb[2] = average;
				averagefilter.setPixel(x, y, rgb);
				
				
			}
		}
		averagefilter.display("Average Filter");
	}
	private void noFilter(int k) {
		// TODO Auto-generated method stub
		Image nofilter = new Image(input.getW()/ k, input.getH() / k);
		whiteImage(nofilter);
		int[] rgb = new int[3];
		for(int y =0; y < nofilter.getH(); y++)
		{
			for(int x = 0; x < nofilter.getW(); x++)
			{
				input.getPixel(x*k, y*k, rgb);
				nofilter.setPixel(x, y, rgb);
			}
		}
		nofilter.display("No Filter");
	}
	
}
