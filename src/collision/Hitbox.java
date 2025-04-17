package collision;

import utils.Position;

import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class Hitbox {
    private int x, y;
    private int width, height;
    private double angle;

    public Hitbox(int x, int y, int width, int height, double angle) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
    }

    public Point2D.Double[] getVertices() {
        Point2D.Double[] vertices = new Point2D.Double[4];

        int[] xOffsets = {0, width, width, 0};
        int[] yOffsets = {0, 0, height, height};

        for (int i = 0; i < 4; i++) {
            double rotatedX = x + xOffsets[i] * Math.cos(angle) - yOffsets[i] * Math.sin(angle);
            double rotatedY = y + xOffsets[i] * Math.sin(angle) + yOffsets[i] * Math.cos(angle);

            vertices[i] = new Point2D.Double(rotatedX, rotatedY);
        }

        return vertices;
    }

    public void updateLeftTop(int x, int y) {
        updateLeftTop(new Position(x, y));
    }

    public void updateLeftTop(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }

    public boolean intersects(Hitbox other) {
        Path2D thisPath = new Path2D.Double();
        Point2D.Double[] thisVertices = this.getVertices();
        thisPath.moveTo(thisVertices[0].x, thisVertices[0].y);
        for (int i = 1; i < thisVertices.length; i++) {
            thisPath.lineTo(thisVertices[i].x, thisVertices[i].y);
        }
        thisPath.closePath();

        Path2D otherPath = new Path2D.Double();
        Point2D.Double[] otherVertices = other.getVertices();
        otherPath.moveTo(otherVertices[0].x, otherVertices[0].y);
        for (int i = 1; i < otherVertices.length; i++) {
            otherPath.lineTo(otherVertices[i].x, otherVertices[i].y);
        }
        otherPath.closePath();

        Area thisArea = new Area(thisPath);
        Area otherArea = new Area(otherPath);

        thisArea.intersect(otherArea);
        return !thisArea.isEmpty();
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public double getAngle() { return angle; }
    public Position getLeftTop() {
        return new Position(x, y);
    }
}