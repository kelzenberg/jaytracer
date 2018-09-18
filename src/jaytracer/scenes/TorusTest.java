package jaytracer.scenes;

import cgtools.Mat4;
import jaytracer.PinholeCamera;
import jaytracer.lights.LightIntf;
import jaytracer.materials.Diffuse;
import jaytracer.materials.Luminescent;
import jaytracer.materials.Reflective;
import jaytracer.shapes.*;
import jaytracer.textures.CheckerBoard;
import jaytracer.textures.Constant;
import jaytracer.textures.TextureFromImage;

import java.util.ArrayList;

import static cgtools.Mat4.rotate;
import static cgtools.Mat4.translate;
import static cgtools.Vec3.*;

public class TorusTest implements Scene {

    private final PinholeCamera cam;
    private final Group group;
    private final ArrayList<LightIntf> lights;

    public TorusTest(int width, int height) {
        cam = new PinholeCamera(translate(0, 1.5, 0).multiply(translate(vec3(2, 0, -2)).multiply(rotate(vec3(0, 1, 0), 90).multiply(rotate(vec3(1, 0, 0), -35)))), Math.PI / 2, width, height);
        group = new Group(Mat4.identity);
        lights = new ArrayList<>();
        ShapeIntf background = new Background(new Luminescent(new TextureFromImage("./src/textures/lake.jpg")));
        ShapeIntf torus = new Torus(vec3(0, 0, -2), 0.75, 0.3, new Reflective(new Constant(white), 0.1));
        ShapeIntf plane = new Plane(vec3(0, -0.3, -2), vec3(2.3, 0, 2.3), new Diffuse(new CheckerBoard(white, black, 5)));
        ShapeIntf sphere = new Sphere(vec3(0, 0, -2), 0.2, new Luminescent(new TextureFromImage("./src/textures/pattern.jpg")));
        Group turnCube = new Group(translate(vec3(0, -1, -0.3)).multiply(rotate(vec3(1, 0, 0), 45).multiply(rotate(vec3(0, 0, 1), 45))));
        ShapeIntf cuboid = new Cuboid(vec3(-0.05, -0.05, -1.95), vec3(0.05, 0.05, -2.05), new Luminescent(new Constant(vec3(1, 1, 1))));
        turnCube.fillWith(cuboid);
        group.fillWith(background, torus, plane, sphere, turnCube);
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
