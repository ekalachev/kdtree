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

        if (this.root == null) {
            RectHV rectangle = new RectHV(0, 0, 1, 1);
            this.root = new Node(point, rectangle);
            size++;

            return;
        }

        if (this.root.point.equals(point))
            return;

        RectHV rectangle;

        if (Point2D.X_ORDER.compare(this.root.point, point) > 0) {
            if (this.root.left == null) {
                rectangle = new RectHV(this.root.rectangle.xmin(), this.root.rectangle.ymin(),
                                       this.root.point.x(), this.root.rectangle.ymax());
                this.root.left = insertHorisontal(point, rectangle);
            }
            else {

            }
        }
        else {
            if (this.root.right == null) {
                rectangle = new RectHV(this.root.point.x(), this.root.rectangle.ymin(),
                                       this.root.rectangle.xmax(), this.root.rectangle.ymax());
                this.root.right = insertHorisontal(point, rectangle);
            }
            else {

            }
        }
    }

    private Node insertVertical(Point2D point, RectHV rectangle) {

    }

    private Node insertHorisontal(Point2D point, RectHV rectangle) {

    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();


    }

    // draw all points to standard draw
    public void draw() {

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();

        Point2D minPoint = new Point2D(rect.xmin(), rect.ymin());
        Point2D maxPoint = new Point2D(rect.xmax(), rect.ymax());


    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();

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
