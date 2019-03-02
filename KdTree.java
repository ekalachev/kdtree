/* *****************************************************************************
 *  Name: KdTree.java
 *  Date: 02/28/2019
 *  Description: A mutable data type PointSET.java that represents a set of
 *               points in the unit square.
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private class Node {
        private Point2D point;
        private RectHV rectangle;
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

    private Node root;
    private int size;

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

        if (node.left != null) {
            boolean goLeft = horisontal
                             ? node.left.rectangle.ymax() >= p.y()
                             : node.left.rectangle.xmax() >= p.x();

            if (goLeft)
                return contains(node.left, p, !horisontal);
        }

        return contains(node.right, p, !horisontal);
    }

    // draw all points to standard draw
    public void draw() {

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();

        if (isEmpty())
            return null;

        Queue<Point2D> foundPoints = new Queue<>();

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

        if (point.distanceTo(found) < point.distanceTo(nearestPoint))
            nearestPoint = found;

        nearestPoint = nearestPoint(node.left, point, nearestPoint);
        nearestPoint = nearestPoint(node.right, point, nearestPoint);

        return nearestPoint;
    }

    private Point2D nearestPoint(Node node, Point2D point, Point2D nearestPoint) {
        if (node != null
                && node.rectangle.distanceTo(point) < nearestPoint.distanceTo(point)) {
            nearestPoint = nearest(node, point, nearestPoint);
        }

        return nearestPoint;
    }

    public static void main(String[] args) {
        KdTree ps = new KdTree();
        ps.insert(new Point2D(0.7, 0.2));
        ps.insert(new Point2D(0.5, 0.4));
        ps.insert(new Point2D(0.2, 0.3));
        ps.insert(new Point2D(0.4, 0.7));
        ps.insert(new Point2D(0.9, 0.6));

        StdOut.println(ps.contains(new Point2D(0.2, 0.3)));

        StdOut.println(ps.range(new RectHV(0, 0.2, 0.5, 0.5)));

        StdOut.println(ps.nearest(new Point2D(0.1, 0.1)));

        // StdOut.println(ps.range(new RectHV(0.2, 0.2, 0.6, 0.6)));
        // StdOut.println(ps.nearest(new Point2D(0.2, 0.4)));
        // ps.draw();
    }
}
