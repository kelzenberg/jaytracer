package jaytracer.textures;

import cgtools.Mat4;
import cgtools.Vec3;

public class TransformedTexture implements TextureIntf {

    private TextureIntf texture;
    private Mat4 transform;

    public TransformedTexture(TextureIntf texture, Mat4 transform) {
        this.texture = texture;
        this.transform = transform;
    }

    @Override
    public Vec3 getColor(Vec3 uv) {
        return texture.getColor(transform.transformPoint(uv));
    }
}
