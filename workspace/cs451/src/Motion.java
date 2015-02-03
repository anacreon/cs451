import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Motion {

	BufferedWriter bw;
	Image tframe;
	Image rframe;
	String vectors;
	Image error;
	Motion(String outputFile)
	{
		
		try {
			bw = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void compensation(String target, String reference) {
		// TODO Auto-generated method stub
		this.tframe = new Image(target);
		this.rframe = new Image(reference);
		this.error = new Image(tframe.getW(), tframe.getH());
		this.vectors = block(tframe,rframe);
		
		this.error.display("error image");
		print(vectors);
		cleanup();
		
	}
	
	public void removeMoving(int target, int reference)
	{
		String starget;
		String ref;
		
		if((target / 10) < 1)
		{
			starget = "Walk_00"+ target + ".ppm";
		}
		else if((target / 100) < 1)
		{
			starget = "Walk_0" + target +".ppm";
		}
		else
		{
			starget = "Walk_" + target +".ppm";
		}
		
		if((reference / 10) < 1)
		{
			ref = "Walk_00"+ reference + ".ppm";
		}
		else if((reference / 100) < 1)
		{
			ref = "Walk_0"+ reference + ".ppm";
		}
		else
		{
			ref = "Walk_"+ reference + ".ppm";
		}
		
		this.tframe = new Image(starget);
		tframe.display("before");
		Image tframe2 = new Image(starget);
		Image rframe2 = new Image(ref);
		this.rframe = new Image("Walk_005.ppm");
		
		
		
		adjacent(tframe,rframe2,0);
		adjacent(tframe2, rframe2,1);
		tframe.display("After adjacent");
		tframe2.display("After reference replace");
		
	}

	private void adjacent(Image tframe, Image rframe, int methood) {
		// TODO Auto-generated method stub
		int[] rgb = new int[3];
		int[][] tblock = new int[16][16];
		int[][] rblock = new int[16][16];
		int[] coords = new int[2];
		int[][] errorred = new int[tframe.getW()][tframe.getH()];
		int[][] errorgreen = new int[tframe.getW()][tframe.getH()];
		int[][] errorblue = new int[tframe.getW()][tframe.getH()];
		String vectors = "";
		for(int y = 0; y < tframe.getH(); y = y + 16)
		{
			for(int x = 0; x < tframe.getW(); x = x + 16)
			{
				
				for(int i = y; i < y+16; i++)
				{
					for(int j= x; j < x+16; j++)
					{
						tframe.getPixel(j, i, rgb);
						int grey = (int) Math.round(0.299*rgb[0] + 0.587*rgb[1] + 0.114*rgb[2]);
						if(grey > 255)
							grey = 255;
						if(grey < 0)
							grey = 0;
						
						tblock[j%16][i%16] = grey;
						
						
					}
				}
				coords = blockCycle(tblock,rblock, rframe, x, y);
				int dx, dy;
				dx = coords[0] - x;
				dy = coords[1] - y;
				if((dx > 1) || (dy>1))
				{
					if(methood == 0)
					{
						for(int i = 0; i < 16; i++)
						{
							for(int j = 0; j < 16; j++)
							{
								int[] trgb = new int[3];
								tframe.getPixel(j + (x -16), i + y, trgb);
								tframe.setPixel(j+x, i + y, trgb);
							
							}
						}
					}
					if(methood == 1)
					{
						for(int i = 0; i < 16; i++)
						{
							for(int j = 0; j < 16; j++)
							{
								int[] trgb = new int[3];
								this.rframe.getPixel(j + x, i + y, trgb);
								tframe.setPixel(j + x, i + y, trgb);
							 
							}
						}
					}
				}
				
			}
			
		}
	}

	private void cleanup() {
		// TODO Auto-generated method stub
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void print(String vectors) {
		// TODO Auto-generated method stub
		try {
			bw.write(vectors);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private String block(Image tframe, Image rframe) {
		// TODO Auto-generated method stub
		
		int[] rgb = new int[3];
		int[][] tblock = new int[16][16];
		int[][] rblock = new int[16][16];
		int[] coords = new int[2];
		int[][] errorred = new int[tframe.getW()][tframe.getH()];
		int[][] errorgreen = new int[tframe.getW()][tframe.getH()];
		int[][] errorblue = new int[tframe.getW()][tframe.getH()];
		String vectors = "";
		for(int y = 0; y < tframe.getH(); y = y + 16)
		{
			for(int x = 0; x < tframe.getW(); x = x + 16)
			{
				
				for(int i = y; i < y+16; i++)
				{
					for(int j= x; j < x+16; j++)
					{
						tframe.getPixel(j, i, rgb);
						int grey = (int) Math.round(0.299*rgb[0] + 0.587*rgb[1] + 0.114*rgb[2]);
						if(grey > 255)
							grey = 255;
						if(grey < 0)
							grey = 0;
						
						tblock[j%16][i%16] = grey;
						
						
					}
				}
				coords = blockCycle(tblock,rblock, rframe, x, y);
				int dx, dy;
				dx = coords[0] - x;
				dy = coords[1] - y;
				vectors += "[" + dx + ", " + dy + "] ";
				for(int i = 0; i < 16; i++)
				{
					for(int j = 0; j < 16; j++)
					{
						int[] trgb = new int[3];
						int[] rrgb = new int[3];
						int[] drgb = new int[3];
						tframe.getPixel(j + x, i + y, trgb);
						rframe.getPixel(j + coords[0],i + coords[1], rrgb);
						
						drgb[0] = trgb[0] - rrgb[0];
						drgb[1] = trgb[1] - rrgb[1];
						drgb[2] = trgb[2] - rrgb[2];
						
						errorred[j][i] = drgb[0];
						errorgreen[j][i] = drgb[1];
						errorblue[j][i] = drgb[2];
					}
				}
				
				
			}
			vectors+= "\n";
		}
		scale(errorred, errorgreen, errorblue);
		createErrorImage(errorred,errorgreen,errorblue);
		return vectors;
		
		
	}

	private void createErrorImage(int[][] errorred, int[][] errorgreen, int[][] errorblue) {
		// TODO Auto-generated method stub
		int[] rgb = new int[3];
		for(int i = 0; i < this.error.getH(); i++)
		{
			for(int k = 0; k < this.error.getW(); k++)
			{
				
				rgb[0] = errorred[k][i];
				rgb[1] = errorgreen[k][i];
				rgb[2] = errorblue[k][i];
				
				int grey = (int) Math.round(0.299*rgb[0] + 0.587*rgb[1] + 0.114*rgb[2]);
				
				
				if(grey < 0)
					grey = 0;
				if(grey > 255)
					grey = 255;
				
				rgb[0] = grey;
				rgb[1] = grey;
				rgb[2] = grey;
				
				this.error.setPixel(k, i, rgb);
			}
		}
	}

	private void scale(int[][] errorred, int[][] errorgreen, int[][] errorblue) {
		// TODO Auto-generated method stub
		int minred = Integer.MAX_VALUE;
		int maxred = Integer.MIN_VALUE;
		
		int mingreen = Integer.MAX_VALUE;
		int maxgreen = Integer.MIN_VALUE;
		
		int minblue = Integer.MAX_VALUE;
		int maxblue = Integer.MIN_VALUE;
		
		for(int i = 0; i < errorred[0].length; i++)
		{
			for(int k = 0; k < errorred.length; k++)
			{
				if(minred > errorred[k][i])
				{
					minred = errorred[k][i];
				}
				if(maxred < errorred[k][i])
				{
					maxred = errorred[k][i];
				}
			}
		}
		
		for(int i = 0; i < errorgreen[0].length; i++)
		{
			for(int k = 0; k < errorgreen.length; k++)
			{
				if(mingreen > errorgreen[k][i])
				{
					mingreen = errorgreen[k][i];
				}
				if(maxgreen < errorgreen[k][i])
				{
					maxgreen = errorgreen[k][i];
				}
			}
		}
		
		for(int i = 0; i < errorblue[0].length; i++)
		{
			for(int k = 0; k < errorblue.length; k++)
			{
				if(minblue > errorblue[k][i])
				{
					minblue = errorblue[k][i];
				}
				if(maxblue < errorblue[k][i])
				{
					maxblue = errorblue[k][i];
				}
			}
		}
		
		
		int rangered = maxred - minred;
		double conversionred = 255.0 / rangered;
		for(int i = 0; i < errorred[0].length; i++)
		{
			for(int k = 0; k < errorred.length; k++)
			{
				errorred[k][i] = (int) (conversionred * (errorred[k][i] - minred));
			}
		}
		
		int rangegreen = maxgreen - mingreen;
		double conversiongreen = 255.0 / rangegreen;
		for(int i = 0; i < errorgreen[0].length; i++)
		{
			for(int k = 0; k < errorgreen.length; k++)
			{
				errorgreen[k][i] = (int) (conversiongreen * (errorgreen[k][i] - mingreen));
			}
		}
		
		int rangeblue = maxblue - minblue;
		double conversionblue = 255.0 / rangeblue;
		for(int i = 0; i < errorblue[0].length; i++)
		{
			for(int k = 0; k < errorblue.length; k++)
			{
				errorblue[k][i] = (int) (conversionblue * (errorblue[k][i] - minblue));
			}
		}
	}

	private int[] blockCycle(int[][] block, int[][] refblock, Image rframe, int x, int y) {
		// TODO Auto-generated method stub
		
		int lowx = x - 12;
		int lowy = y - 12;
		
		int highx = x + 12;
		int highy = y + 12;
		
		if((highx+16) >= rframe.getW())
			highx = rframe.getW() - 15;
		
		if((highy+16) >= rframe.getH())
			highy = rframe.getH() - 15;
		
		if(lowx < 0)
			lowx = 0;
		
		if(lowy < 0)
			lowy = 0;
		
		int[][] rblock = new int[16][16];
		int[] rgb = new int[3];
		int[] bestcoords = new int[2];
		double rating = Integer.MAX_VALUE;
		for(int i = lowy; i < highy; i++)
		{
			for(int j = lowx; j < highx; j++)
			{
				
				
				for(int l = i; l < (i + 16); l++)
				{
					for(int m = j; m < (j+16); m++)
					{
						rframe.getPixel(m, l, rgb);
						int grey = (int) Math.round(0.299*rgb[0] + 0.587*rgb[1] + 0.114*rgb[2]);
						if(grey > 255)
							grey = 255;
						if(grey < 0)
							grey = 0;
						rblock[m - j][l - i] = grey;
					}
				}
				double temp = 0;
				for(int l = 0; l < 16; l++)
				{
					for(int m = 0; m < 16; m++)
					{
						temp += Math.pow(block[m][l] - rblock[m][l], 2);
					}
				}
				temp = temp / (16 * 16);
				if(temp < rating)
				{
					rating = temp;
					bestcoords[0] = j;
					bestcoords[1] = i;
					refblock = rblock;
				}
				
			}
		}
		return bestcoords;
		
		
		
	}
	
	

}
