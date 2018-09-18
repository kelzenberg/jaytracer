package jaytracer;

import cgtools.Vec3;

import static cgtools.Vec3.*;

public class Ray {

    public final Vec3 origin;
    public final Vec3 dir;
    public final double t0;
    public final double t1;

    public Ray(Vec3 origin, Vec3 dir, double t0, double t1) {
        this.origin = origin;
        this.dir = normalize(dir);
        this.t0 = t0;
        this.t1 = t1;
    }

    public Vec3 pointAt(double t) {
        return add(origin, multiply(t, dir));
    }

    public boolean contains(double t) {
        return t0 < t && t <= t1;
    }
}
