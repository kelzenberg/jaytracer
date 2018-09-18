package jaytracer.scenes;

import cgtools.Mat4;
import cgtools.Random;
import jaytracer.PinholeCamera;
import jaytracer.lights.LightIntf;
import jaytracer.materials.Luminescent;
import jaytracer.materials.Reflective;
import jaytracer.shapes.Background;
import jaytracer.shapes.Group;
import jaytracer.shapes.ShapeIntf;
import jaytracer.shapes.Sphere;
import jaytracer.textures.Constant;
import jaytracer.textures.TextureFromImage;

import java.util.ArrayList;

import static cgtools.Mat4.rotate;
import static cgtools.Mat4.translate;
import static cgtools.Vec3.vec3;
import static cgtools.Vec3.white;

public class TextureTest implements Scene {

    private final PinholeCamera cam;
    private final Group group;
    private final ArrayList<LightIntf> lights;

    public TextureTest(int width, int height) {
        cam = new PinholeCamera(Mat4.identity, 360, width, height);
        group = new Group(Mat4.identity);
        lights = new ArrayList<>();

        Group trio = new Group(Mat4.identity);
        ShapeIntf sphere1 = new Sphere(vec3(-0.5, -0.25, -1), 0.3, new Reflective(new Constant(white), 0.2));
        ShapeIntf sphere2 = new Sphere(vec3(0, 0.25, -2), 0.3, new Reflective(new Constant(white), 0.2));
        ShapeIntf sphere3 = new Sphere(vec3(0.5, -0.25, -1), 0.3, new Reflective(new Constant(white), 0.2));
        trio.fillWith(sphere1, sphere2, sphere3);

        for (int i = 0; i < 50; i++) {
            double boo = Random.random();
            int VZi = 1;
            if (boo >= 0.5) {
                VZi = -1;
            }
            double x = Random.random() * 5.0 * VZi;
            double y = Random.random() * 5.0 * VZi;
            double z = Random.random() * 5.0 * VZi;
            double a = Random.random() * 3.6 * 100 * VZi;
            double b = Random.random() * 3.6 * 100 * VZi;
            double c = Random.random() * 3.6 * 100 * VZi;
            Group moved = new Group(translate(vec3(x, y, z)).multiply(rotate(vec3(0, 1, 0), a).multiply(rotate(vec3(0, 0, 1), b).multiply(rotate(vec3(1, 0, 0), c)))));
            moved.fillWith(trio);
            group.fillWith(moved);
        }
        ShapeIntf background = new Background(new Luminescent(new TextureFromImage("./src/textures/lake.jpg")));
        group.fillWith(background);
    }

    @Override
    public PinholeCamera getCam() {
        return cam;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public ArrayList<LightIntf> getLights() {
        return lights;
    }
}
