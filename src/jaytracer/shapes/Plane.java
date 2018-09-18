package jaytracer.shapes;

import cgtools.Vec3;
import jaytracer.BoundingBox;
import jaytracer.Hit;
import jaytracer.Ray;
import jaytracer.materials.MaterialIntf;

import static cgtools.Vec3.*;

public class Plane implements ShapeIntf {

    private Vec3 origin;
    private Vec3 normal;
    private Vec3 size;
    private MaterialIntf material;
    private BoundingBox box;

    public Plane(Vec3 origin, Vec3 size, MaterialIntf material) {
        // center of plane is origin (0,0)
        this.origin = origin;
        this.normal = vec3(0, 1, 0);
        // size is defined by vec3(x, ignored, z)
        this.size = size;
        this.material = material;
    }

    @Override
    public Hit intersect(Ray ray) {
        // TODO: test if Bbox is faster with or without on Shapes
        double t = dotProduct(subtract(origin, ray.origin), normal) / dotProduct(ray.dir, normal);
        if (ray.contains(t)) {
            Vec3 pos = ray.pointAt(t);
            if (Math.abs(pos.x - origin.x) <= size.x / 2 && Math.abs(pos.z - origin.z) <= size.z / 2) {
                // calculating texture coordinates in new Vec3
                return new Hit(t, pos, normal, new Vec3(pos.x / size.x + 0.5, pos.z / size.z + 0.5, 0), this.material);
            }
        }
        return null;
    }

    @Override
    public BoundingBox bounds() {
        Vec3 min = vec3(origin.x - (size.x / 2), 0, origin.z + (size.z / 2));
        Vec3 max = vec3(origin.x + (size.x / 2), 0, origin.z - (size.z / 2));
        return new BoundingBox(min, max);
    }
}