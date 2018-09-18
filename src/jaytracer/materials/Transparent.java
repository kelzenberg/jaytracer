package jaytracer.materials;

import cgtools.Random;
import cgtools.Vec3;
import jaytracer.Hit;
import jaytracer.Ray;
import jaytracer.textures.TextureIntf;

import static cgtools.Vec3.*;

public class Transparent implements MaterialIntf {

    private final TextureIntf texture;
    private final double origN1 = 1; // refraction index of air
    private final double origN2; // refraction index of input

    public Transparent(TextureIntf texture, double n2) {
        this.texture = texture;
        origN2 = n2;
    }

    @Override
    public Vec3 emittedRadiance(Ray r, Hit h) {
        return zero;
    }

    @Override
    public Ray scatteredRay(Ray r, Hit h) {
        double n1 = origN1;
        double n2 = origN2;
        Ray ray;
        Vec3 n = h.getNormal();

        // If Ray origin is from INSIDE
        if (dotProduct(n, r.dir) > 0) {
            // Negate normal vector
            n = multiply(-1, n);
            // Swap refractive indices
            double temp = n1;
            n1 = n2;
            n2 = temp;
        }

        //Snell's Law: change in dir of a beam as it passes through another medium.
        double c = dotProduct(multiply(-1, n), r.dir);
        double ratio = n1 / n2;
        double disk = 1 - Math.pow(ratio, 2) * (1 - Math.pow(c, 2));

        // Schlick Approximation: Ratio of reflection to transmisison on the surface.
        // Equivalent to Fresnel reflection & transmission, but shorter.
        double r0 = Math.pow(((n1 - n2) / (n1 + n2)), 2);
        double schlick = r0 + (1 - r0) * Math.pow(1 + dotProduct(n, r.dir), 5);

        if (disk >= 0 && Random.random() > schlick) {
            // Direction of scattered Ray after refraction
            Vec3 scatDir = add(multiply(ratio, r.dir), multiply((ratio * c - Math.sqrt(disk)), n));
            ray = new Ray(h.getPos(), scatDir, 0.0001, Double.POSITIVE_INFINITY);
        } else {
            // Total reflexion
            Vec3 d = subtract(r.dir, multiply(2 * dotProduct(n, r.dir), n));
            ray = new Ray(h.getPos(), d, 0.0001, Double.POSITIVE_INFINITY);
        }
        return ray;
    }

    @Override
    public Vec3 albedo(Ray r, Hit h) {
        return texture.getColor(h.getUv());
    }
}
