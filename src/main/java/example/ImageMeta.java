package example;

import java.awt.image.BufferedImage;

public class ImageMeta {
    private BufferedImage image;
    private String src;

    public BufferedImage getImage()
    {
        return image;
    }

    @Override
    public String toString() {
        return "ImageMeta{" +
                "image=" + image +
                ", src='" + src + '\'' +
                '}';
    }
}
