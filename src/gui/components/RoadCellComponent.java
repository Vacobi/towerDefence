package gui.components;

import core.RoadCell;
import gui.sprites.SpriteLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RoadCellComponent extends GameComponent<RoadCell> {

    protected static BufferedImage sprite;

    static {
        String pathToGrassSprite = "/gui/sprites/stone.png";
        sprite = SpriteLoader.getSprite(pathToGrassSprite);
    }

    public RoadCellComponent(RoadCell model, int width, int height) {
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
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(0.5F));
        g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
}