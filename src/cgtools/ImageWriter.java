/* Author: Henrik Tramberend tramberend@beuth-hochschule.de */

package cgtools;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A simple image writer that takes an array of pixel components and the image
 * size and writes the corresponding image in 16-bit PNG format to the provided
 * location.
 *
 * @author henrik
 */
public class ImageWriter {
    private BufferedImage image;
    /**
     * Creates a new writer instance for the provided image data.
     *
     * @param data An array of 3*width*height double precision RGB pixel
     *     components. Component values are clipped to the interval [0.0, 1.0].
     * @param width The width of the array in pixels.
     * @param height The height of the array in pixels.
     */
    public ImageWriter(double[] data, int width, int height) {
        /* Setup an sRGB image with 16-bit components of the right size. */
        ComponentColorModel ccm =
                new ComponentColorModel(
                        ColorSpace.getInstance(ColorSpace.CS_sRGB),
                        false,
                        false,
                        ComponentColorModel.OPAQUE,
                        DataBuffer.TYPE_USHORT);

        WritableRaster raster =
                Raster.createBandedRaster(DataBuffer.TYPE_USHORT, width, height, 3, null);
        image = new BufferedImage(ccm, raster, false, null);

        /* Convert double data to ushort pixels. */
        for (int y = 0; y != height; y++) {
            for (int x = 0; x != width; x++) {
                int i = (width * y + x) * 3;
                int[] rgb = {
                    (int) (clamp(data[i + 0]) * 65535.0),
                    (int) (clamp(data[i + 1]) * 65535.0),
                    (int) (clamp(data[i + 2]) * 65535.0)
                };
                raster.setPixel(x, y, rgb);
            }
        }
    }

    /**
     * Write the PNG image to a file with the provided name.
     *
     * @param filename Name of the image file.
     * @throws IOException If anything goes wrong.
     */
    public void write(String filename) throws IOException {
        File outputfile = new File(filename);
        ImageIO.write(image, "png", outputfile);
    }

    private double clamp(double v) {
        return Math.min(Math.max(0, v), 1);
    }
}
