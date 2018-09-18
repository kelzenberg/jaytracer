package jaytracer.shapes;

import cgtools.Vec3;
import jaytracer.BoundingBox;
import jaytracer.Hit;
import jaytracer.Ray;
import jaytracer.materials.MaterialIntf;

import static cgtools.Vec3.*;

public class Cylinder implements ShapeIntf {

    private Vec3 origin;
    private double radius;
    private double height;
    private MaterialIntf material;

    public Cylinder(Vec3 origin, double radius, double height, MaterialIntf material) {
        this.origin = origin;
        this.radius = radius;
        this.height = height;
        this.material = material;
    }

    @Override
    public Hit intersect(Ray ray) {

        // project all vectors to 2D by removing Y components
        Vec3 originR2D = vec3(ray.origin.x, 0, ray.origin.z);
        Vec3 dirR2D = vec3(ray.dir.x, 0, ray.dir.z);
        Vec3 origin2D = vec3(origin.x, 0, origin.z);

        Vec3 shift = subtract(originR2D, origin2D);
        double a = dotProduct(dirR2D, dirR2D);
        double b = 2 * dotProduct(shift, dirR2D);
        double c = dotProduct(shift, shift) - Math.pow(radius, 2);
        double disk = Math.pow(b, 2) - 4 * a * c;

        if (disk >= 0) {
            // 1. hit (comes before t2)
            double t1 = (-b - Math.sqrt(disk)) / (2 * a);
            // 2. hit (comes after t1)
            double t2 = (-b + Math.sqrt(disk)) / (2 * a);

            Vec3 p1 = ray.pointAt(t1);
            Vec3 p2 = ray.pointAt(t2);
            // TODO: add 'shift' instead of ray.origin...
            Vec3 circle = vec3(origin.x, origin.y + height, origin.z);
            double yDiffP2toHead = circle.y - p2.y;
            double yDiffRtoP1 = ray.origin.y - p1.y;
            double yDiffRtoP2 = ray.origin.y - p2.y;
            double tH = ((-(t2 - t1) * yDiffP2toHead) / (yDiffRtoP2 - yDiffRtoP1)) + t2;
            Vec3 pH = ray.pointAt(tH);

            // TODO: maybe remove origin ?
            if (ray.contains(t1) && p1.y >= origin.y && p1.y <= height + origin.y) {
                if (ray.contains(tH)) {
                    Vec3 norm = divide(subtract(pH, origin), radius);
                    // TODO: calculate UV
                    return new Hit(tH, pH, norm, vec3(0, 0, 0), material);
                }
                Vec3 norm = divide(subtract(p1, origin2D), radius);
                // TODO: calculate UV
                return new Hit(t1, p1, multiply(norm, vec3(1, 0, 1)), vec3(0, 0, 0), material);
            }
            if (ray.contains(t2) && p2.y >= origin.y && p2.y <= height + origin.y) {
                if (ray.contains(tH)) {
                    Vec3 norm = divide(subtract(pH, origin), radius);
                    // TODO: calculate UV
                    return new Hit(tH, pH, norm, vec3(0, 0, 0), material);
                }
                Vec3 norm = divide(subtract(p2, origin2D), radius);
                // TODO: calculate UV
                return new Hit(t2, p2, multiply(norm, vec3(1, 0, 1)), vec3(0, 0, 0), material);
            }
            // Special case if you see through the cylinder and p1/p2 are not within cylinder height
            if (ray.contains(t1) && ray.contains(t2) && ray.contains(tH)) {
                if ((p1.y >= height + origin.y && p2.y <= origin.y) || (p2.y >= height + origin.y && p1.y <= origin.y)) {
                    Vec3 norm = divide(subtract(pH, origin), radius);
                    // TODO: calculate UV
                    return new Hit(tH, pH, norm, vec3(0, 0, 0), material);
                }
            }
        }
        return null;
    }

    @Override
    public BoundingBox bounds() {
        Vec3 min = vec3(origin.x - radius, origin.y, origin.z + radius);
        Vec3 max = vec3(origin.x + radius, origin.y + height, origin.z - radius);
        return new BoundingBox(min, max);
    }
}
