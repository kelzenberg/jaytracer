package jaytracer;

import cgtools.ImageWriter;
import cgtools.Vec3;

import java.io.IOException;

public class Image {

    private double[] arr;
    private int width;
    private int height;

    public Image(int width, int height) {
        arr = new double[width * height * 3];
        this.width = width;
        this.height = height;
    }

    public void setPixel(int x, int y, Vec3 color) {
        arr[(width * y + x) * 3] = color.x;
        arr[(width * y + x) * 3 + 1] = color.y;
        arr[(width * y + x) * 3 + 2] = color.z;
    }

    public void setPixel(int x, int y, Vec3 color, double gamma) {
        // add gamma correction
        double ix = Math.pow(color.x, (1 / gamma));
        double iy = Math.pow(color.y, (1 / gamma));
        double iz = Math.pow(color.z, (1 / gamma));

        arr[(width * y + x) * 3] = ix;
        arr[(width * y + x) * 3 + 1] = iy;
        arr[(width * y + x) * 3 + 2] = iz;
    }

    public void write(String filename) throws IOException {
        ImageWriter writer = new ImageWriter(arr, width, height);
        writer.write(filename);
    }
}
