package jaytracer.shapes;

import cgtools.Vec3;
import jaytracer.BoundingBox;
import jaytracer.Hit;
import jaytracer.Ray;
import jaytracer.materials.MaterialIntf;

import static cgtools.Vec3.*;

public class Cone implements ShapeIntf {

    private Vec3 origin;
    private double radius;
    private double height;
    private MaterialIntf material;

    public Cone(Vec3 origin, double radius, double height, MaterialIntf material) {
        // tip of the cone is origin
        this.origin = origin;
        this.radius = radius;
        this.height = height;
        this.material = material;
    }

    @Override
    public Hit intersect(Ray ray) {

        Vec3 shift = subtract(ray.origin, origin);
        double s = (radius * radius) / (height * height);
        double a = Math.pow(ray.dir.x, 2) + Math.pow(ray.dir.z, 2) - s * Math.pow(ray.dir.y, 2);
        double b = 2 * (shift.x * ray.dir.x + shift.z * ray.dir.z - s * shift.y * ray.dir.y);
        double c = Math.pow(shift.x, 2) + Math.pow(shift.z, 2) - s * Math.pow(shift.y, 2);
        double disk = Math.pow(b, 2) - 4 * a * c;

        if (disk >= 0) {
            // 1. hit (comes before t2)
            double t1 = (-b - Math.sqrt(disk)) / (2 * a);
            // 2. hit (comes after t1)
            double t2 = (-b + Math.sqrt(disk)) / (2 * a);

            if (ray.contains(t1)) {
                Vec3 pos = ray.pointAt(t1);
                if (pos.y <= origin.y && pos.y >= origin.y - height) {
                    Vec3 norm = divide(subtract(pos, origin), radius);
                    norm = vec3(norm.x, pos.y, norm.z);
                    // TODO: calculate UV
                    return new Hit(t1, pos, norm, vec3(0, 0, 0), material);
                }
            }
            if (ray.contains(t2)) {
                Vec3 pos = ray.pointAt(t2);
                if (pos.y <= origin.y && pos.y >= origin.y - height) {
                    Vec3 norm = divide(subtract(pos, origin), radius);
                    norm = vec3(norm.x, pos.y, norm.z);
                    // TODO: calculate UV
                    return new Hit(t2, pos, norm, vec3(0, 0, 0), material);
                }
            }
        }
        return null;
    }

    @Override
    public BoundingBox bounds() {
        Vec3 min = vec3(origin.x - radius, origin.y - height, origin.z + radius);
        Vec3 max = vec3(origin.x + radius, origin.y, origin.z - radius);
        return new BoundingBox(min, max);
    }
}
