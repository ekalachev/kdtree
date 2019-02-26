/* *****************************************************************************
 *  Name: PointSET.java
 *  Date: 02/26/2019
 *  Description: A mutable data type PointSET.java that represents a set of
 *               points in the unit square.
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> coordinates;

    // construct an empty set of points
    public PointSET() {
        coordinates = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.coordinates.isEmpty();
    }

    // number of points in the set
    public int size() {
        return this.coordinates.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        this.coordinates.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        return this.coordinates.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : this.coordinates) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();

        Point2D minPoint = new Point2D(rect.xmin(), rect.ymin());
        Point2D maxPoint = new Point2D(rect.xmax(), rect.ymax());

        return this.coordinates.subSet(minPoint, maxPoint);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        if (this.coordinates.isEmpty())
            return null;

        double minDistance = Double.POSITIVE_INFINITY;
        Point2D nearestPoint = null;

        for (Point2D point : this.coordinates) {
            double distance = p.distanceSquaredTo(point);

            if (Double.compare(distance, minDistance) < 0) {
                minDistance = distance;
                nearestPoint = point;
            }
        }

        return nearestPoint;
    }

    public static void main(String[] args) {
        PointSET ps = new PointSET();
        ps.insert(new Point2D(0.1, 0.1));
        ps.insert(new Point2D(0.2, 0.2));
        ps.insert(new Point2D(0.3, 0.3));
        ps.insert(new Point2D(0.4, 0.4));
        ps.insert(new Point2D(0.5, 0.5));

        StdOut.println(ps.range(new RectHV(0.2, 0.2, 0.6, 0.6)));
        StdOut.println(ps.nearest(new Point2D(0.2, 0.4)));
        ps.draw();
    }
}
