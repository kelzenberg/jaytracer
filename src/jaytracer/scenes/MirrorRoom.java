package jaytracer.scenes;

import cgtools.Mat4;
import cgtools.Vec3;
import jaytracer.PinholeCamera;
import jaytracer.lights.LightIntf;
import jaytracer.materials.Luminescent;
import jaytracer.materials.Reflective;
import jaytracer.materials.Transparent;
import jaytracer.shapes.*;
import jaytracer.textures.Constant;
import jaytracer.textures.TextureFromImage;

import java.util.ArrayList;

import static cgtools.Mat4.rotate;
import static cgtools.Mat4.translate;
import static cgtools.Vec3.vec3;
import static cgtools.Vec3.white;

public class MirrorRoom implements Scene {

    private final PinholeCamera cam;
    private final Group group;
    private final ArrayList<LightIntf> lights;
    private Vec3 blue = vec3(239 / 255.0, 250 / 255.0, 252 / 255.0);
    private Vec3 sand = vec3(254 / 255.0, 248 / 255.0, 242 / 255.01);
    private Vec3 red = vec3(253 / 255.0, 247 / 255.0, 247 / 255.0);

    public MirrorRoom(int width, int height) {
        cam = new PinholeCamera(translate(vec3(0, -1, 1.9999)), 360, width, height);
        group = new Group(Mat4.identity);
        lights = new ArrayList<>();

        ShapeIntf background = new Background(new Luminescent(new TextureFromImage("./src/textures/stars.jpg")));
        ShapeIntf ground = new Plane(vec3(0, -1.5, 0), vec3(5, 0, 5), new Reflective(new Constant(white), 0.5));

        Group ceiling = new Group(translate(vec3(0, 2, 0)).multiply(rotate(vec3(0, 1, 0), 45)).multiply(rotate(vec3(1, 0, 0), 180)));
        ShapeIntf ceil1 = new Plane(vec3(0, 0, 0), vec3(1, 0, 1), new Luminescent(new Constant(white)));
        ceiling.fillWith(ceil1);
        ShapeIntf torusBig = new Torus(vec3(0, 1.5, 0), 2, 0.1, new Luminescent(new Constant(white)));

        Group front = new Group(translate(vec3(0, 0, 2)).multiply(rotate(vec3(1, 0, 0), 90)));
        ShapeIntf frontwall = new Plane(vec3(0, 0, 0), vec3(5, 0, 5), new Reflective(new Constant(red), 0.13));
        front.fillWith(frontwall);

        Group right = new Group(translate(vec3(2, 0, 0)).multiply(rotate(vec3(0, 1, 0), -90).multiply(rotate(vec3(1, 0, 0), 90))));
        ShapeIntf rightwall = new Plane(vec3(0, 0, 0), vec3(5, 0, 5), new Reflective(new Constant(blue), 0.13));
        right.fillWith(rightwall);

        Group back = new Group(translate(vec3(0, 0, -2)).multiply(rotate(vec3(1, 0, 0), -90)));
        ShapeIntf backwall = new Plane(vec3(0, 0, 0), vec3(5, 0, 5), new Reflective(new Constant(red), 0.13));
        back.fillWith(backwall);

        Group left = new Group(translate(vec3(-2, 0, 0)).multiply(rotate(vec3(0, 1, 0), 90).multiply(rotate(vec3(1, 0, 0), 90))));
        ShapeIntf leftwall = new Plane(vec3(0, 0, 0), vec3(5, 0, 5), new Reflective(new Constant(blue), 0.13));
        left.fillWith(leftwall);

        Group objects = new Group(Mat4.identity);
        ShapeIntf innerSphere = new Sphere(vec3(0, -0.82, 0), 0.35, new Reflective(new Constant(white), 0));
        ShapeIntf outerSphere = new Sphere(vec3(0, -0.82, 0), 0.5, new Transparent(new Constant(white), 1.5));
        ShapeIntf podest = new Cuboid(vec3(-0.25, -1.5, -0.25), vec3(0.25, -1.3, 0.25), new Transparent(new Constant(blue), 2.5));
        ShapeIntf torusSmall = new Torus(vec3(0, -1.5, 0), 0.75, 0.1, new Luminescent(new Constant(white)));
        objects.fillWith(innerSphere, outerSphere, podest, torusSmall);

        //LightIntf dirLight = new DirectionalLight(vec3(0, -1, 0), vec3(1, 1, 1));
        //lights.add(dirLight);
        group.fillWith(background, ground, ceiling, torusBig, front, right, back, left, objects);
    }

    @Override
    public Group getGroup() {
        return group;
    }

    @Override
    public ArrayList<LightIntf> getLights() {
        return lights;
    }

    @Override
    public PinholeCamera getCam() {
        return cam;
    }
}
