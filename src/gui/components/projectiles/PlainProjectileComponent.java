package gui.components.projectiles;

import collision.Hitbox;
import collision.HitboxParameters;
import gui.components.GameComponent;
import gui.sprites.SpriteLoader;
import projectile.PlainProjectile;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class PlainProjectileComponent extends GameComponent<PlainProjectile> {

    protected static BufferedImage sprite;

    static {
        String pathToSprite = "/gui/sprites/cool_projectile.png";
        sprite = SpriteLoader.getSprite(pathToSprite);
    }

    public PlainProjectileComponent(PlainProjectile model) {
        super(model,
            model.getHitbox().getHitboxParameters().width(),
            model.getHitbox().getHitboxParameters().height()
        );

        Hitbox hitbox = model.getHitbox();
        HitboxParameters parameters = model.getHitbox().getHitboxParameters();
        setBounds(hitbox.getX() - parameters.width()/2, hitbox.getY() - parameters.height()/2, parameters.width(), parameters.height());
        setOpaque(false);
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        if (sprite != null) {
            g2.drawImage(sprite, 0, 0, getWidth(), getHeight(), null);
        } else {
            drawHitbox(g2, model.getHitbox(), Color.GRAY);
        }

        g2.dispose();
    }

    private void drawHitbox(Graphics2D g2, Hitbox h, Color color) {
        Point2D.Double[] vs = h.getVertices();
        Path2D p = new Path2D.Double();

        int componentX = getX();
        int componentY = getY();

        p.moveTo(vs[0].x - componentX, vs[0].y - componentY);
        for (int i = 1; i < vs.length; i++) {
            p.lineTo(vs[i].x - componentX, vs[i].y - componentY);
        }
        p.closePath();

        g2.setColor(color);
        g2.fill(p);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(color.darker());
        g2.draw(p);
    }
}
