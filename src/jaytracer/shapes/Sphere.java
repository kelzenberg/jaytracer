package jaytracer.shapes;

import cgtools.Vec3;
import jaytracer.BoundingBox;
import jaytracer.Hit;
import jaytracer.Ray;
import jaytracer.materials.MaterialIntf;

import static cgtools.Vec3.*;

public class Sphere implements ShapeIntf {

    private final Vec3 origin;
    private final double radius;
    private MaterialIntf material;
    private BoundingBox box;

    public Sphere(Vec3 origin, double radius, MaterialIntf material) {
        this.origin = origin;
        this.radius = radius;
        this.material = material;

        Vec3 min = vec3(origin.x - radius, origin.y - radius, origin.z - radius);
        Vec3 max = vec3(origin.x + radius, origin.y + radius, origin.z + radius);
        box = new BoundingBox(min, max);
    }

    @Override
    public Hit intersect(Ray ray) {
        Vec3 shift = subtract(ray.origin, origin);
        double a = dotProduct(ray.dir, ray.dir);
        double b = 2 * dotProduct(shift, ray.dir);
        double c = dotProduct(shift, shift) - Math.pow(radius, 2);
        double disk = Math.pow(b, 2) - 4 * a * c;

        if (disk >= 0) {
            /*
            // 1. hit (comes before t2)
            double t1 = (-b - Math.sqrt(disk)) / (2 * a);
            // 2. hit (comes after t1)
            double t2 = (-b + Math.sqrt(disk)) / (2 * a);
            */

            // this is numerical more stable for calculations with finite accuracy
            double t1 = (-b - Math.signum(b) * Math.sqrt(disk)) / (2 * a);
            double t2 = c / (a * t1);
            if (t1 > t2) {
                double tmp = t1;
                t1 = t2;
                t2 = tmp;
            }

            // check if t0 & t1 is on Ray
            if (ray.contains(t1)) {
                Vec3 pos = ray.pointAt(t1);
                // norm is pointing to center of sphere
                Vec3 norm = divide(subtract(pos, origin), radius);
                // calculate texture coordinates
                double inclination = Math.acos(norm.y);
                double azimuth = Math.PI + Math.atan2(norm.x, norm.z);
                double u = azimuth / (2 * Math.PI);
                double v = inclination / Math.PI;

                return new Hit(t1, pos, norm, vec3(u, v, 0), material);
            }
            if (ray.contains(t2)) {
                Vec3 pos = ray.pointAt(t2);
                // norm is pointing to center of sphere
                Vec3 norm = divide(subtract(pos, origin), radius);
                // calculate texture coordinates
                double inclination = Math.acos(norm.y);
                double azimuth = Math.PI + Math.atan2(norm.x, norm.z);
                double u = azimuth / (2 * Math.PI);
                double v = inclination / Math.PI;

                return new Hit(t2, pos, norm, vec3(u, v, 0), material);
            }
        }
        return null;
    }

    @Override
    public BoundingBox bounds() {
        /*
        Vec3 min = translate(new Vec3(-radius)).transformPoint(origin);
        Vec3 max = translate(new Vec3(radius)).transformPoint(origin);
        return new BoundingBox(min, max);
        */
        return box;
    }
}