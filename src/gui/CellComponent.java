package gui;

import core.Cell;
import gui.sprites.SpriteLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CellComponent extends GameComponent<Cell> {

    protected static BufferedImage sprite;

    static {
        String pathToGrassSprite = "/gui/sprites/grass_sprite.png";
        sprite = SpriteLoader.getSprite(pathToGrassSprite);
    }

    public CellComponent(Cell model, int width, int height) {
        super(model, width, height);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int borderSize = 1;

        if (sprite != null) {
            g2.drawImage(
                    sprite,
                    borderSize, borderSize,
                    getWidth() - 2 * borderSize, getHeight() - 2 * borderSize,
                    null
            );
        } else {
            g2.setColor(new Color(40, 180, 40));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(0.5F));
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
}
