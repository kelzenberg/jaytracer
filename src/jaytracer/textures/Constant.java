package jaytracer.textures;

import cgtools.Vec3;

public class Constant implements TextureIntf {

    private Vec3 color;

    public Constant(Vec3 color) {
        this.color = color;
    }

    @Override
    public Vec3 getColor(Vec3 uv) {
        // uv will be ignored because texel getColor is everywhere the same
        return color;
    }
}
