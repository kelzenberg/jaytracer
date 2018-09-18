package jaytracer.textures;

import cgtools.Vec3;

public class CheckerBoard implements TextureIntf {

    private Vec3 primary;
    private Vec3 secondary;
    private double n;

    public CheckerBoard(Vec3 primary, Vec3 secondary, double n) {
        this.primary = primary;
        this.secondary = secondary;
        this.n = n;
    }

    @Override
    public Vec3 getColor(Vec3 uv) {
        double u = uv.x % 1;
        double v = uv.y % 1;
        double ui = (int) (u * n);
        double vi = (int) (v * n);
        if ((ui + vi) % 2 == 0 ^ uv.y < 0 ^ uv.x < 0) {
            return secondary;
        } else {
            return primary;
        }
    }
}