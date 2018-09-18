package jaytracer.materials;

import cgtools.Random;
import cgtools.Vec3;
import jaytracer.Hit;
import jaytracer.Ray;
import jaytracer.textures.TextureIntf;

import static cgtools.Vec3.*;

public class Reflective implements MaterialIntf {

    private final TextureIntf texture;
    private double factor = 0;

    public Reflective(TextureIntf texture, double factor) {
        this.texture = texture;
        this.factor = factor / 10.0;
    }

    @Override
    public Vec3 emittedRadiance(Ray r, Hit h) {
        return zero;
    }

    @Override
    public Ray scatteredRay(Ray r, Hit h) {
        Vec3 dir = subtract(r.dir, multiply(2 * dotProduct(h.getNormal(), r.dir), h.getNormal()));
        if (factor != 0) {
            // the higher the diffusion factor, the less the limiter will interfere
            double limiter = factor * 1000.0;
            // use limiter to stop calculating fractDirs after a reasonable time
            while (limiter >= 0) {
                Vec3 random = new Vec3(Random.random() * 2.0 - 1.0, Random.random() * 2.0 - 1.0, Random.random() * 2.0 - 1.0);
                Vec3 fractDir = add(dir, multiply(factor, random));
                // check if Vec3 does not collide with its object & check for needed acute angle
                if (length(random) <= 1 && dotProduct(fractDir, h.getNormal()) > 0) {
                    // then fractDir is usable as dir
                    dir = fractDir;
                    break;
                }
                limiter--;
            }
            // when limiter is down to zero, cheat and use last correct fractDir instead
        }
        return new Ray(h.getPos(), dir, 0.0001, Double.POSITIVE_INFINITY);
    }

    @Override
    public Vec3 albedo(Ray r, Hit h) {
        return texture.getColor(h.getUv());
    }
}
