package pointManager;

public class PointManager {
    private final double x;
    private final double y;
    private final double r;
    private final boolean isInArea;


    public PointManager(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.isInArea = isInside(x,y,r);
    }

    private boolean isInside(double x, double y, double r) {
        return (x <= 0 && y <= 0 && x * x + y * y <= r/2 * r/2) ||
                (x >= 0 && y <= 0 && x <= r && y >= -r/2) ||
                (x <= 0 && y >= 0 && y <= x + r);

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public boolean isInArea() {
        return isInArea;
    }
}