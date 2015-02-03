
public class DCTTest {
	
	public static void main(String args[])
	{
		ImageCompress dct = new ImageCompress(new Image(10,10));
		ImageDecompress idct = new ImageDecompress(new Image(10,10));
		double[][] input1 = {
				{139, 144, 149, 153, 155, 155, 155, 155},
				{144, 151, 153, 156, 159, 156, 156, 156},
				{150, 155, 160, 163, 158, 156, 156, 156},
				{159, 161, 162, 160, 160, 159, 159, 159},
				{159, 160, 161, 162, 162, 155, 155, 155},
				{161, 161, 161, 161, 160, 157, 157, 157},
				{162, 162, 161, 163, 162, 157, 157, 157},
				{162, 162, 161, 161, 163, 158, 158, 158}
			};

			double[][] input2 = {
				{200, 202, 189, 188, 189, 175, 175, 175},
				{200, 203, 198, 188, 189, 182, 178, 175},
				{203, 200, 200, 195, 200, 187, 185, 175},
				{200, 200, 200, 200, 197, 187, 187, 187},
				{200, 205, 200, 200, 195, 188, 187, 175},
				{200, 200, 200, 200, 200, 190, 187, 175},
				{205, 200, 199, 200, 191, 187, 187, 175},
				{210, 200, 200, 200, 188, 185, 187, 186}
			};

			double[][] input3 = {
				{178, 187, 183, 175, 178, 177, 150, 183},
				{191, 174, 171, 182, 176, 171, 170, 188},
				{199, 153, 128, 177, 171, 167, 173, 183},
				{195, 178, 158, 167, 167, 165, 166, 177},
				{190, 186, 159, 155, 159, 164, 158, 178},
				{194, 184, 137, 148, 157, 158, 150, 173},
				{200, 194, 148, 151, 161, 155, 148, 167},
				{200, 195, 172, 159, 159, 152, 156, 154}
			};
		double[][] output1 = new double[8][8];
		double[][] output2 = new double[8][8];
		double[][] output3 = new double[8][8];
		int[] dimensions = {8,8};
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j<8; j++)
			{
				output1[i][j] = 0;
				input1[i][j] = input1[i][j] -128;
				output2[i][j] = 0;
				input2[i][j] = input2[i][j] -128;
				output3[i][j] = 0;
				input3[i][j] = input3[i][j] -128;
			}
		}
		
		
		
		dct.dct(input1, dimensions, output1);
		dct.dct(input2, dimensions, output2);
		dct.dct(input3, dimensions, output3);
		
		System.out.println("original input 1");
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j<8; j++)
			{
				System.out.print(input1[i][j] + " ");
			}
			System.out.print("\n");
		}
		idct.idct(output1, dimensions, input1);
		
		
		System.out.println("output 1");
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j<8; j++)
			{
				System.out.print(output1[i][j] + " ");
			}
			System.out.print("\n");
		}
		System.out.println(" input 1 after idct");
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j<8; j++)
			{
				System.out.print(input1[i][j] + " ");
			}
			System.out.print("\n");
		}
		
		
		System.out.println("original input 2");
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j<8; j++)
			{
				System.out.print(input2[i][j] + " ");
			}
			System.out.print("\n");
		}
		idct.idct(output2, dimensions, input2);
		
		System.out.println("output 2");
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j<8; j++)
			{
				System.out.print(output2[i][j] + " ");
			}
			System.out.print("\n");
		}
		
		System.out.println(" input 2 after idct");
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j<8; j++)
			{
				System.out.print(input2[i][j] + " ");
			}
			System.out.print("\n");
		}
		
		System.out.println("original input 3");
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j<8; j++)
			{
				System.out.print(input2[i][j] + " ");
			}
			System.out.print("\n");
		}
		idct.idct(output3, dimensions, input3);
		
		System.out.println("output 3");
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j<8; j++)
			{
				System.out.print(output3[i][j] + " ");
			}
			System.out.print("\n");
		}
		
		System.out.println("input 3 after idct");
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j<8; j++)
			{
				System.out.print(input3[i][j] + " ");
			}
			System.out.print("\n");
		}
		
		
		
	}

}
