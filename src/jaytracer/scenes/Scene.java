package jaytracer.scenes;

import jaytracer.PinholeCamera;
import jaytracer.lights.LightIntf;
import jaytracer.shapes.Group;

import java.util.ArrayList;

public interface Scene {

    public PinholeCamera getCam();

    public Group getGroup();

    public ArrayList<LightIntf> getLights();
}
