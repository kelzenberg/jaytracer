package jaytracer;

import cgtools.Vec3;
import jaytracer.materials.MaterialIntf;

public class Hit {

    private final double t;
    private final Vec3 pos;
    private final Vec3 normal;
    private final Vec3 uv;
    private final MaterialIntf material;

    public Hit(double t, Vec3 pos, Vec3 normal, Vec3 uv, MaterialIntf material) {
        // parameter of ray with which the ray hits
        this.t = t;
        // position of hit point
        this.pos = pos;
        // normal vector on hit position
        this.normal = normal;
        // texel coordinate on hit position
        this.uv = uv;
        // material on hit position
        this.material = material;
    }

    public double getT() {
        return t;
    }

    public Vec3 getPos() {
        return pos;
    }

    public Vec3 getNormal() {
        return normal;
    }

    public Vec3 getUv() {
        return uv;
    }

    public MaterialIntf getMaterial() {
        return material;
    }
}