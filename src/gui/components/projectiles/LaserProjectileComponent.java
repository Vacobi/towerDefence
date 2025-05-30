package gui.components.projectiles;

import collision.Hitbox;
import gui.components.GameComponent;
import projectile.LaserProjectile;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class LaserProjectileComponent extends GameComponent<LaserProjectile> {

    public LaserProjectileComponent(LaserProjectile model) {
        super(model,
                model.getHitbox().getHitboxParameters().width(),
                model.getHitbox().getHitboxParameters().height()
        );

        setBoundingRectangle();
        setOpaque(false);
    }

    private void setBoundingRectangle() {
        Point2D.Double[] vertices = model.getHitbox().getVertices();

        double minX = vertices[0].x;
        double maxX = vertices[0].x;
        double minY = vertices[0].y;
        double maxY = vertices[0].y;

        for (int i = 1; i < vertices.length; i++) {
            minX = Math.min(minX, vertices[i].x);
            maxX = Math.max(maxX, vertices[i].x);
            minY = Math.min(minY, vertices[i].y);
            maxY = Math.max(maxY, vertices[i].y);
        }

        int x = (int) minX;
        int y = (int) minY;
        int width = (int) Math.ceil(maxX - minX);
        int height = (int) Math.ceil(maxY - minY);

        setBounds(x, y, width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        Hitbox hb = model.getHitbox();
        drawHitbox(g2, hb, Color.RED);

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
