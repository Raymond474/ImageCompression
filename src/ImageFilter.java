public class ImageFilter {

	public static MImage inImage;
	
	public ImageFilter(MImage inImage) {
		this.inImage = inImage;
	}
	
	public static void uCQG(int nr, int ng, int nb) {
		int height = inImage.getH();
		int width = inImage.getW();
		int[][][] quant = new int[height][width][2];
		quantG(quant, nr, ng, nb);
		int[][][] error = new int[height][width][2];
		errorPix(quant, error);
		//error diffusion
		MImage ucqImage = new MImage(width, height);
		int[] rtx = new int[3];
		inImage.getPixel(0, 0, rtx);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				//right pixel
				if (x + 1 < width) {
					double FLOYDR = 7 / 16.0; 
					int[] rgb = new int[3];
					inImage.getPixel(x + 1, y, rgb);
					errorDif(FLOYDR, rgb, error[y][x]);	
					ucqImage.setPixel(x + 1, y, rgb);
				}//bottom-left pixel
				if (x - 1 > 0 && y + 1 < height) {
					double FLOYDBL = 3 / 16.0;
					int[] rgb = new int[3];
					inImage.getPixel(x - 1, y + 1, rgb);
					errorDif(FLOYDBL, rgb, error[y][x]);
					ucqImage.setPixel(x - 1, y + 1, rgb);
					
				}//bottom pixel
				if (y + 1 < height) {
					double FLOYDB = 5 / 16.0;
					int[] rgb = new int[3];
					inImage.getPixel(x, y + 1, rgb);
					errorDif(FLOYDB, rgb, error[y][x]);
					ucqImage.setPixel(x, y + 1, rgb);
				}//bottom-right pixel
				if (x + 1 < width && y + 1 < height) {
					double FLOYDBR = 1 / 16.0;
					int[] rgb = new int[3];
					inImage.getPixel(x + 1, y + 1, rgb);
					errorDif(FLOYDBR, rgb, error[y][x]);
					ucqImage.setPixel(x + 1, y + 1, rgb);
				}
			}
		}
		
		String name = createName(inImage.getName(), String.format("r%1$dg%2$db%3$d", nr, ng, nb));
		ucqImage.write2PPM(name);
	}
	
	public static void uCQ8() {
		int[][][] quant = new int[inImage.getH()][inImage.getW()][2];
		quant(quant);
		int[][][] error = new int[inImage.getH()][inImage.getW()][2];
		errorPix(quant, error);
		//error diffusion
		int height = inImage.getH();
		int width = inImage.getW();
		MImage ucqImage = new MImage(width, height);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				//right pixel
				if (x + 1 < width) {
					double FLOYDR = 7 / 16.0; 
					int[] rgb = new int[3];
					inImage.getPixel(x + 1, y, rgb);
					errorDif(FLOYDR, rgb, error[y][x]);	
					ucqImage.setPixel(x + 1, y, rgb);
					
				}//bottom-left pixel
				if (x - 1 > 0 && y + 1 < height) {
					double FLOYDBL = 3 / 16.0;
					int[] rgb = new int[3];
					inImage.getPixel(x - 1, y + 1, rgb);
					errorDif(FLOYDBL, rgb, error[y][x]);
					ucqImage.setPixel(x - 1, y + 1, rgb);
					
				}//bottom pixel
				if (y + 1 < height) {
					double FLOYDB = 5 / 16.0;
					int[] rgb = new int[3];
					inImage.getPixel(x, y + 1, rgb);
					errorDif(FLOYDB, rgb, error[y][x]);
					ucqImage.setPixel(x, y + 1, rgb);
				}//bottom-right pixel
				if (x + 1 < width && y + 1 < height) {
					double FLOYDBR = 1 / 16.0;
					int[] rgb = new int[3];
					inImage.getPixel(x + 1, y + 1, rgb);
					errorDif(FLOYDBR, rgb, error[y][x]);
					ucqImage.setPixel(x + 1, y + 1, rgb);
				}
			}
		}
		
		String name = createName(inImage.getName(), "ucq8bit");
		ucqImage.write2PPM(name);
	}
	
	private static void quant(int[][][] quant) {
		int height = inImage.getH();
		int width = inImage.getW();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int[] rgb = new int[3];
				inImage.getPixel(x, y, rgb);
				
				int R = rgb[0];
				int G = rgb[1];
				int B = rgb[2];
				
				R = ((int) (R / 32.0)) * 32 + 16;
				G = ((int) (G / 32.0)) * 32 + 16;
				B = ((int) (B / 64.0)) * 64 + 32;
				
				rgb[0] = R;
				rgb[1] = G;
				rgb[2] = B;
				quant[y][x] = rgb;
			}
		}
	}
	
	private static void errorPix(int[][][] quant, int[][][] error) {
		int height = inImage.getH();
		int width = inImage.getW();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int[] rgb = new int[3];
				inImage.getPixel(x, y, rgb);
				
				int R = rgb[0];
				int G = rgb[1];
				int B = rgb[2];
				
				int eR = R - quant[y][x][0];
				int eG = G - quant[y][x][1];
				int eB = B - quant[y][x][2];
				rgb[0] = eR;
				rgb[1] = eG;
				rgb[2] = eB;
				error[y][x] = rgb;
			}
		}
	}
	
	private static void errorDif(double FLOYD, int[] rgb, int[] errorRGB) {
		rgb[0] = (int) (rgb[0] + Math.round(FLOYD * errorRGB[0]));
		rgb[1] = (int) (rgb[1] + Math.round(FLOYD * errorRGB[1]));
		rgb[2] = (int) (rgb[2] + Math.round(FLOYD * errorRGB[2]));
		
		if (rgb[0] > 255)
			rgb[0] = 255;
		else if (rgb[0] < 0)
			rgb[0] = 0;
		if (rgb[1] > 255)
			rgb[1] = 255;
		else if (rgb[1] < 0)
			rgb[1] = 0;
		if (rgb[2] > 255)
			rgb[2] = 255;
		else if (rgb[2] < 0)
			rgb[2] = 0;
	}
	
	private static void quantG(int[][][] quant, int nr, int ng, int nb) {
		int height = inImage.getH();
		int width = inImage.getW();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int[] rgb = new int[3];
				inImage.getPixel(x, y, rgb);
				int R = (int) Math.round(Math.pow(2, 8 - nr));
				int G = (int) Math.round(Math.pow(2, 8 - ng));
				int B = (int) Math.round(Math.pow(2, 8 - nb));
				
				rgb[0] = ((int) (rgb[0] / R)) * R + (R / 2);
				rgb[1] = ((int) (rgb[1] / G)) * G + (R / 2);
				rgb[2] = ((int) (rgb[2] / B)) * B + (R / 2);
				
				if (nr == 1 ) {
					int[] rgb2 = new int[3];
					inImage.getPixel(x, y, rgb2);
					
					if (rgb2[0] >= 128)
						rgb[0] = 255;
					else if (rgb2[0] < 128)
						rgb[0] = 0;
				}
				if (ng == 1) {
					int[] rgb2 = new int[3];
					inImage.getPixel(x, y, rgb2);
					
					if (rgb2[1] >= 128)
						rgb[1] = 255;
					else if (rgb2[1] < 128)
						rgb[1] = 0;
				}
				if (nb == 1) {
					int[] rgb2 = new int[3];
					inImage.getPixel(x, y, rgb2);
					
					if (rgb2[2] >= 128)
						rgb[2] = 255;
					else if (rgb2[2] < 128)
						rgb[2] = 0;
				}
				
				quant[y][x] = rgb;
			}
		}
	}
	
	private static String createName(String fileName, String suffix) {
		String[] string = fileName.split(".ppm");
		fileName = string[0];
		fileName += "-" + suffix + ".ppm";
		return fileName;
	}
}