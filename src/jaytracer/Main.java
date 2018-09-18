package jaytracer;

import cgtools.StopWatch;
import jaytracer.scenes.MirrorRoom;
import jaytracer.scenes.Scene;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {

    private static int width = 4961 / 4; //820 //3840 //7000
    private static int height = 3508 / 4; //512 //2150 //3500
    private static int rate = 10;
    private static int depth = 100;
    private static double gamma = 2.2;
    private static Scene scene;

    public static void main(String[] args) {
        setParameters();
        // choose a scene here
        scene = new MirrorRoom(width, height);
        String filename = "./doc/rendered.png";
        try {
            // start the raytrace and write the result to the file
            raytrace().write(filename);
            System.out.println("\n  Wrote image to '" + filename + "'");
            // make some noise when finished raytracing
            Toolkit.getDefaultToolkit().beep();
        } catch (IOException error) {
            System.out.println(String.format("\n  Something went wrong writing: %s: %s", filename, error));
        }
    }

    private static Image raytrace() {
        Image image = new Image(width, height);
        StopWatch watch = new StopWatch();
        ThreadPoolExecutor exe = (ThreadPoolExecutor) Executors.newFixedThreadPool(11);

        // start Stopwatch
        watch.start();
        System.out.println("  Start time: " + watch.getStartTime());

        // create Thread for each pixel
        for (int x = 0; x != width; x++) {
            for (int y = 0; y != height; y++) {
                Runnable worker = new PixelWorker(image, scene, rate, depth, gamma, x, y);
                exe.execute(worker);
            }
        }
        exe.shutdown();
        long lastSavedPixelCount = -1;

        // wait until every thread has finished its work
        while (!exe.isTerminated()) {
            // one completed Task is equal to one raytraced Pixel
            long pixelCount = exe.getCompletedTaskCount();
            // calculate Percentage for Output
            double percent = Math.round((double) pixelCount / (double) (width * height) * 10000.0) / 100.0;
            System.out.print("\r  Raytracing is at " + percent + "% | " + pixelCount + " Pixels raytraced | Last Preview at " + lastSavedPixelCount + " Pixels");


            // save preview Image every x Pixel
            if (pixelCount > lastSavedPixelCount + 100000) {
                lastSavedPixelCount = pixelCount;
                try {
                    image.write("./doc/preview.png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // NOPe
            }
        }

        // stop StopWatch
        watch.stop();
        System.out.println("\n  End time: " + watch.getEndTime());

        System.out.print("\r  Raytracing finished after " + watch.getElapsedTime().toMillis()
                + "ms (that's " + watch.getElapsedTime().toMillis() / 1000 + "s or ~"
                + watch.getElapsedTime().toMillis() / 1000 / 60 + "min ;D)");

        return image;
    }

    private static void setParameters() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Boolean set = false;
        while (!set) {
            //System.out.println("\n" + scene.toString() + "(Name of Scene)");
            System.out.println("\n  Type\n[ Width Height SupersamplingRate RecursionDepth ]\n  or just press Enter.");
            String input = "";
            try {
                input = reader.readLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            if (!input.trim().isEmpty() && input.split(" ").length <= 4) {
                String[] param = input.split(" ");
                try {
                    width = Integer.parseInt(param[0].trim());
                    height = Integer.parseInt(param[1].trim());
                    rate = Integer.parseInt(param[2].trim());
                    depth = Integer.parseInt(param[3].trim());
                    System.out.println("\n  Parameters set:");
                } catch (NumberFormatException e) {
                    System.out.println("\nError: " + e.getMessage());
                }
            } else {
                System.out.println("  Standard Settings applied:");
            }
            System.out.printf("[ Resolution is %d x %d (W/H) with a Rate @%d and Depth @%d ]\n",
                    width, height, rate, depth);
            while (true) {
                System.out.println("\n  Start Raytracing with these settings? [y/n]");
                try {
                    input = reader.readLine();
                } catch (IOException e) {
                    e.getMessage();
                }
                if (input.trim().equals("y") || input.trim().isEmpty()) {
                    set = true;
                    break;
                }
                if (input.trim().equals("n")) {
                    set = false;
                    break;
                }
            }
        }
    }
}
