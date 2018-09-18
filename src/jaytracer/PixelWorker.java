package jaytracer;

import cgtools.Random;
import cgtools.Vec3;
import jaytracer.lights.LightIntf;
import jaytracer.materials.Diffuse;
import jaytracer.scenes.Scene;
import jaytracer.shapes.Group;

import java.util.ArrayList;

import static cgtools.Vec3.*;

public class PixelWorker implements Runnable {

    private int rate;
    private PinholeCamera cam;
    private Group group;
    private ArrayList<LightIntf> lights;
    private int depth;
    private Image image;
    private int x;
    private int y;
    private double gamma;

    PixelWorker(Image image, Scene scene, int rate, int depth, double gamma, int x, int y) {
        this.rate = rate;
        cam = scene.getCam();
        group = scene.getGroup();
        lights = scene.getLights();
        this.depth = depth;
        this.image = image;
        this.x = x;
        this.y = y;
        this.gamma = gamma;
    }

    @Override
    public void run() {
        Vec3 ratio = new Vec3(0, 0, 0);
        int sqRate = (int) Math.sqrt(rate);

        for (int row = 0; row < sqRate; row++) {
            for (int col = 0; col < sqRate; col++) {

                double rowDot = x + ((Random.random() + row) / sqRate);
                double colDot = y + ((Random.random() + col) / sqRate);
                try {
                    Vec3 radiance = calculateRadiance(cam.createRay(rowDot, colDot), depth);

                    ratio = add(ratio, divide(radiance, (sqRate * sqRate)));
                } catch (Exception e) {
                    System.out.println(e.toString());
                    System.exit(1);
                }
            }
        }
        image.setPixel(x, y, ratio, gamma);
    }

    private Vec3 calculateRadiance(Ray ray, int depth) {
        if (depth == 0) return black;

        // calculation where ray hits scene objects
        Hit hit = group.intersect(ray);

        // calculation of albedo for this hit point
        Vec3 albedo = hit.getMaterial().albedo(ray, hit);

        // calculation of emitted radiance for this hit point
        Vec3 emitted = hit.getMaterial().emittedRadiance(ray, hit);

        // calculation of reflected/scattered Ray for this hit point
        Ray scattered = hit.getMaterial().scatteredRay(ray, hit);

        Vec3 brightness = zero;
        if (lights == null) {
            System.out.println("Found No Lights!!!");
        }
        // check if Material is Diffuse (otherwise things break) & t hits background
        if (hit.getMaterial() instanceof Diffuse && hit.getT() != Double.POSITIVE_INFINITY && lights != null) {
            // for every hit check all light sources
            for (LightIntf light : lights) {
                // if sample point exists
                LightIntf.Sample sample = light.pointHitsLight(group, hit.getPos());
                if (sample == null) {
                    continue;
                }
                double angle = dotProduct(sample.dir, hit.getNormal());
                if (angle <= 0) {
                    continue;
                }
                brightness = multiply(angle, sample.emis);
            }
        }

        if (scattered != null) {
            // calculate radiance when lit by light source (or not) with recursive calculation of radiance
            Vec3 litRadiance = add(calculateRadiance(scattered, --depth), brightness);
            return add(emitted, multiply(albedo, litRadiance));
        } else {
            // else: is a light source by its own
            return emitted;
        }
    }
}