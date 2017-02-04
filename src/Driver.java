import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Driver {
	public static void main(String[] args) throws IOException {
		// list of operations that will be performed
		ArrayList<String> operations = new ArrayList<String>(Arrays.asList("resolution", "log", "power", "histogram",
				"arithmetic", "geometric", "harmonic", "contraharmonic", "max", "min", "midpoint", "median", "alpha",
				"gaussian", "laplace", "planes", "RLE", "LZW", "DPCM", "Huffman"));

		// open the original image
		BufferedImage originalImage = ImageIO.read(new File("input/test.png"));

		// create new image with identical properties
		BufferedImage modifiedImage = Utility.createBufferedImage(originalImage);

		// kernel size
		int kernel = 3;

		// bit planes to remove
		int[] planes = {0, 1, 2, 3, 4, 5};

		// omega value for gaussian
		double omega = 1.0;

		// gray level reduction bits
		int bitResolution = 2;

		// log factor
		double logFactor = 1.0;

		// gamma value for powerTransformation
		double gamma = 0.2;

		// alpha value smaller than kernel for trimming
		int alpha = 3;

		// grab lookup table from the original image for histogram EQ
		int[] lookup = ImageProcessingFunctions1.histogramEqualization(originalImage);

		// create gaussian kernel based on kernel size
		double[][] gaussian = Utility.getGaussianKernel(kernel, omega);

		// create laplacian kernel based on kernel size
		int[][] laplacian = Utility.getLaplacianKernel(kernel);

		// perform each operation
		while (operations.size() != 0) {
			// grab new operation from list
			String operation = operations.remove(0);

			// perform compression tests - RLE - LZW - DPCM - Huffman
			if (operation.equals("RLE")) {
				ImageProcessingFunctions3.RLE(originalImage);
				continue;
			} else if (operation.equals("LZW")) {
				ImageProcessingFunctions3.LZW(originalImage);
				continue;
			} else if (operation.equals("DPCM")) {
				ImageProcessingFunctions3.DPCM(originalImage);
				continue;
			} else if (operation.equals("Huffman")) {
				// unfinished
				//ImageProcessingFunctions3.Huffman(originalImage);
				continue;
			}

			// loop through pixel data
			for (int y = 0; y < originalImage.getHeight(); y++) {
				for (int x = 0; x < originalImage.getWidth(); x++) {
					// grab the grayscale value at this pixel
					int gray = Utility.getGrayscale(originalImage.getRGB(x, y));

					// our new gray value
					int modifiedGray = 0;

					// switch over string to determine the necessary operation
					switch (operation) {
					case "resolution":
						// perform bit level resolution operation
						modifiedGray = ImageProcessingFunctions1.newGrayLevelResolution(gray, bitResolution);
						break;
					case "log":
						// perform log transformation
						modifiedGray = ImageProcessingFunctions1.logTransformation(gray, logFactor);
						break;
					case "power":
						// perform power transformation
						modifiedGray = ImageProcessingFunctions1.powerTransformation(gray, gamma);
						break;
					case "histogram":
						// perform histogram equalization;
						modifiedGray = lookup[gray];
						break;
					case "arithmetic":
						// perform arithmetic mean
						modifiedGray = ImageProcessingFunctions2.arithmeticMean(originalImage, x, y, kernel);
						break;
					case "geometric":
						// perform geometric mean
						modifiedGray = ImageProcessingFunctions2.geometricMean(originalImage, x, y, kernel);
						break;
					case "harmonic":
						// perform harmonic mean
						modifiedGray = ImageProcessingFunctions2.harmonicMean(originalImage, x, y, kernel);
						break;
					case "contraharmonic":
						// perform contraharmonic mean
						modifiedGray = ImageProcessingFunctions2.contraHarmonicMean(originalImage, x, y, kernel);
						break;
					case "max":
						// perform max
						modifiedGray = ImageProcessingFunctions2.max(originalImage, x, y, kernel);
						break;
					case "min":
						// perform min
						modifiedGray = ImageProcessingFunctions2.min(originalImage, x, y, kernel);
						break;
					case "midpoint":
						// perform midpoint
						modifiedGray = ImageProcessingFunctions2.midpoint(originalImage, x, y, kernel);
						break;
					case "median":
						// perform median
						modifiedGray = ImageProcessingFunctions2.median(originalImage, x, y, kernel);
						break;
					case "alpha":
						// perform alpha-trimmed
						modifiedGray = ImageProcessingFunctions2.alphaTrimmed(originalImage, x, y, kernel, alpha);
						break;
					case "gaussian":
						// perform smoothing
						modifiedGray = ImageProcessingFunctions2.gaussianSmoothing(originalImage, x, y, gaussian);
						break;
					case "laplace":
						// perform sharpening
						modifiedGray = ImageProcessingFunctions2.laplacianSharpening(originalImage, x, y, laplacian);
						break;
					case "planes":
						// remove bit planes
						modifiedGray = ImageProcessingFunctions2.removeBitPlanes(gray, planes);
						break;
					}
					// grab new color for modified gray value
					Color modifiedImageColor = new Color(modifiedGray, modifiedGray, modifiedGray);

					// set the modified Image's RGB value at that location
					modifiedImage.setRGB(x, y, modifiedImageColor.getRGB());
				}
			}
			// create modified image file and write the BufferedImage to disk
			File outputFile = new File("output_test/" + operation + ".png");
			ImageIO.write(modifiedImage, "png", outputFile);
		}
	}
}
