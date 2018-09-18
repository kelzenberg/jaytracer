package jaytracer.materials;

import cgtools.Random;
import cgtools.Vec3;
import jaytracer.Hit;
import jaytracer.Ray;
import jaytracer.textures.TextureIntf;

import static cgtools.Vec3.*;

public class Diffuse implements MaterialIntf {

    private final TextureIntf texture;

    public Diffuse(TextureIntf texture) {
        this.texture = texture;
    }

    @Override
    public Vec3 emittedRadiance(Ray r, Hit h) {
        return zero;
    }

    @Override
    public Ray scatteredRay(Ray r, Hit h) {
        Vec3 random;
        // |Random| needs to <= 1 otherwise it will collide with the object ( |h.getNormal()| is 1 )
        do {
            random = new Vec3(Random.random() * 2.0 - 1.0, Random.random() * 2.0 - 1.0, Random.random() * 2.0 - 1.0);
        } while (length(random) > 1);
        // 0.0001 as Epsilon
        return new Ray(h.getPos(), normalize(add(h.getNormal(), random)), 0.0001, Double.POSITIVE_INFINITY);
    }

    @Override
    public Vec3 albedo(Ray r, Hit h) {
        return texture.getColor(h.getUv());
    }
}
