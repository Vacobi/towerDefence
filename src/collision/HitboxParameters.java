package collision;

public class HitboxParameters {
    private int width, height;
    private double angle;

    public HitboxParameters(int width, int height, double angle) {
        this.width = width;
        this.height = height;
        this.angle = angle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getAngle() {
        return angle;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
