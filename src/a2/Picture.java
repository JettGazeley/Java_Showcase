package a2;

/******************************************************************************
 *  Compilation:  javac Picture.java
 *  Execution:    java Picture imagename
 *  Dependencies: none
 *
 *  Data type for manipulating individual pixels of an image. The original
 *  image can be read from a file in JPG, GIF, or PNG format, or the
 *  user can create a blank image of a given dimension. Includes methods for
 *  displaying the image in a window on the screen or saving to a file.
 *
 *  % java Picture mandrill.jpg
 *
 *  Remarks
 *  -------
 *   - pixel (x, y) is column x and row y, where (0, 0) is upper left
 *
 ******************************************************************************/

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * THIS CLASS HAS BEEN MODIFIED FROM THE ORIGINAL. The modified version
 * allows the programmer to read an image from a file, create a new blank
 * image, get the height and width of the image, a get and set the {@code Color} 
 * of an image pixel.
 * 
 * <p>
 * This class provides methods for manipulating individual pixels of an image
 * using the RGB color format. The alpha component (for transparency) is not
 * currently supported. The original image can be read from a {@code PNG},
 * {@code GIF}, or {@code JPEG} file or the user can create a blank image of a
 * given dimension. This class includes methods for displaying the image in a
 * window on the screen or saving it to a file.
 * <p>
 * Pixel (<em>col</em>, <em>row</em>) is column <em>col</em> and row
 * <em>row</em>. By default, the origin (0, 0) is the pixel in the top-left
 * corner, which is a common convention in image processing. The method
 * {@link #setOriginLowerLeft()} change the origin to the lower left.
 * <p>
 * The {@code get()} and {@code set()} methods use {@link Color} objects to get
 * or set the color of the specified pixel. The {@code getRGB()} and
 * {@code setRGB()} methods use a 32-bit {@code int} to encode the color,
 * thereby avoiding the need to create temporary {@code Color} objects. The red
 * (R), green (G), and blue (B) components are encoded using the least
 * significant 24 bits. Given a 32-bit {@code int} encoding the color, the
 * following code extracts the RGB components: <blockquote>
 * 
 * <pre>
 * int r = (rgb &gt;&gt; 16) &amp; 0xFF;
 * int g = (rgb &gt;&gt; 8) &amp; 0xFF;
 * int b = (rgb &gt;&gt; 0) &amp; 0xFF;
 * </pre>
 * 
 * </blockquote> Given the RGB components (8-bits each) of a color, the
 * following statement packs it into a 32-bit {@code int}: <blockquote>
 * 
 * <pre>
 * int rgb = (r &lt;&lt; 16) + (g &lt;&lt; 8) + (b &lt;&lt; 0);
 * </pre>
 * 
 * </blockquote>
 * <p>
 * A <em>W</em>-by-<em>H</em> picture uses ~ 4 <em>W H</em> bytes of memory,
 * since the color of each pixel is encoded as a 32-bit <code>int</code>.
 * <p>
 * For additional documentation, see
 * <a href="https://introcs.cs.princeton.edu/31datatype">Section 3.1</a> of
 * <i>Computer Science: An Interdisciplinary Approach</i> by Robert Sedgewick
 * and Kevin Wayne. See {@link GrayscalePicture} for a version that supports
 * grayscale images.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public final class Picture {
	private BufferedImage image; // the rasterized image
	private boolean isOriginUpperLeft = true; // location of origin
	private final int width, height; // width and height

	/**
	 * Creates a {@code width}-by-{@code height} picture, with {@code width} columns
	 * and {@code height} rows, where each pixel is black.
	 *
	 * @param width  the width of the picture
	 * @param height the height of the picture
	 * @throws IllegalArgumentException if {@code width} is negative or zero
	 * @throws IllegalArgumentException if {@code height} is negative or zero
	 */
	public Picture(int width, int height) {
		if (width <= 0)
			throw new IllegalArgumentException("width must be positive");
		if (height <= 0)
			throw new IllegalArgumentException("height must be positive");
		this.width = width;
		this.height = height;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// set to TYPE_INT_ARGB here and in next constructor to support transparency
	}


	/**
	 * Creates a picture by reading the image from a PNG, GIF, or JPEG file.
	 *
	 * @param file the file
	 * @throws IllegalArgumentException if cannot read image
	 * @throws IllegalArgumentException if {@code file} is {@code null}
	 */
	public Picture(File file) {
		if (file == null)
			throw new IllegalArgumentException("constructor argument is null");

		try {
			image = ImageIO.read(file);
		} catch (IOException ioe) {
			throw new IllegalArgumentException("could not open file: " + file, ioe);
		}
		if (image == null) {
			throw new IllegalArgumentException("could not read file: " + file);
		}
		width = image.getWidth(null);
		height = image.getHeight(null);
	}

	/**
	 * Returns a {@link JLabel} containing this picture, for embedding in a
	 * {@link JPanel}, {@link JFrame} or other GUI widget.
	 *
	 * @return the {@code JLabel}
	 */
	public JLabel getJLabel() {
		if (image == null)
			return null; // no image available
		ImageIcon icon = new ImageIcon(image);
		return new JLabel(icon);
	}


	/**
	 * Returns the height of the picture.
	 *
	 * @return the height of the picture (in pixels)
	 */
	public int height() {
		return height;
	}

	/**
	 * Returns the width of the picture.
	 *
	 * @return the width of the picture (in pixels)
	 */
	public int width() {
		return width;
	}

	private void validateRowIndex(int row) {
		if (row < 0 || row >= height())
			throw new IllegalArgumentException("row index must be between 0 and " + (height() - 1) + ": " + row);
	}

	private void validateColumnIndex(int col) {
		if (col < 0 || col >= width())
			throw new IllegalArgumentException("column index must be between 0 and " + (width() - 1) + ": " + col);
	}

	/**
	 * Returns the color of pixel ({@code col}, {@code row}) as a
	 * {@link java.awt.Color}.
	 *
	 * @param col the column index
	 * @param row the row index
	 * @return the color of pixel ({@code col}, {@code row})
	 * @throws IllegalArgumentException unless both {@code 0 <= col < width} and
	 *                                  {@code 0 <= row < height}
	 */
	public Color get(int col, int row) {
		validateColumnIndex(col);
		validateRowIndex(row);
		int rgb = getRGB(col, row);
		return new Color(rgb);
	}

	/**
	 * Returns the color of pixel ({@code col}, {@code row}) as an {@code int}.
	 * Using this method can be more efficient than {@link #get(int, int)} because
	 * it does not create a {@code Color} object.
	 *
	 * @param col the column index
	 * @param row the row index
	 * @return the integer representation of the color of pixel ({@code col},
	 *         {@code row})
	 * @throws IllegalArgumentException unless both {@code 0 <= col < width} and
	 *                                  {@code 0 <= row < height}
	 */
	private int getRGB(int col, int row) {
		validateColumnIndex(col);
		validateRowIndex(row);
		if (isOriginUpperLeft)
			return image.getRGB(col, row);
		else
			return image.getRGB(col, height - row - 1);
	}

	/**
	 * Sets the color of pixel ({@code col}, {@code row}) to given color.
	 *
	 * @param col   the column index
	 * @param row   the row index
	 * @param color the color
	 * @throws IllegalArgumentException unless both {@code 0 <= col < width} and
	 *                                  {@code 0 <= row < height}
	 * @throws IllegalArgumentException if {@code color} is {@code null}
	 */
	public void set(int col, int row, Color color) {
		validateColumnIndex(col);
		validateRowIndex(row);
		if (color == null)
			throw new IllegalArgumentException("color argument is null");
		int rgb = color.getRGB();
		setRGB(col, row, rgb);
	}

	/**
	 * Sets the color of pixel ({@code col}, {@code row}) to given color.
	 *
	 * @param col the column index
	 * @param row the row index
	 * @param rgb the integer representation of the color
	 * @throws IllegalArgumentException unless both {@code 0 <= col < width} and
	 *                                  {@code 0 <= row < height}
	 */
	private void setRGB(int col, int row, int rgb) {
		validateColumnIndex(col);
		validateRowIndex(row);
		if (isOriginUpperLeft)
			image.setRGB(col, row, rgb);
		else
			image.setRGB(col, height - row - 1, rgb);
	}

}
