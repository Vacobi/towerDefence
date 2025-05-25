package gui;

import collision.Hitbox;
import projectile.LaserProjectile;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class LaserProjectileComponent extends GameComponent<LaserProjectile> {

    public LaserProjectileComponent(LaserProjectile model, int fieldWidthPx, int fieldHeightPx) {
        super(model,
                model.getHitbox().getHitboxParameters().width(),
                model.getHitbox().getHitboxParameters().height()
        );

        setBounds(0, 0, fieldWidthPx, fieldHeightPx);
        setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        Hitbox hb = model.getHitbox();
        drawHitbox(g2, hb, Color.RED);

        g2.dispose();
    }

    private void drawHitbox(Graphics2D g2, Hitbox h, Color color) {
        Point2D.Double[] vs = h.getVertices();
        Path2D p = new Path2D.Double();
        p.moveTo(vs[0].x, vs[0].y);
        for (int i = 1; i < vs.length; i++) {
            p.lineTo(vs[i].x, vs[i].y);
        }
        p.closePath();

        g2.setColor(color);
        g2.fill(p);
        g2.setStroke(new BasicStroke(2f));
        g2.setColor(color.darker());
        g2.draw(p);
    }
}
