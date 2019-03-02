/* *****************************************************************************
 *  Name: KdTree.java
 *  Date: 02/28/2019
 *  Description: A mutable data type PointSET.java that represents a set of
 *               points in the unit square.
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private Node root;
    private int size;

    private class Node {
        private final Point2D point;
        private final RectHV rectangle;
        private Node left;
        private Node right;

        public Node(Point2D point, RectHV rectangle) {
            if (point == null)
                throw new IllegalArgumentException();
            if (rectangle == null)
                throw new IllegalArgumentException();

            this.point = point;
            this.rectangle = rectangle;
        }
    }

    // construct an empty set of points
    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return this.size == 0;
    }

    // number of points in the set
    public int size() {
        return this.size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D point) {
        if (point == null)
            throw new IllegalArgumentException();

        if (isEmpty()) {
            this.root = create(point, new RectHV(0, 0, 1, 1));
            return;
        }

        insert(this.root, point, this.root.rectangle, true);
    }

    private void insert(Node node, Point2D point, RectHV rectangle, boolean horisontal) {
        if (node.point.equals(point))
            return;

        int compared = horisontal
                       ? Point2D.X_ORDER.compare(node.point, point)
                       : Point2D.Y_ORDER.compare(node.point, point);

        if (compared > 0) {
            left(node, rectangle, point, horisontal);
        }
        else {
            right(node, rectangle, point, horisontal);
        }
    }

    private void right(Node node, RectHV rectangle, Point2D point, boolean horisontal) {
        if (node.right == null) {
            RectHV r;
            if (horisontal)
                r = new RectHV(node.point.x(), rectangle.ymin(),
                               rectangle.xmax(), rectangle.ymax());
            else
                r = new RectHV(rectangle.xmin(), node.point.y(),
                               rectangle.xmax(), rectangle.ymax());

            node.right = create(point, r);
        }
        else {
            insert(node.right, point, node.right.rectangle, !horisontal);
        }
    }

    private void left(Node node, RectHV rectangle, Point2D point, boolean horisontal) {
        if (node.left == null) {
            RectHV r;
            if (horisontal)
                r = new RectHV(rectangle.xmin(), rectangle.ymin(),
                               node.point.x(), rectangle.ymax());
            else
                r = new RectHV(rectangle.xmin(), rectangle.ymin(),
                               rectangle.xmax(), node.point.y());

            node.left = create(point, r);
        }
        else {
            insert(node.left, point, node.left.rectangle, !horisontal);
        }
    }

    private Node create(Point2D point, RectHV rectangle) {
        this.size++;
        return new Node(point, rectangle);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        return contains(this.root, p, false);
    }

    private boolean contains(Node node, Point2D p, boolean horisontal) {
        if (node == null)
            return false;

        if (node.point.equals(p))
            return true;

        boolean found = false;
        if (node.left != null && node.left.rectangle.contains(p)) {
            found = contains(node.left, p, !horisontal);
        }

        if (node.right != null && node.right.rectangle.contains(p)) {
            found = contains(node.right, p, !horisontal);
        }

        return found;
    }

    // draw all points to standard draw
    public void draw() {
        if (isEmpty())
            return;

        draw(this.root, false);
    }

    private void draw(Node node, boolean horisontal) {
        if (node == null)
            return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.015);
        StdDraw.point(node.point.x(), node.point.y());

        double xMin, xMax, yMin, yMax;

        if (horisontal) {
            xMin = node.rectangle.xmin();
            xMax = node.rectangle.xmax();
            yMin = node.point.y();
            yMax = node.point.y();

            StdDraw.setPenColor(StdDraw.BLUE);
        }
        else {
            xMin = node.point.x();
            xMax = node.point.x();
            yMin = node.rectangle.ymin();
            yMax = node.rectangle.ymax();

            StdDraw.setPenColor(StdDraw.RED);
        }

        StdDraw.setPenRadius();
        StdDraw.line(xMin, yMin, xMax, yMax);

        draw(node.left, !horisontal);
        draw(node.right, !horisontal);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();

        if (isEmpty())
            return null;

        Queue<Point2D> foundPoints = new Queue<Point2D>();

        range(this.root, rect, foundPoints);

        return foundPoints;
    }

    private void range(Node node, RectHV rect, Queue<Point2D> foundPoints) {
        if (node == null)
            return;

        if (node.rectangle.intersects(rect)) {
            if (rect.contains(node.point))
                foundPoints.enqueue(node.point);

            range(node.left, rect, foundPoints);
            range(node.right, rect, foundPoints);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

        if (isEmpty())
            return null;

        return nearest(this.root, p, this.root.point);
    }

    private Point2D nearest(Node node, Point2D point, Point2D nearestPoint) {
        if (node == null) {
            return null;
        }

        Point2D found = node.point;

        if (point.distanceSquaredTo(found) < point.distanceSquaredTo(nearestPoint))
            nearestPoint = found;

        nearestPoint = nearestPoint(node.left, point, nearestPoint);
        nearestPoint = nearestPoint(node.right, point, nearestPoint);

        return nearestPoint;
    }

    private Point2D nearestPoint(Node node, Point2D point, Point2D nearestPoint) {
        if (node != null
                && node.rectangle.distanceSquaredTo(point) < nearestPoint
                .distanceSquaredTo(point)) {
            nearestPoint = nearest(node, point, nearestPoint);
        }

        return nearestPoint;
    }

    public static void main(String[] args) {
        KdTree ps = new KdTree();
        ps.insert(new Point2D(0.5, 0.5));
        ps.insert(new Point2D(0.0, 0.5));

        StdOut.println(ps.range(new RectHV(0.0, 0.25, 0.25, 0.75)));
        //
        // ps.insert(new Point2D(0.5, 0.4));
        // ps.insert(new Point2D(0.2, 0.3));
        // ps.insert(new Point2D(0.4, 0.7));
        // ps.insert(new Point2D(0.9, 0.6));

        // ps.insert(new Point2D(0.0078125, 0.0));
        // ps.insert(new Point2D(0.0234375, 0.0));
        // ps.insert(new Point2D(0.0390625, 0.0));
        // ps.insert(new Point2D(0.109375, 0.0));
        // ps.insert(new Point2D(0.1171875, 0.0));
        // ps.insert(new Point2D(0.1328125, 0.0));
        // ps.insert(new Point2D(0.1484375, 0.0));
        // ps.insert(new Point2D(0.1640625, 0.0));
        // ps.insert(new Point2D(0.1953125, 0.0));
        // ps.insert(new Point2D(0.2109375, 0.0));
        // ps.insert(new Point2D(0.21875, 0.0));
        // ps.insert(new Point2D(0.2265625, 0.0));
        // ps.insert(new Point2D(0.234375, 0.0));
        // ps.insert(new Point2D(0.2421875, 0.0));
        // ps.insert(new Point2D(0.265625, 0.0));
        // ps.insert(new Point2D(0.2734375, 0.0));
        // ps.insert(new Point2D(0.28125, 0.0));
        // ps.insert(new Point2D(0.2890625, 0.0));
        // ps.insert(new Point2D(0.296875, 0.0));
        // ps.insert(new Point2D(0.3046875, 0.0));
        // ps.insert(new Point2D(0.3203125, 0.0));
        // ps.insert(new Point2D(0.328125, 0.0));
        // ps.insert(new Point2D(0.359375, 0.0));
        // ps.insert(new Point2D(0.390625, 0.0));
        // ps.insert(new Point2D(0.4140625, 0.0));
        // ps.insert(new Point2D(0.421875, 0.0));
        //
        // StdOut.println(ps.range(new RectHV(0.00390625, 0.0, 0.01171875, 0.00390625)));

        // ps.insert(new Point2D(0.0, 0.25));
        // ps.insert(new Point2D(1.0, 0.75));
        // ps.insert(new Point2D(0.0, 0.125));
        // ps.insert(new Point2D(0.875, 0.875));
        // ps.insert(new Point2D(0.75, 0.375));
        // ps.insert(new Point2D(0.875, 0.625));
        // ps.insert(new Point2D(0.125, 1.0));
        // ps.insert(new Point2D(0.75, 0.5));
        // ps.insert(new Point2D(0.625, 0.125));
        // ps.insert(new Point2D(0.5, 1.0));
        // ps.insert(new Point2D(0.25, 0.125));
        // ps.insert(new Point2D(1.0, 0.875));
        // ps.insert(new Point2D(0.625, 0.0));
        // ps.insert(new Point2D(0.375, 0.125));
        // ps.insert(new Point2D(1.0, 0.625));
        // ps.insert(new Point2D(0.0, 1.0));
        // ps.insert(new Point2D(0.375, 0.0));
        // ps.insert(new Point2D(0.625, 0.5));
        // ps.insert(new Point2D(0.25, 0.875));
        // ps.insert(new Point2D(0.125, 0.125));
        //
        // ps.draw();
        //
        // StdOut.println(ps.contains(new Point2D(0.375, 0.125)));
        // StdDraw.setPenColor(StdDraw.RED);
        // StdDraw.setPenRadius(0.01);
        // StdDraw.point(0.375, 0.125);
    }
}
