import java.util.ArrayList;

public class EightBitImage extends Image {

	private int[][] table;
	private int red[];
	private int green[];
	private int blue[];
	
	public EightBitImage(int w, int h, int[][] lut) {
		super(w, h);
		this.table = lut;
		// TODO Auto-generated constructor stub
	}
	public EightBitImage(Image img, int[][] index)
	{
		super(img.getW(),img.getH());
		this.table = index;
		rgbVal();
		convert(img);
	}
	public EightBitImage(Image input, Image index) {
		// TODO Auto-generated constructor stub
		super(input.getW(),input.getH());
		getTable(index);
		rgbVal();
		convert(input);
	}
	private void getTable(Image index) {
		// TODO Auto-generated method stub
		int rgb[][] = new int[256][3];
		for(int x = 0; x < index.getW(); x++)
		{
			index.getPixel(x, 0, rgb[x]);		
		}
		this.table = rgb;
		
	}
	private void rgbVal() {
		// TODO Auto-generated method stub
		ArrayList<Integer> red = new ArrayList<Integer>();
		ArrayList<Integer> green = new ArrayList<Integer>();
		ArrayList<Integer> blue = new ArrayList<Integer>();
		for(int i = 0; i < table.length; i++)
		{
			if(!red.contains(table[i][0]))
				red.add(table[i][0]);
			if(!green.contains(table[i][1]))
				green.add(table[i][1]);
			if(!blue.contains(table[i][2]))
				blue.add(table[i][2]);
		}
		Integer[] r = new Integer[red.size()];
		Integer[] g = new Integer[green.size()];
		Integer[] b = new Integer[blue.size()];
		
		red.toArray(r);
		green.toArray(g);
		blue.toArray(b);
		
		this.red = integerToInt(r);
		this.green = integerToInt(g);
		this.blue = integerToInt(b);
		
		
		
		
	}
	private int[] integerToInt(Integer[] r) {
		// TODO Auto-generated method stub
		 
		int[] result = new int[r.length];
		for (int i = 0; i < r.length; i++) {
			result[i] = r[i].intValue();
		}
		return result;
	}
	private void convert(Image img) {
		// TODO Auto-generated method stub
		int rgb[] = new int[3];
		int rgb8[] = new int[3];
		for(int i = 0; i < this.getH(); i++)
		{
			for(int j = 0; j < this.getW(); j++)
			{
				img.getPixel(j, i, rgb);
				int id = closest(rgb);
				rgb8 = this.table[id];
				this.setPixel(j, i, rgb8);
			}
		}
		
	}
	private int closest(int[] rgb) {
		// TODO Auto-generated method stub
		int index[] = new int[3];
		int rgb8[] = new int[3];
		int rclose,gclose,bclose;
		rclose = 500;
		gclose = 500;
		bclose = 500;
		for(int r = 0; r < red.length; r++)
		{
			int temp = Math.abs(rgb[0] - red[r]);
			if(temp < rclose)
			{
				rclose = temp;
				index[0] = r;
			}
		}
		for(int g = 0; g < green.length; g++)
		{
			int temp = Math.abs(rgb[1] - green[g]);
			if(temp < gclose)
			{
				gclose = temp;
				index[1] = g;
			}
		}
		for(int b = 0; b < blue.length; b++)
		{
			int temp = Math.abs(rgb[2] - blue[b]);
			if(temp < bclose)
			{
				bclose = temp;
				index[2] = b;
			}
		}
		rgb8[0] = red[index[0]];
		rgb8[1] = green[index[1]];
		rgb8[2] = blue[index[2]];
		
		int id = find(rgb8);
		
		return id;
	}
	private int find(int[] rgb8) {
		// TODO Auto-generated method stub
		for(int i = 0; i < table.length; i ++)
		{
			if(table[i][0] == rgb8[0])
			{
				if(table[i][1] == rgb8[1])
				{
					if(table[i][2] == rgb8[2])
					{
						return i;
					}
				}
			}
		}
		return -1;
	}
	

}
