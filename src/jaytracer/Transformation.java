package jaytracer;

import cgtools.Mat4;
import cgtools.Vec3;

public class Transformation {

    public Mat4 toObject;
    public Mat4 toWorld;

    public Transformation(Mat4 transformation) {
        toObject = transformation.invertFull();
        toWorld = transformation;
    }

    public Vec3 pointToObject(Vec3 point) {
        return toObject.transformPoint(point);
    }

    public Vec3 pointToWorld(Vec3 point) {
        return toWorld.transformPoint(point);
    }

    public Vec3 dirToObject(Vec3 dir) {
        return toObject.transformDirection(dir);
    }

    public Vec3 dirToWorld(Vec3 dir) {
        return toWorld.transformDirection(dir);
    }

    public Vec3 normToWorld(Vec3 normal) {
        return toWorld.invertFull().transpose().transformDirection(normal);
    }
}
