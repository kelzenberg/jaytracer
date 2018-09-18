package jaytracer.shapes;

import cgtools.Vec3;
import jaytracer.BoundingBox;
import jaytracer.Hit;
import jaytracer.Ray;
import jaytracer.materials.MaterialIntf;

import static cgtools.Vec3.*;

public class Torus implements ShapeIntf {

    private Vec3 origin;
    private double radius;
    private double thickness;
    private MaterialIntf material;

    public Torus(Vec3 origin, double radius, double thickness, MaterialIntf material) {
        // center of torus is origin
        this.origin = origin;
        this.radius = radius;
        this.thickness = thickness;
        this.material = material;
    }

    @Override
    public Hit intersect(Ray ray) {

        Vec3 shift = subtract(ray.origin, origin);

        double u = dotProduct(vec3(0, 1, 0), shift);
        double v = dotProduct(vec3(0, 1, 0), ray.dir);

        double a = 1 - v * v;
        double b = 2 * (dotProduct(shift, ray.dir) - u * v);
        double c = dotProduct(shift, shift) - u * u;

        double alpha = dotProduct(shift, shift) + radius * radius - thickness * thickness;

        double A = 1;
        double B = 4 * dotProduct(shift, ray.dir);
        double C = 2 * alpha + B * B / 4 - 4 * radius * radius * a;
        double D = B * alpha - 4 * radius * radius * b;
        double E = alpha * alpha - 4 * radius * radius * c;

        // roots content is sorted from closest to furthest
        double[] roots = solveQuartic(A, B, C, D, E);

        if (!(roots == null || roots.length == 0)) {
            double t1 = 0;
            double t2 = 0;
            double t3 = 0;
            double t4 = 0;

            // assign Ts to fit the number of hits
            switch (roots.length) {
                case 1:
                    t1 = roots[0];
                    break;
                case 2:
                    t1 = roots[0];
                    t2 = roots[1];
                    break;
                case 3:
                    t1 = roots[0];
                    t2 = roots[1];
                    t3 = roots[2];
                    break;
                case 4:
                    t1 = roots[0];
                    t2 = roots[1];
                    t3 = roots[2];
                    t4 = roots[3];
                    break;
                default:
            }

            // norm with help from http://cosinekitty.com/raytrace/chapter13_torus.html
            if (ray.contains(t1)) {
                Vec3 pos = ray.pointAt(t1);
                Vec3 shadow = vec3(pos.x, 0, pos.z);
                Vec3 centerToShadow = normalize(subtract(shadow, origin));
                Vec3 centerOfThickness = multiply(radius, centerToShadow);
                Vec3 rayToCenterOT = add(origin, centerOfThickness);
                Vec3 norm = divide(subtract(pos, rayToCenterOT), thickness);
                // TODO: calculate UV
                return new Hit(t1, pos, norm, vec3(0, 0, 0), material);
            }
            if (ray.contains(t2)) {
                Vec3 pos = ray.pointAt(t2);
                Vec3 shadow = vec3(pos.x, 0, pos.z);
                Vec3 centerToShadow = normalize(subtract(shadow, origin));
                Vec3 centerOfThickness = multiply(radius, centerToShadow);
                Vec3 rayToCenterOT = add(origin, centerOfThickness);
                Vec3 norm = divide(subtract(pos, rayToCenterOT), thickness);
                // TODO: calculate UV
                return new Hit(t2, pos, norm, vec3(0, 0, 0), material);
            }
            if (ray.contains(t3)) {
                Vec3 pos = ray.pointAt(t3);
                Vec3 shadow = vec3(pos.x, 0, pos.z);
                Vec3 centerToShadow = normalize(subtract(shadow, origin));
                Vec3 centerOfThickness = multiply(radius, centerToShadow);
                Vec3 rayToCenterOT = add(origin, centerOfThickness);
                Vec3 norm = divide(subtract(pos, rayToCenterOT), thickness);
                // TODO: calculate UV
                return new Hit(t3, pos, norm, vec3(0, 0, 0), material);
            }
            if (ray.contains(t4)) {
                Vec3 pos = ray.pointAt(t4);
                Vec3 shadow = vec3(pos.x, 0, pos.z);
                Vec3 centerToShadow = normalize(subtract(shadow, origin));
                Vec3 centerOfThickness = multiply(radius, centerToShadow);
                Vec3 rayToCenterOT = add(origin, centerOfThickness);
                Vec3 norm = divide(subtract(pos, rayToCenterOT), thickness);
                // TODO: calculate UV
                return new Hit(t4, pos, norm, vec3(0, 0, 0), material);
            }
        }
        return null;
    }

    @Override
    public BoundingBox bounds() {
        Vec3 min = vec3(origin.x - radius, origin.y - thickness, origin.z + radius);
        Vec3 max = vec3(origin.x + radius, origin.y, origin.z - radius);
        return new BoundingBox(min, max);
    }

    /**
     * Adapted from https://github.com/PetrKudr/raytracer
     * <p>
     * Solve a quartic equation of the form ax^4+bx^3+cx^2+cx^1+d=0. The roots
     * are returned in a sorted array of doubles in increasing order.
     *
     * @param a coefficient of x^4
     * @param b coefficient of x^3
     * @param c coefficient of x^2
     * @param d coefficient of x^1
     * @param e coefficient of x^0
     * @return a sorted array of roots, or <code>null</code> if no solutions exist
     */
    public double[] solveQuartic(double a, double b, double c, double d, double e) {
        double inva = 1 / a;
        double c1 = b * inva;
        double c2 = c * inva;
        double c3 = d * inva;
        double c4 = e * inva;
        // cubic resolvant
        double c12 = c1 * c1;
        double p = -0.375 * c12 + c2;
        double q = 0.125 * c12 * c1 - 0.5 * c1 * c2 + c3;
        double r = -0.01171875 * c12 * c12 + 0.0625 * c12 * c2 - 0.25 * c1 * c3 + c4;
        double z = solveCubicForQuartic(-0.5 * p, -r, 0.5 * r * p - 0.125 * q * q);
        double d1 = 2.0 * z - p;
        if (d1 < 0) {
            if (d1 > -0.000001) {
                d1 = 0;
            } else {
                return null;
            }
        }
        double d2;
        if (d1 < 0.000001) {
            d2 = z * z - r;
            if (d2 < 0) {
                return null;
            }
            d2 = Math.sqrt(d2);
        } else {
            d1 = Math.sqrt(d1);
            d2 = 0.5 * q / d1;
        }
        // setup useful values for the quadratic factors
        double q1 = d1 * d1;
        double q2 = -0.25 * c1;
        double pm = q1 - 4 * (z - d2);
        double pp = q1 - 4 * (z + d2);
        if (pm >= 0 && pp >= 0) {
            // 4 roots (!)
            pm = Math.sqrt(pm);
            pp = Math.sqrt(pp);
            double[] results = new double[4];
            results[0] = -0.5 * (d1 + pm) + q2;
            results[1] = -0.5 * (d1 - pm) + q2;
            results[2] = 0.5 * (d1 + pp) + q2;
            results[3] = 0.5 * (d1 - pp) + q2;
            // tiny insertion sort
            for (int i = 1; i < 4; i++) {
                for (int j = i; j > 0 && results[j - 1] > results[j]; j--) {
                    double t = results[j];
                    results[j] = results[j - 1];
                    results[j - 1] = t;
                }
            }
            return results;
        } else if (pm >= 0) {
            pm = Math.sqrt(pm);
            double[] results = new double[2];
            results[0] = -0.5 * (d1 + pm) + q2;
            results[1] = -0.5 * (d1 - pm) + q2;
            return results;
        } else if (pp >= 0) {
            pp = Math.sqrt(pp);
            double[] results = new double[2];
            results[0] = 0.5 * (d1 - pp) + q2;
            results[1] = 0.5 * (d1 + pp) + q2;
            return results;
        }
        return null;
    }

    /**
     * Adapted from https://github.com/PetrKudr/raytracer
     * <p>
     * Return only one root for the specified cubic equation. This routine is
     * only meant to be called by the quartic solver. It assumes the cubic is of
     * the form: x^3+px^2+qx+r.
     *
     * @param p
     * @param q
     * @param r
     * @return double
     */
    private double solveCubicForQuartic(double p, double q, double r) {
        double A2 = p * p;
        double Q = (A2 - 3.0 * q) / 9.0;
        double R = (p * (A2 - 4.5 * q) + 13.5 * r) / 27.0;
        double Q3 = Q * Q * Q;
        double R2 = R * R;
        double d = Q3 - R2;
        double an = p / 3.0;
        if (d >= 0) {
            d = R / Math.sqrt(Q3);
            double theta = Math.acos(d) / 3.0;
            double sQ = -2.0 * Math.sqrt(Q);
            return sQ * Math.cos(theta) - an;
        } else {
            double sQ = Math.pow(Math.sqrt(R2 - Q3) + Math.abs(R), 1.0 / 3.0);
            if (R < 0) {
                return (sQ + Q / sQ) - an;
            } else {
                return -(sQ + Q / sQ) - an;
            }
        }
    }
}
