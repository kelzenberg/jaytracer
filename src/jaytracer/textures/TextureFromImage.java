package jaytracer.textures;

import cgtools.Vec3;

import java.io.IOException;

import static cgtools.Vec3.vec3;

public class TextureFromImage implements TextureIntf {

    private cgtools.ImageTexture texture;

    public TextureFromImage(String filename) {
        try {
            texture = new cgtools.ImageTexture(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vec3 getColor(Vec3 uv) {
        // uv.z is not relevant and always 0
        return revertGamma(texture.samplePoint(uv.x, uv.y));
    }

    private Vec3 revertGamma(Vec3 color) {

        // remove gamma correction
        // TODO: remove gamma relative and/or read from Metadata of file
        double ix = Math.pow(color.x, 2.2);
        double iy = Math.pow(color.y, 2.2);
        double iz = Math.pow(color.z, 2.2);

        return vec3(ix, iy, iz);
    }
}
