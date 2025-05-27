package gui;

import collision.Hitbox;
import collision.HitboxParameters;
import gui.sprites.SpriteLoader;
import projectile.ExplosiveProjectile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ExplosiveProjectileComponent extends GameComponent<ExplosiveProjectile> {

    protected static BufferedImage sprite;
    protected static BufferedImage explosionSprite;

    static {
        String pathToSprite = "/gui/sprites/explosive_projectile.png";
        sprite = SpriteLoader.getSprite(pathToSprite);

        explosionSprite = SpriteLoader.getSprite("/gui/sprites/explose.png");
    }

    public ExplosiveProjectileComponent(ExplosiveProjectile model, int fieldWidthPx, int fieldHeightPx) {
        super(model,
                model.getHitbox().getHitboxParameters().width(),
                model.getHitbox().getHitboxParameters().height()
        );

        Hitbox hitbox = model.getHitbox();
        HitboxParameters parameters = model.getHitbox().getHitboxParameters();
        setBounds(hitbox.getX() - parameters.width()/2, hitbox.getY() - parameters.height()/2, parameters.width(), parameters.height());
        setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        if (model.isExploded()) {
            startExplosion();
            int explosionSize = model.getRadius() * 2;
            int x = getWidth()/2 - model.getRadius();
            int y = getHeight()/2 - model.getRadius();
            g2.drawImage(explosionSprite, x, y, explosionSize, explosionSize, null);
        } else {
            g2.drawImage(sprite, 0, 0, getWidth(), getHeight(), null);
        }

        g2.dispose();
    }

    private void startExplosion() {

        int explosionSize = model.getRadius() * 2;
        setBounds(
                getX() + getWidth()/2 - explosionSize/2,
                getY() + getHeight()/2 - explosionSize/2,
                explosionSize,
                explosionSize
        );

        repaint();
    }
}
