import java.util.Scanner;

public class CS4551_Martinez {

	public static void main(String[] args) {
		if (args.length != 1) {
			usage();
			System.exit(1);
		}
		
		MImage img = new MImage(args[0]);
		menu(img);
		
	}
	
	public static void menu(MImage img) {
		System.out.println("Main Menu-----------------------------------\r\n"
				+ "1. 8-bit UCQ and Error Diffusion\r\n"
				+ "2. Generic UCQ and Error Diffusion\r\n"
				+ "3. Quit\r\n"
				+ "Please enter the task number [1-3]:"
				);
		Scanner input = new Scanner(System.in);
		
		try {
			int inp = input.nextInt();
			
			if (inp == 1) {
				ImageFilter ucq8Image = new ImageFilter(img);
				ucq8Image.uCQ8();
				System.out.println();
				menu(img);
			}
			else if (inp == 2) {
				ImageFilter ucqGImage = new ImageFilter(img);
				System.out.println("Please enter the number of index bits for each R");
				int nr = input.nextInt();
				System.out.println("Please enter the number of index bits for each G");
				int ng = input.nextInt();
				System.out.println("Please enter the number of index bits for each B");
				int nb = input.nextInt();
				
				ucqGImage.uCQG(nr, ng, nb);
				System.out.println();
				menu(img);
			}
			else if (inp == 3) {
				System.exit(1);
			}
			else {
				menu(img);
			}
			
		} catch(Exception ex) {
			System.out.println(ex);
		}
	}
	
	public static void usage() {
		System.out.println("\nUsage: java CS4551_Main [input_ppm_file]\n");
	}
}