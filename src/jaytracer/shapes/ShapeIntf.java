package jaytracer.shapes;

import jaytracer.BoundingBox;
import jaytracer.Hit;
import jaytracer.Ray;

public interface ShapeIntf {

    Hit intersect(Ray ray);

    BoundingBox bounds();

}