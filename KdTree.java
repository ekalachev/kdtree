/* *****************************************************************************
 *  Name: KdTree.java
 *  Date: 02/28/2019
 *  Description: A mutable data type PointSET.java that represents a set of
 *               points in the unit square.
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
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

        insertHorisontal(this.root, point, this.root.rectangle);
    }

    private void insertHorisontal(Node node, Point2D point, RectHV rectangle) {
        if (node.point.equals(point))
            return;

        if (Point2D.X_ORDER.compare(node.point, point) > 0) {
            left(node, rectangle, point, true);
        }
        else {
            right(node, rectangle, point, true);
        }
    }

    private void insertVertical(Node node, Point2D point, RectHV rectangle) {
        if (node.point.equals(point))
            return;

        if (Point2D.Y_ORDER.compare(node.point, point) > 0) {
            left(node, rectangle, point, false);
        }
        else {
            right(node, rectangle, point, false);
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
            if (horisontal)
                insertVertical(node.right, point, node.right.rectangle);
            else
                insertHorisontal(node.right, point, node.right.rectangle);
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
            if (horisontal)
                insertVertical(node.left, point, node.left.rectangle);
            else
                insertHorisontal(node.left, point, node.left.rectangle);
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

        return containsVertical(this.root, p);
    }

    private boolean containsVertical(Node node, Point2D p) {
        if (node == null)
            return false;

        if (node.point.equals(p))
            return true;

        if (node.left != null && node.left.rectangle.xmax() >= p.x())
            return containsHorisontal(node.left, p);
        else
            return containsHorisontal(node.right, p);
    }

    private boolean containsHorisontal(Node node, Point2D p) {
        if (node == null)
            return false;

        if (node.point.equals(p))
            return true;

        if (node.left != null && node.left.rectangle.ymax() >= p.y())
            return containsVertical(node.left, p);
        else
            return containsVertical(node.right, p);
    }

    // draw all points to standard draw
    public void draw() {

    }

    // // all points that are inside the rectangle (or on the boundary)
    // public Iterable<Point2D> range(RectHV rect) {
    //     if (rect == null)
    //         throw new IllegalArgumentException();
    //
    //     Point2D minPoint = new Point2D(rect.xmin(), rect.ymin());
    //     Point2D maxPoint = new Point2D(rect.xmax(), rect.ymax());
    //
    //
    // }
    //
    // // a nearest neighbor in the set to point p; null if the set is empty
    // public Point2D nearest(Point2D p) {
    //     if (p == null)
    //         throw new IllegalArgumentException();
    //
    // }

    public static void main(String[] args) {
        KdTree ps = new KdTree();
        ps.insert(new Point2D(0.7, 0.2));
        ps.insert(new Point2D(0.5, 0.4));
        ps.insert(new Point2D(0.2, 0.3));
        ps.insert(new Point2D(0.4, 0.7));
        ps.insert(new Point2D(0.9, 0.6));

        StdOut.println(ps.contains(new Point2D(0.4, 0.7)));


        // StdOut.println(ps.range(new RectHV(0.2, 0.2, 0.6, 0.6)));
        // StdOut.println(ps.nearest(new Point2D(0.2, 0.4)));
        // ps.draw();
    }
}
