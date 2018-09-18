package jaytracer.scenes;

import cgtools.Mat4;
import jaytracer.PinholeCamera;
import jaytracer.lights.DirectionalLight;
import jaytracer.lights.LightIntf;
import jaytracer.materials.Diffuse;
import jaytracer.materials.Luminescent;
import jaytracer.materials.Transparent;
import jaytracer.shapes.*;
import jaytracer.textures.CheckerBoard;
import jaytracer.textures.Constant;
import jaytracer.textures.TextureFromImage;

import java.util.ArrayList;

import static cgtools.Mat4.rotate;
import static cgtools.Mat4.translate;
import static cgtools.Vec3.black;
import static cgtools.Vec3.vec3;
import static cgtools.Vec3.white;

public class LightTest implements Scene {

    private final PinholeCamera cam;
    private final Group group;
    private final ArrayList<LightIntf> lights;

    public LightTest(int width, int height) {

        cam = new PinholeCamera(translate(0, 1.5, 0).multiply(translate(vec3(2, 0, -2)).multiply(rotate(vec3(0, 1, 0), 90).multiply(rotate(vec3(1, 0, 0), -35)))), Math.PI / 1.75, width, height);
        group = new Group(Mat4.identity);
        lights = new ArrayList<>();

        ShapeIntf background = new Background(new Luminescent(new TextureFromImage("./src/textures/lake.jpg")));
        ShapeIntf cuboid = new Cuboid(vec3(-0.5, 0.15, -1.7), vec3(0.5, 0.75, -2.3), new Transparent(new Constant(white), 2.5));
        ShapeIntf torus = new Torus(vec3(0, -0.15, -2), 0.75, 0.15, new Diffuse(new Constant(white)));
        ShapeIntf plane = new Plane(vec3(0, -0.3, -2), vec3(2.3, 0, 2.3), new Diffuse(new CheckerBoard(black, white, 15)));
        group.fillWith(background, cuboid, torus, plane);

        LightIntf dirLight = new DirectionalLight(vec3(0, -1, -1), vec3(1, 1, 1));
        lights.add(dirLight);
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
