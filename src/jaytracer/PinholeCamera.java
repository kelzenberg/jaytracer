package jaytracer;

import cgtools.Mat4;
import cgtools.Vec3;

import static cgtools.Vec3.normalize;
import static cgtools.Vec3.vec3;

public class PinholeCamera {

    private final Mat4 transformation;
    private final double angle;
    private int width;
    private int height;

    public PinholeCamera(Mat4 transformation, double angle, int width, int height) {
        // negative Z-Axis in view direction of camera, positive Y-Axis to the top, positive X-Axis to the right
        this.transformation = transformation;
        this.angle = angle;
        this.width = width;
        this.height = height;
    }

    Ray createRay(double x, double y) {
        double a = x - width / 2;
        double b = height / 2 - y;
        double c = -((width / 2) / Math.tan(angle / 2));

        Vec3 dir = transformation.transformDirection(normalize(vec3(a, b, c)));

        if (angle == 360) {
            // theta:
            double inclination = Math.PI * (y / (height));
            // phi:
            double azimuth = 2 * Math.PI * (x / width);
            dir = transformation.transformDirection(normalize(vec3(
                    (Math.sin(inclination) * Math.sin(azimuth)),    // x
                    Math.cos(inclination),                          // y
                    (Math.sin(inclination) * Math.cos(azimuth))))); // z
        }

        return new Ray(transformation.transformPoint(vec3(0, 0, 0)), dir, 0, Double.POSITIVE_INFINITY);
    }

    public double getAngle() {
        return angle;
    }
}