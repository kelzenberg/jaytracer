package jaytracer.lights;

import cgtools.Vec3;
import jaytracer.Hit;
import jaytracer.Ray;
import jaytracer.shapes.Group;

import static cgtools.Vec3.multiply;

public class DirectionalLight implements LightIntf {

    Vec3 dir;
    Vec3 emis;

    public DirectionalLight(Vec3 dir, Vec3 emis) {
        this.dir = dir;
        this.emis = emis;
    }

    @Override
    public Sample pointHitsLight(Group group, Vec3 fromPoint) {
        Vec3 dirInv = multiply(-1, dir);
        // send a Ray to Light from Points on all Objects
        Hit hit = group.intersect(new Ray(fromPoint, dirInv, 0.0001, Double.POSITIVE_INFINITY));
        // and check if Ray hits Background (aka Lightsource)
        if (hit.getT() == Double.POSITIVE_INFINITY) {
            return new Sample(dirInv, emis);
        }
        return null;
    }
}
