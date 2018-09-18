package jaytracer.shapes;

import cgtools.Mat4;
import cgtools.Vec3;
import jaytracer.BoundingBox;
import jaytracer.Hit;
import jaytracer.Ray;
import jaytracer.Transformation;
import jaytracer.materials.MaterialIntf;

import java.util.ArrayList;
import java.util.Arrays;

import static cgtools.Vec3.zero;

public class Group implements ShapeIntf {

    private ArrayList<ShapeIntf> shapes;
    private Transformation trans;
    private BoundingBox box;

    public Group(Mat4 transformation) {
        trans = new Transformation(transformation);
        box = new BoundingBox();
        shapes = new ArrayList<>();
    }

    public void fillWith(ShapeIntf... shape) {
        shapes.addAll(Arrays.asList(shape));
        for (ShapeIntf member : shapes) {
            BoundingBox bbMember = member.bounds();
            if (member instanceof Group) {
                bbMember = bbMember.transform(((Group) member).trans.toWorld);
            }
            box = box.extend(bbMember);
        }
    }

    public ArrayList<ShapeIntf> getShapes() {
        return shapes;
    }

    @Override
    public Hit intersect(Ray ray) {
        double t = Double.POSITIVE_INFINITY;
        Vec3 pos = zero;
        Vec3 normal = zero;
        Vec3 uv = zero;
        MaterialIntf material = null;
        Boolean hit = false;

        // transform ray to local coordinates
        Ray localRay = new Ray(trans.pointToObject(ray.origin), trans.dirToObject(ray.dir), ray.t0, ray.t1);

        // TODO: flatten the hierarchy & auto-sort shapes into groups (see VL)
        // check if ray hits this group (box) at all
        if (!box.intersect(localRay)) {
            return null;
        }
        for (ShapeIntf x : shapes) {
            Hit temp = x.intersect(localRay);
            if (temp != null) {
                if (temp.getT() <= t && temp.getT() >= 0) {
                    t = temp.getT();
                    pos = temp.getPos();
                    normal = temp.getNormal();
                    uv = temp.getUv();
                    material = temp.getMaterial();
                    hit = true;
                }
            }
        }
        if (!hit) {
            return null;
        } else {
            // transform results back to world coordinates
            return new Hit(t, trans.pointToWorld(pos), trans.normToWorld(normal), uv, material);
        }
    }

    @Override
    public BoundingBox bounds() {
        return box;
    }
}