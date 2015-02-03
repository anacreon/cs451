import java.util.ArrayList;


public class LZWDecode {
	
	private ArrayList<String> dictionary;
	private String encodedline;
	String decodedline ="";
	final int MAX_SIZE = 255;
	public LZWDecode(String encodedline, ArrayList<String> dictionary) {
		// TODO Auto-generated constructor stub
		this.encodedline = encodedline;
		this.dictionary = dictionary;
	}
	public void decode()
	{
		String[] indicies = encodedline.split(" ");
		int[] index = new int[indicies.length];
		for(int i = 0; i < indicies.length; i ++)
		{
			index[i] = Integer.parseInt(indicies[i]);
		}
		for(int i = 0; i < index.length; i++)
		{
			int k = i + 1;
			String entry = dictionary.get(index[i]);
			this.decodedline = this.decodedline + entry;
			if((k < index.length) && (dictionary.size() > index[k]))
			{
				String next = dictionary.get(index[k]);
				if(dictionary.size() <= this.MAX_SIZE)
					dictionary.add(entry + next.charAt(0));
			}
			else
			{
				if(dictionary.size() <= this.MAX_SIZE)
					dictionary.add(entry + entry.charAt(0));
			}
		}
		
	}
	public void print()
	{
		System.out.println("Decoded message: " + this.decodedline);
	}
}
