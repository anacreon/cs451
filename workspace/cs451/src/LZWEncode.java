import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public class LZWEncode {
	int size;
	private ArrayList<String> dictionaryinit = new ArrayList<String>(); //this is what is going to be sent with the encoded string
	private ArrayList<String> dictionaryfull = new ArrayList<String>();
	public String line;
	private String encodedline = "";
	public LZWEncode(File file, int size) {
		// TODO Auto-generated constructor stub
		this.size = size;
		readTextfile(file);
	}
	public LZWEncode(String string, int size)
	{
		this.line = string;
		this.size = size;
	}

	public void encode() {
		// TODO Auto-generated method stub
		for(int i = 0; i < line.length(); i++)
		{
			String temp;
			temp = line.substring(i, i+1);
			if(!dictionaryinit.contains(temp))
			{
				if(dictionaryinit.size() <= size)
				{
					dictionaryinit.add(temp);
					dictionaryfull.add(temp);
				}
			}
		}
		int i = 0;
		while (i < line.length())
		{
			for(int j = line.length(); j > i; j--)
			{
				String substring = line.substring(i, j);
				if(dictionaryfull.contains(substring))
				{
					int index = dictionaryfull.indexOf(substring);
					int old = i;
					i = i + substring.length();
					encodedline = encodedline + index + " ";
					if((dictionaryfull.size() <= size) && ((j+1) <= line.length()))
					{
						String entry = line.substring(old, j+1);
						if(!dictionaryfull.contains(entry))
						{
							dictionaryfull.add(entry);
						}
						
					}
				}
			}
		}
		
		
		
		
	}
	public void print()
	{
		
		System.out.println("Encoded: " + encodedline);
		double compression = compressionratio();
		System.out.println("Compression Ratio: " + compression);
		System.out.println("Dictionary generated");
		for(int i = 0; i < dictionaryfull.size(); i ++)
		{
			System.out.println(i + " | " + dictionaryfull.get(i));
		}
	}
	private double compressionratio() {
		// TODO Auto-generated method stub
		double uncompressed = line.length() * 8;
		int compressed = dictionaryfull.size();
		int bitcount = 0;
		while(compressed != 0)
		{
			compressed = compressed /2;
			bitcount ++;
		}
		double ratio = uncompressed /(bitcount * encodedline.split(" ").length);
		
		
		return ratio;
	}
	private void readTextfile(File file) {
		// TODO Auto-generated method stub
		BufferedReader br = null;
		
		try
		{
			br = new BufferedReader(new FileReader(file));
			System.out.println("Opened " + file.getName() + " Successfully");
			this.line = br.readLine();
			
			
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
	public String getEncodedline() {
		// TODO Auto-generated method stub
		return this.encodedline;
	}
	public ArrayList<String> getDictionary() {
		// TODO Auto-generated method stub
		return this.dictionaryinit;
	}

}
