import java.util.Comparator;

/**
 * A simple class representing a location in 2D space.
 */
public final class Point {
    public final int x;

    public final int y;

    private int pathLen;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
        pathLen = -1;
    }

    public String toString() {
        return "(" + x + "," + y + ", ?" + pathLen + ")";
    }

    public boolean equals(Object other) {
        return other instanceof Point && ((Point) other).x == this.x && ((Point) other).y == this.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public double distance(Point p2){
        return Math.sqrt(Functions.distanceSquared(this, p2));
    }

    public int hashCode() {
        int result = 17;
        result = result * 31 + x;
        result = result * 31 + y;
        return result;
    }

    public int getPathLen() {
        return pathLen;
    }

    public void setPathLen(int pathLen) {
        this.pathLen = pathLen;
    }
}
