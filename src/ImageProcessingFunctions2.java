import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

public class ImageProcessingFunctions2 {
	private ImageProcessingFunctions2() {
	}

	public static int arithmeticMean(BufferedImage image, int x, int y, int k) {
		// averaging amount
		int count = 0;

		// iteratively added grayscale
		double totalGray = 0.0;

		// negative->positive index for looping through kernel and image
		int offset = k / 2;

		// loop through kernel and perform convolution
		for (int i = -offset; i < offset; i++) {
			for (int j = -offset; j < offset; j++) {
				// continue if pixel is outside bounds of image
				if (x + i < 0 || x + i >= image.getWidth() || y + j < 0 || y + j >= image.getHeight())
					continue;

				// grab grayscale value
				int gray = Utility.getGrayscale(image.getRGB(x + i, y + j));

				// add each color
				totalGray += gray;

				// increment total value used for averaging
				count++;
			}
		}
		// can't divide by zero so return 0
		if (count == 0) return 0;

		return Utility.squash(totalGray / count);
	}

	public static int geometricMean(BufferedImage image, int x, int y, int k) {
		// averaging amount
		double count = 0;

		// iteratively multipled grayscale
		double totalGray = 1;

		// negative->positive index for looping through kernel and image
		int offset = k / 2;

		// loop through kernel and perform convolution
		for (int i = -offset; i < offset; i++) {
			for (int j = -offset; j < offset; j++) {
				// continue if pixel is outside bounds of image
				if (x + i < 0 || x + i >= image.getWidth() || y + j < 0 || y + j >= image.getHeight())
					continue;

				// grab grayscale value
				int gray = Utility.getGrayscale(image.getRGB(x + i, y + j));

				// multiply each color
				totalGray *= gray;

				// increment total value used for averaging
				count++;
			}
		}
		// can't divide by zero so return 0
		if (count == 0) return 0;

		// geometric mean calculation
		double modifiedGray = Math.pow(totalGray, 1.0/count);

		// squash color value between 0 - 255
		return Utility.squash(modifiedGray);
	}

	public static int harmonicMean(BufferedImage image, int x, int y, int k) {
		// averaging amount
		int count = 0;

		// iteratively added grayscale
		double totalGray = 0;

		// negative->positive index for looping through kernel and image
		int offset = k / 2;

		// loop through kernel and perform convolution
		for (int i = -offset; i < offset; i++) {
			for (int j = -offset; j < offset; j++) {
				// continue if pixel is outside bounds of image
				if (x + i < 0 || x + i >= image.getWidth() || y + j < 0 || y + j >= image.getHeight())
					continue;

				// grab grayscale value
				int gray = Utility.getGrayscale(image.getRGB(x + i, y + j));

				// add each inverse color
				totalGray += 1.0/gray;

				// increment total value used for averaging
				count++;
			}
		}
		// can't divide by zero so return 0
		if (totalGray == 0) return 0;

		// harmonic mean calculation
		double modifiedGray = count / totalGray;

		// squash color values between 0 - 255
		return Utility.squash(modifiedGray);
	}

	public static int contraHarmonicMean(BufferedImage image, int x, int y, int k) {
		// averaging amount
		int count = 0;

		// iteratively added grayscale numerator and denominator
		double grayNumerator = 0.0;
		double grayDenominator = 0.0;

		// negative->positive index for looping through kernel and image
		int offset = k / 2;

		// loop through kernel and perform convolution
		for (int i = -offset; i < offset; i++) {
			for (int j = -offset; j < offset; j++) {
				// continue if pixel is outside bounds of image
				if (x + i < 0 || x + i >= image.getWidth() || y + j < 0 || y + j >= image.getHeight())
					continue;

				// grab grayscale value
				int gray = Utility.getGrayscale(image.getRGB(x + i, y + j));

				// add each color squared to numerator
				// add each color to denominator
				grayNumerator += Math.pow(gray, 2);
				grayDenominator += gray;

				// increment total value used for averaging
				count++;
			}
		}
		// can't divide by zero so return 0
		if (count == 0) return 0;

		// contra harmonic mean calculation
		// squash color values between 0 - 255
		return Utility.squash(grayNumerator / grayDenominator);
	}

	public static int max(BufferedImage image, int x, int y, int k) {
		//new gray value
		int max = 0;

		// negative->positive index for looping through kernel and image
		int offset = k / 2;

		// loop through kernel and perform convolution
		for (int i = -offset; i < offset; i++) {
			for (int j = -offset; j < offset; j++) {
				// continue if pixel is outside bounds of image
				if (x + i < 0 || x + i >= image.getWidth() || y + j < 0 || y + j >= image.getHeight())
					continue;

				// grab grayscale value
				int gray = Utility.getGrayscale(image.getRGB(x + i, y + j));

				// max calculation
				if (gray > max) max = gray;
			}
		}
		return max;
	}

	public static int min(BufferedImage image, int x, int y, int k) {
		//new gray value
		int min = 255;

		// negative->positive index for looping through kernel and image
		int offset = k / 2;

		// loop through kernel and perform convolution
		for (int i = -offset; i < offset; i++) {
			for (int j = -offset; j < offset; j++) {
				// continue if pixel is outside bounds of image
				if (x + i < 0 || x + i >= image.getWidth() || y + j < 0 || y + j >= image.getHeight())
					continue;

				// grab grayscale value
				int gray = Utility.getGrayscale(image.getRGB(x + i, y + j));

				// min calculation
				if (gray < min) min = gray;
			}
		}
		return min;
	}

	public static int midpoint(BufferedImage image, int x, int y, int k) {
		// grab min/max values in kernel neighborhood
		int min = min(image, x, y, k);
		int max = max(image, x, y, k);

		// return the midpoint of the two values
		return (min + max) / 2;
	}

	public static int median(BufferedImage image, int x, int y, int k) {
		//gray values in kernel neighborhood
		ArrayList<Integer> grays = new ArrayList<Integer>();

		// negative->positive index for looping through kernel and image
		int offset = k / 2;

		// loop through kernel and perform convolution
		for (int i = -offset; i < offset; i++) {
			for (int j = -offset; j < offset; j++) {
				// continue if pixel is outside bounds of image
				if (x + i < 0 || x + i >= image.getWidth() || y + j < 0 || y + j >= image.getHeight())
					continue;

				// grab grayscale value
				int gray = Utility.getGrayscale(image.getRGB(x + i, y + j));

				// store this gray value for later use
				grays.add(gray);
			}
		}
		// sort the array to find the median
		Collections.sort(grays);

		// find middle index
		int middle = grays.size() / 2;

		if (grays.size() % 2 == 1) {
			// return median value
			return grays.get(middle);
		} else {
			// return average of two median values (even number of items)
			return (int) ((grays.get(middle-1) + grays.get(middle)) / 2.0);
		}
	}

	public static int alphaTrimmed(BufferedImage image, int x, int y, int k, int alpha) {
		//gray values in kernel neighborhood
		ArrayList<Integer> grays = new ArrayList<Integer>();

		int count = 0;

		// negative->positive index for looping through kernel and image
		int offset = k / 2;

		// loop through kernel and perform convolution
		for (int i = -offset; i < offset; i++) {
			for (int j = -offset; j < offset; j++) {
				// continue if pixel is outside bounds of image
				if (x + i < 0 || x + i >= image.getWidth() || y + j < 0 || y + j >= image.getHeight())
					continue;

				// grab grayscale value
				int gray = Utility.getGrayscale(image.getRGB(x + i, y + j));

				// store this gray value for later use
				grays.add(gray);

				count++;
			}
		}
		// can't compute if alpha is larger than total array
		if (count-alpha <= 0) return 0;

		// new gray
		int modifiedGray = 0;

		// sort the array to find the median
		Collections.sort(grays);

		// determine edges to chop off
		int half = alpha/2;

		// remove edges
		for (int i = 0 + half; i < count-half; i++) {
        		modifiedGray += grays.get(i);
		}

		// alpha-trimmed calculation
		return Utility.squash(modifiedGray / (count-alpha));
	}

	public static int gaussianSmoothing(BufferedImage image, int x, int y, double[][] kernel) {
		// iteratively added grayscale
		double totalGray = 0;

		// negative->positive index for looping through kernel and image
		int offset = kernel.length / 2;

		// indices for stepping through kernel
		int xIndex = 0;
		int yIndex = 0;

		// loop through kernel and perform convolution
		for (int i = -offset; i < offset; i++) {
			for (int j = -offset; j < offset; j++) {
				// continue if pixel is outside bounds of image
				if (x + j < 0 || x + j >= image.getWidth() || y + i < 0 || y + i >= image.getHeight()) {
					xIndex++;
					continue;
				}
				// grab grayscale value
				int gray = Utility.getGrayscale(image.getRGB(x + j, y + i));

				// multiply each color with the kernel and add to total
				totalGray = totalGray + (gray * kernel[yIndex][xIndex]);

				xIndex++;
			}
			// reset x index and increment y (cycling through kernel)
			xIndex = 0;
			yIndex++;
		}
		// squash color values between 0 - 255
		return Utility.squash(totalGray);
	}

	public static int laplacianSharpening(BufferedImage image, int x, int y, int[][] kernel) {
		// iteratively added grayscale
		int totalGray = 0;

		// negative->positive index for looping through kernel and image
		int offset = kernel.length / 2;

		// kernel indices
		int xIndex = 0;
		int yIndex = 0;

		int originalGray = Utility.getGrayscale(image.getRGB(x, y));

		// loop through kernel and perform convolution
		for (int i = -offset; i < offset; i++) {
			for (int j = -offset; j < offset; j++) {
				// continue if pixel is outside bounds of image
				if (x + j < 0 || x + j >= image.getWidth() || y + i < 0 || y + i >= image.getHeight()) {
					xIndex++;
					continue;
				}

				// grab grayscale value
				int gray = Utility.getGrayscale(image.getRGB(x + j, y + i));

				// multiple gray value by kernel
				int value =  -1 * gray * kernel[yIndex][xIndex];

				// add each color with the negated kernel value
				totalGray = totalGray + value;

				xIndex++;
			}
			xIndex = 0;
			yIndex++;
		}
		// max and min values for scaling
		double min = 255.0 * (kernel[offset][offset] + 1);
		double max = 255.0 * (-1 * kernel[offset][offset]);

		// laplaction calculation
		double scale = (((double) totalGray - min) / (max - min));
		totalGray = (int) (255 * scale);

		return totalGray;
	}

	public static int removeBitPlanes(int gray, int[] planes) {
		//grab binary form of integer
		String grayBinary = Integer.toBinaryString(gray);

		//create string builder for new modified gray string
		StringBuilder modifiedGrayBinary = new StringBuilder(grayBinary);

		//pad the string
		while (modifiedGrayBinary.length() < 8) {
			modifiedGrayBinary.insert(0, "0");
		}

		//modify bit planes m-n
		for (int i = 0; i < planes.length; i++) {
			modifiedGrayBinary.setCharAt(modifiedGrayBinary.length()-1-planes[i], '0');
		}

		// convert string to integer
		int modifiedGray = Integer.parseInt(modifiedGrayBinary.toString(), 2);
		return modifiedGray;
	}
}
