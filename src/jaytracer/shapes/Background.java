package jaytracer.shapes;

import jaytracer.BoundingBox;
import jaytracer.Hit;
import jaytracer.Ray;
import jaytracer.materials.MaterialIntf;

import static cgtools.Vec3.multiply;
import static cgtools.Vec3.vec3;

public class Background implements ShapeIntf {

    private MaterialIntf material;

    public Background(MaterialIntf material) {
        this.material = material;
    }

    @Override
    public Hit intersect(Ray ray) {
        // calculate texture coordinates
        double inclination = Math.acos(ray.dir.y);
        double azimuth = Math.PI + Math.atan2(ray.dir.x, ray.dir.z);
        double u = azimuth / (2 * Math.PI);
        double v = inclination / Math.PI;

        return new Hit(Double.POSITIVE_INFINITY, ray.pointAt(Double.POSITIVE_INFINITY), multiply(-1, ray.dir), vec3(u, v, 0), this.material);
    }

    @Override
    public BoundingBox bounds() {
        // infinite
        return new BoundingBox(vec3(Double.NEGATIVE_INFINITY), vec3(Double.POSITIVE_INFINITY));
    }
}