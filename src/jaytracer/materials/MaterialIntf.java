package jaytracer.materials;

import cgtools.Vec3;
import jaytracer.Hit;
import jaytracer.Ray;

public interface MaterialIntf {

    Vec3 emittedRadiance(Ray r, Hit h);

    Ray scatteredRay(Ray r, Hit h);

    Vec3 albedo(Ray r, Hit h);

}