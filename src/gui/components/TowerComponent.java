package gui.components;

import gui.sprites.SpriteLoader;
import projectile.ExplosiveProjectile;
import projectile.LaserProjectile;
import projectile.PlainProjectile;
import projectile.Projectile;
import tower.Tower;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class TowerComponent extends GameComponent<Tower<?>> {

    protected static Map<Class<? extends Projectile>, BufferedImage> sprites;
    protected BufferedImage sprite;

    static {
        sprites = new HashMap<>();

        String pathToLaserTowerSprite = "/gui/sprites/laser_tower.png";
        sprites.put(LaserProjectile.class, SpriteLoader.getSprite(pathToLaserTowerSprite));

        String pathToDefaultTowerSprite = "/gui/sprites/tower.png";
        sprites.put(PlainProjectile.class, SpriteLoader.getSprite(pathToDefaultTowerSprite));

        String pathToExplosiveTowerSprite = "/gui/sprites/explosive_tower.png";
        sprites.put(ExplosiveProjectile.class, SpriteLoader.getSprite(pathToExplosiveTowerSprite));
    }

    public TowerComponent(Tower<?> model, int width, int height) {
        super(model,
                width,
                height
        );

        sprite = sprites.get(model.getTypicalProjectile().getClass());
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        if (sprite != null) {
            g2.drawImage(sprite,
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    null
            );
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.drawRect(0, 0, getWidth(), getHeight());
        }
    }
}