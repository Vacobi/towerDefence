package collision;

import utils.Position;

import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class Hitbox {
    private int x;
    private int y;
    private HitboxParameters hitboxParameters;

    public Hitbox(int x, int y, HitboxParameters hitboxParameters) {
        this.x = x;
        this.y = y;
        this.hitboxParameters = hitboxParameters;
    }

    public Hitbox(Position center, HitboxParameters hitboxParameters) {
        this.x = center.getX();
        this.y = center.getY();
        this.hitboxParameters = hitboxParameters;
    }

    //------------------------------------------------------------------------------------------------------------------

    public Point2D.Double[] getVertices() {
        Point2D.Double[] vertices = new Point2D.Double[4];

        double halfWidth = hitboxParameters.width() / 2.0;
        double halfHeight = hitboxParameters.height() / 2.0;

        double[] xOffsets = {-halfWidth, halfWidth, halfWidth, -halfWidth};
        double[] yOffsets = {-halfHeight, -halfHeight, halfHeight, halfHeight};

        double angle = hitboxParameters.angle();
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        for (int i = 0; i < 4; i++) {
            double rotatedX = xOffsets[i] * cos - yOffsets[i] * sin;
            double rotatedY = xOffsets[i] * sin + yOffsets[i] * cos;
            vertices[i] = new Point2D.Double(x + rotatedX, y + rotatedY);
        }

        return vertices;
    }

    public void updateCenter(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }

    public boolean intersects(Hitbox other) {
        Area thisArea = createArea(this);
        Area otherArea = createArea(other);

        thisArea.intersect(otherArea);
        return !thisArea.isEmpty();
    }

    private Area createArea(Hitbox hitbox) {
        Path2D path = new Path2D.Double();
        Point2D.Double[] vertices = hitbox.getVertices();

        path.moveTo(vertices[0].x, vertices[0].y);
        for (int i = 1; i < vertices.length; i++) {
            path.lineTo(vertices[i].x, vertices[i].y);
        }
        path.closePath();
        return new Area(path);
    }

    //------------------------------------------------------------------------------------------------------------------

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public HitboxParameters getHitboxParameters() {
        return hitboxParameters;
    }

    public Position getCenter() {
        return new Position(x, y);
    }
}