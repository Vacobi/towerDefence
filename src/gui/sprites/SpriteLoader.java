package gui.sprites;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public abstract class SpriteLoader {

    private static BufferedImage makeTransparent(BufferedImage image, Color colorToRemove) {
        BufferedImage result = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                if ((pixel & 0x00FFFFFF) == (colorToRemove.getRGB() & 0x00FFFFFF)) {
                    result.setRGB(x, y, 0x00000000);
                } else {
                    result.setRGB(x, y, pixel);
                }
            }
        }

        return result;
    }

    public static BufferedImage getSprite(String path) {
        try {
            InputStream is = SpriteLoader.class.getResourceAsStream(path);
            BufferedImage sprite = ImageIO.read(is);
            return makeTransparent(sprite, new Color(255, 0, 255));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
