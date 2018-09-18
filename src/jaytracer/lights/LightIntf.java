package jaytracer.lights;

import cgtools.Vec3;
import jaytracer.shapes.Group;

public interface LightIntf {

    Sample pointHitsLight(Group group, Vec3 fromPoint);

    class Sample {

        // Direction to light source
        public Vec3 dir;
        public Vec3 emis;

        public Sample(Vec3 dir, Vec3 emis) {
            this.dir = dir;
            this.emis = emis;
        }
    }
}

