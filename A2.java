package a2;

import java.awt.Color;
import java.io.File;
import java.net.URI;
import java.net.URL;

public class A2 {
	/**
	 * The original image
	 */
	private static Picture orig;

	/**
	 * The image viewer class
	 */
	private static A2Viewer viewer;

	/**
	 * Returns a 300x200 image containing the Queen's flag (without the crown).
	 * 
	 * @return an image containing the Queen's flag
	 */
	public static Picture flag() {
		Picture img = new Picture(300, 200);
		int w = img.width();
		int h = img.height();

		// set the pixels in the blue stripe
		Color blue = new Color(0, 48, 95);
		for (int col = 0; col < w / 3; col++) {
			for (int row = 0; row < h - 1; row++) {
				img.set(col, row, blue);
			}
		}

		// set the pixels in the yellow stripe
		Color yellow = new Color(255, 189, 17);
		for (int col = w / 3; col < 2 * w / 3; col++) {
			for (int row = 0; row < h - 1; row++) {
				img.set(col, row, yellow);
			}
		}

		// set the pixels in the red stripe
		Color red = new Color(185, 17, 55);
		for (int col = 2 * w / 3; col < w; col++) {
			for (int row = 0; row < h - 1; row++) {
				img.set(col, row, red);
			}
		}
		return img;
	}

	// ADD YOUR METHODS HERE

	public static Picture copy(Picture p) {
		// complete the method
		Picture result = new Picture(p.width(), p.height());
		for (int col = 0; col < p.width(); col++) {
			for (int row = 0; row < p.height(); row++) {
				Color c = p.get(col, row);
				result.set(col, row, c);
			}
		}

		return result;

	}

	public static Picture border(Picture p, int thickness) {
		Picture first = A2.copy(p);
		Color c = new Color(0, 0, 255);
		for (int col = 0; col < p.width(); col++) {
			for (int row = 0; row < p.height(); row++) {
				if (col <= thickness || col >= 398 - thickness) {
					first.set(col, row, c);
				}
				if (row <= thickness || row >= 299 - thickness) {
					first.set(col, row, c);
				}
			}
		}
		return first;
	}

	public static Picture grayscale(Picture p) {
		Picture result = new Picture(p.width(), p.height());

		for (int col = 0; col < p.width(); col++) {
			for (int row = 0; row < p.height(); row++) {
				Color c = p.get(col, row);
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();
				long y = Math.round(0.2989 * (r) + 0.5870 * (g) + 0.1140 * (b));
				int x = (int) y;
				Color c1 = new Color(x, x, x);
				result.set(col, row, c1);

			}

		}
		return result;
	}

	public static Picture binary(Picture p) {
		Picture first = new Picture(p.width(), p.height());
		Picture second = A2.grayscale(p);
		for (int col = 0; col < p.width(); col++) {
			for (int row = 0; row < p.height(); row++) {
				Color c = second.get(col, row);
				int r = c.getRed();
				if (r < 128) {
					Color c1 = new Color(0, 0, 0);
					first.set(col, row, c1);
				} else {
					Color c2 = new Color(255, 255, 255);
					first.set(col, row, c2);

				}
			}
		}
		return first;
	}

	public static Picture flipVertical(Picture p) {
		Picture first = new Picture(p.width(), p.height());
		for (int col = 0; col < p.width(); col++) {
			for (int row = 0; row < p.height(); row++) {
				int h = p.height();
				int target = h - row - 1;
				Color c = p.get(col, row);
				first.set(col, target, c);
			}
		}
		return first;
	}

	public static Picture rotateClockwise90(Picture p) {
		Picture first = new Picture(p.width(), p.height());
		for (int col = 0; col < p.width(); col++) {
			for (int row = 0; row < p.height(); row++) {
				int w = p.width();
				int target = w - col - 1;
				Color c = p.get(col, row);
				first.set(target, row, c);

			}
		}
		return first;
	}

	public static Picture redEye(Picture P) {
		Picture copy = A2.copy(P);
		for (int col = 0; col < copy.width(); col++) {
			for (int row = 0; row < copy.height(); row++) {
				Color c = copy.get(col, row);
				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();
				if (g < 76 && b < 200 && r > 110) {
					Color c1 = new Color(((g + b) / 2), g, b);
					copy.set(col, row, c1);
				}
			}
		}
		return copy;
	}

	public static Picture blur(Picture p, int radius) {
		Picture result = new Picture(p.width(), p.height());
		for (int col = 0; col < p.width(); col++) {
			for (int row = 0; row < p.height(); row++) {
				int n = 0;
				int m = 0;
				int i = -radius;
				while (i < 0) {
					if (col + i < 0 || row + i < 0) {
						n++;
					} else if (col + i >= p.width() || row + i >= p.height()) {
						m++;
					}
					i++;
				}
				int range = 2 * radius + 1 - n + m;
				int r = 0;
				int g = 0;
				int b = 0;
				int z = 0;
				for (int j = col + i; j < col + range; j++) {
					for (int k = row + i; k < row + range; k++) {
						if (j >= 0 && j <= 398) {
							if (k >= 0 && k <= 299) {
								Color c = p.get(j, k);
								r = r + c.getRed();
								g = g + c.getGreen();
								b = b + c.getBlue();
								z++;

							}
						}
					}
				}
				if (z > 0) {
					r = (int) r / z;
					g = (int) g / z;
					b = (int) b / z;
					Color c1 = new Color(r, g, b);
					result.set(col, row, c1);
				}
			}
		}
		return result;
	}

	/**
	 * A2Viewer class calls this method when a menu item is selected. This method
	 * computes a new image and then asks the viewer to display the computed image.
	 * 
	 * @param op the operation selected in the viewer
	 */
	public static void processImage(String op) {

		switch (op) {
		case A2Viewer.FLAG:
			// create a new image by copying the original image
			Picture p = A2.flag();
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.COPY:
			// create a new image by copying the original image
			p = A2.copy(A2.orig);
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.BORDER_1:
			p = A2.border(A2.orig, 1);
			A2.viewer.setComputed(p);
			// create a new image by adding a border of width 1 to the original image

			break;
		case A2Viewer.BORDER_5:
			p = A2.border(A2.orig, 5);
			A2.viewer.setComputed(p);
			// create a new image by adding a border of width 5 the original image

			break;
		case A2Viewer.BORDER_10:
			p = A2.border(A2.orig, 10);
			A2.viewer.setComputed(p);
			// create a new image by adding a border of width 10 the original image

			break;
		case A2Viewer.TO_GRAY:
			// create a new image by converting the original image to grayscale
			p = A2.grayscale(A2.orig);
			A2.viewer.setComputed(p);
			break;

		case A2Viewer.TO_BINARY:
			// create a new image by converting the original image to black and white
			p = A2.binary(A2.orig);
			A2.viewer.setComputed(p);

			break;
		case A2Viewer.FLIP_VERTICAL:
			// create a new image by flipping the original image vertically
			p = A2.flipVertical(A2.orig);
			A2.viewer.setComputed(p);

			break;
		case A2Viewer.ROTATE_RIGHT:
			// create a new image by rotating the original image to the right by 90 degrees
			p = A2.rotateClockwise90(A2.orig);
			A2.viewer.setComputed(p);

			break;
		case A2Viewer.RED_EYE:
			// create a new image by removing the redeye effect in the original image
			p = A2.redEye(A2.orig);
			A2.viewer.setComputed(p);

			break;
		case A2Viewer.BLUR_1:
			// create a new image by blurring the original image with a box blur of radius 1
			p = A2.blur(A2.orig, 1);
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.BLUR_3:
			// create a new image by blurring the original image with a box blur of radius 3
			p = A2.blur(A2.orig, 3);
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.BLUR_5:
			// create a new image by blurring the original image with a box blur of radius 5
			p = A2.blur(A2.orig, 5);
			A2.viewer.setComputed(p);

			break;
		default:
			// do nothing
		}
	}

	/**
	 * Starting point of the program. Students can comment/uncomment which image to
	 * use when testing their program.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		A2.viewer = new A2Viewer();
		A2.viewer.setVisible(true);

		URL img;
		// uncomment one of the next two lines to choose which test image to use (person
		// or cat)
		img = A2.class.getResource("redeye-400x300.jpg");
		// img = A2.class.getResource("cat.jpg");

		try {
			URI uri = new URI(img.toString());
			A2.orig = new Picture(new File(uri.getPath()));
			A2.viewer.setOriginal(A2.orig);
		} catch (Exception x) {

		}
	}

}
