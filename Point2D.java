/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class Point2D implements Comparable<Point2D> {
    private final double x;
    private final double y;

    // construct the point (x, y)
    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // x-coordinate
    public double x() {
        return this.x;
    }

    // y-coordinate
    public double y() {
        return this.y;
    }

    // Euclidean distance between two points
    public double distanceTo(Point2D that) {

    }

    // square of Euclidean distance between two points
    public double distanceSquaredTo(Point2D that) {

    }

    // for use in an ordered symbol table
    public int compareTo(Point2D that) {

    }

    // does this point equal that object?
    public boolean equals(Object that) {
        if (that == null || this.getClass() != that.getClass())
            return false;

        Point2D thatInstance = (Point2D) that;

        if (this.x == thatInstance.x && this.y == thatInstance.y)
            return true;

        return false;
    }

    // draw to standard draw
    public void draw() {

    }

    // string representation
    public String toString() {
        return "x=" + this.x + ", y=" + this.y;
    }
}
