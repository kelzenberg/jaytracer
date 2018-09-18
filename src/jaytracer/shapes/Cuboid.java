package jaytracer.shapes;

import cgtools.Vec3;
import jaytracer.BoundingBox;
import jaytracer.Hit;
import jaytracer.Ray;
import jaytracer.materials.MaterialIntf;

import static cgtools.Vec3.vec3;

public class Cuboid implements ShapeIntf {

    private final Vec3 min;
    private final Vec3 max;
    private MaterialIntf material;
    private BoundingBox box;

    public Cuboid(Vec3 min, Vec3 max, MaterialIntf material) {
        // front left bottom corner is min
        this.min = min;
        // back right top corner is max
        this.max = max;
        this.material = material;
        box = new BoundingBox(
        );
    }

    //
    // Adapted from
    // https://tavianator.com/cgit/dimension.git/tree/libdimension/bvh/bvh.c
    //
    @Override
    public Hit intersect(Ray ray) {

        double dix = 1.0 / ray.dir.x;
        double diy = 1.0 / ray.dir.y;
        double diz = 1.0 / ray.dir.z;

        double tx1 = (min.x - ray.origin.x) * dix;
        double tx2 = (max.x - ray.origin.x) * dix;

        double t1 = Math.min(tx1, tx2);
        double t2 = Math.max(tx1, tx2);

        double ty1 = (min.y - ray.origin.y) * diy;
        double ty2 = (max.y - ray.origin.y) * diy;

        t1 = Math.max(t1, Math.min(ty1, ty2));
        t2 = Math.min(t2, Math.max(ty1, ty2));

        double tz1 = (min.z - ray.origin.z) * diz;
        double tz2 = (max.z - ray.origin.z) * diz;

        t1 = Math.max(t1, Math.min(tz1, tz2));
        t2 = Math.min(t2, Math.max(tz1, tz2));

        if (t2 > t1) {
            if (ray.contains(t1)) {
                // hits
                Vec3 pos = ray.pointAt(t1);
                // TODO: calculate UV
                return new Hit(t1, pos, calcNorm(pos), vec3(0, 0, 0), this.material);
            }
            if (ray.contains(t2)) {
                // 2nd hits
                Vec3 pos = ray.pointAt(t2);
                // TODO: calculate UV
                return new Hit(t2, pos, calcNorm(pos), vec3(0, 0, 0), this.material);
            }
        }
        return null;
    }

    private Vec3 calcNorm(Vec3 pos) {
        // front side
        if (Math.abs(pos.z - min.z) <= 0.0001) {
            return vec3(0, 0, 1);
        }
        // right side
        if (Math.abs(pos.x - max.x) <= 0.0001) {
            return vec3(1, 0, 0);
        }
        // back side
        if (Math.abs(pos.z - max.z) <= 0.0001) {
            return vec3(0, 0, -1);
        }
        // left side
        if (Math.abs(pos.x - min.x) <= 0.0001) {
            return vec3(-1, 0, 0);
        }
        // top side
        if (Math.abs(pos.y - max.y) <= 0.0001) {
            return vec3(0, 1, 0);
        }
        // bottom side
        if (Math.abs(pos.y - min.y) <= 0.0001) {
            return vec3(0, -1, 0);
        }
        return null;
    }

    @Override
    public BoundingBox bounds() {
        return new BoundingBox(min, max);
    }
}
