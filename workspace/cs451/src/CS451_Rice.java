import java.io.File;
import java.util.Scanner;


public class CS451_Rice 
{
	public static void main(String[] args)
	 {
	    if(((args[0].equals("1"))||(args[0].equals("3")))&&(args.length != 2))
	    {
	      usage();
	      System.exit(1);
	    }

	    System.out.println("--Welcome to Multimedia Software System--");
	    if(args[0].equals("1"))
	    {
	    	ImageManipulation imgmanip = new ImageManipulation(args[1]);
	    	imageManipMenu(imgmanip, args);
	    }
	    if(args[0].equals("2"))
	    {
	    	encodingAliasing();
	    }
	    if(args[0].equals("3"))
	    {
	    	compress(args);
	    }
	    if(args[0].equals("4"))
	    {
	    	motionCompensation();
	    }

	    System.out.println("--Good Bye--");
	    System.exit(0);
	 }
		
	private static void motionCompensation() {
		// TODO Auto-generated method stub
		int task = 0;
		Scanner cin = new Scanner(System.in);
		do
		{
			System.out.println("Main Menu-------------------");
			System.out.println("1. Block based motion compensation");
			System.out.println("2. Remove Moving objects");
			System.out.println("3. Quit");
			task = cin.nextInt();
			
			if(task == 1)
			{
				Motion motion = new Motion("mv.txt");
				
				System.out.println("Target Frame?");
				String target;
				String reference;
				cin.nextLine();
				target = cin.nextLine();
				System.out.println("Reference frame");
				reference = cin.nextLine();
				motion.compensation(target,reference);
			}
			if(task == 2)
			{
				Motion motion = new Motion("mv.txt");
				System.out.println("Target Frame?");
				int target;
				target = cin.nextInt();
				
				motion.removeMoving(target,target-2);
			}
			
		}while(task != 3);
			
		
	}

	private static void compress(String[] args) {
		// TODO Auto-generated method stub
		int quality;
		Scanner cin = new Scanner(System.in);
		ImageCompress imgcompress = new ImageCompress(args[1]);
		do{
			System.out.println("Enter the quality where 0 is highest quality and 5 is the lowest.\nAny other number will exit the program.");
			quality = cin.nextInt();
			if((quality >= 0) && (quality <= 5))
			{
				imgcompress.compress(quality);
				ImageDecompress imgdecompress = new ImageDecompress(imgcompress.quantizedy,imgcompress.quantizedcr,imgcompress.quantizedcb);
				imgdecompress.decompress(quality);
			}
		}
		while((quality >= 0) && (quality <= 5));
		
		
	}

	public static void encodingAliasing() {
		int task;
		Scanner cin = new Scanner(System.in);
		do
		{
			System.out.println("Main Menu-----------------------------------");
			System.out.println("1. Aliasing");
			System.out.println("2. Dictionary Coding");
			System.out.println("3. Quit");
			task = cin.nextInt();
			if(task==1)
			{
				int m,n,k;
				System.out.println("Enter the M,N, and K values");
				System.out.println("M?");
				m = cin.nextInt();
				System.out.println("N?");
				n = cin.nextInt();
				System.out.println("K?"); 
				k = cin.nextInt();
				ImageManipulation imgmanip = new ImageManipulation(new Image(512,512));
				imgmanip.drawCircles(n,m);
				imgmanip.displayIn("Origonal");
				imgmanip.resize(k);
			}
			if(task==2)
			{

				System.out.println("Enter the dictionary size");
				int size = cin.nextInt();
				cin.nextLine();
				
				System.out.println("Enter the filepath to the file to be encoded");
				String filepath = cin.nextLine();
				
				LZWEncode encode = new LZWEncode(new File(filepath), size);
				encode.encode();
				encode.print();
				LZWDecode decode = new LZWDecode(encode.getEncodedline(), encode.getDictionary());
				decode.decode();
				decode.print();
			}
		}while(task != 3);
		
	}

	public static void imageManipMenu(ImageManipulation imgmanip, String args[]) {
		int task;
		Scanner cin = new Scanner(System.in);
		do
		{
			System.out.println("Main Menu-----------------------------------");
			System.out.println("1. Conversion to Gray-scale Image (24bits->8bits)");
			System.out.println("2. Conversion to Bi-level Image (24bits->1bit)");
			System.out.println("3. Conversion to N-level Image");
			System.out.println("4. Conversion to 8bit Indexed Color Image using Uniform Color Quantization (24bits->8bits)");
			System.out.println("5. Quit");
			task = cin.nextInt();
			if(task ==1)
			{
				imgmanip.greyScale();
				imgmanip.displayIn("Original");
				imgmanip.displayOut("Grey Scale");
			}
			if(task ==2)
			{
				System.out.println("Select the conversion option");
				System.out.println("1. conversion based on average grey");
				System.out.println("2. conversion using error diffusion");
				System.out.println("3. cancel");
				int option;
				do{
				option = cin.nextInt();
				}while(option !=1 && option !=2 && option !=3);
				if(option ==1)
				{
					imgmanip.biLevelavg();
					imgmanip.displayIn("Original");
					imgmanip.displayOut("Bi-Level based on average");
				}
				if(option == 2)
				{
					imgmanip.biLevelErrDif();
					imgmanip.displayIn("Original");
					imgmanip.displayOut("Bi-Level error diffusion");
				}
			}
			if(task ==3)
			{
				int n;
				System.out.println("enter the level");
				n = cin.nextInt();
				imgmanip.nLevel(n);
				imgmanip.displayIn("Original");
				imgmanip.displayOut(n + "-level image");
				
			}
			if(task ==4)
			{
				imgmanip.eightBitColor(args[1]);
				imgmanip.displayIn("Original");
				imgmanip.displayOut("8-bit color");
			}			
		}while(task != 5);
	}

	public static void usage()
	  {
	    System.out.println("\nUsage: java CS451_Rice 1 [inputfile]\n");
	  }
}
