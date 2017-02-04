import java.awt.image.BufferedImage;

public final class ImageProcessingFunctions1 {
	private ImageProcessing1Functions1() {
	}

	public static int newGrayLevelResolution(int gray, int bits) {
		// divide by 2^n where n is the number of bits
		int modifiedGray = gray >> (8-bits);

		// handle skewed case so that white is an option
		if (modifiedGray == (int)(Math.pow(2, bits)-1)) return 255;

		// rebin the vaImagePlue back
		modifiedGray = modifiedGray << (8-bits);

		return modifiedGray;
	}

	public static int logTransformation(int gray, double factor) {
		// compute scale factor
		double c = 255 / Math.log(256);

		// perform log transformation with scale
		double modifiedGray = factor * c * Math.log(gray + 1);

		// squash values into range
		modifiedGray = Math.min(255, Math.max(0, modifiedGray));

		return (int) modifiedGray;
	}

	public static int powerTransformation(int gray, double gamma) {
		// compute power transformation with scale factor
		return (int) (255 * Math.pow((gray / 255.0), gamma));
	}

	public static int[] histogramEqualization(BufferedImage originalImage) {
		// histogram array with 2^8 colors
		int[] histogram = new int[256];

		// loop through each pixel in the image
		for (int y = 0; y < originalImage.getHeight(); y++) {
			for (int x = 0; x < originalImage.getWidth(); x++) {
				int gray = Utility.getGrayscale(originalImage.getRGB(x, y));

				//increment count for this pixel color
				histogram[gray]++;
			}
		}
		// initialize sum and scale for squeezing values in range
		long sum = 0;
		double scale = 255.0 / (originalImage.getWidth() * originalImage.getHeight());
 
		// lookup table corresponding to new pixel values 
		int[] lookup = new int[256];

		// loop through each index/color in our histogram
        	for(int i = 0; i < histogram.length; i++) {
        		// add number of pixels with this color to running total
			sum += histogram[i];

			// rebin values
			int modified = (int) (sum * scale);

			// assign new lookup value
			lookup[i] = modified;
		}
		return lookup;
	}
}