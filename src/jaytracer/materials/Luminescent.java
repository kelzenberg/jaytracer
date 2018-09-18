package jaytracer.materials;

import cgtools.Vec3;
import jaytracer.Hit;
import jaytracer.Ray;
import jaytracer.textures.TextureIntf;

import static cgtools.Vec3.zero;

public class Luminescent implements MaterialIntf {

    private TextureIntf texture;

    public Luminescent(TextureIntf texture) {
        this.texture = texture;
    }

    @Override
    public Vec3 emittedRadiance(Ray r, Hit h) {
        return texture.getColor(h.getUv());
    }

    @Override
    public Ray scatteredRay(Ray r, Hit h) {
        return null;
    }

    @Override
    public Vec3 albedo(Ray r, Hit h) {
        return zero;
    }
}
