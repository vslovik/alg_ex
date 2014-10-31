/**
 * The insert(), contains(), and nearest() methods in KdTree are passed points with x- and y-coordinates between 0 and 1.
 * You may also assume that the range() method in KdTree is passed a rectangle that lies in the unit box.
 * <p/>
 * Performance:
 * <p/>
 * Squared distances.
 * Whenever you need to compare two Euclidean distances, it is often more efficient to compare the
 * squares of the two distances to avoid the expensive operation of taking square roots. Everyone should implement
 * this optimization because it is both easy to do and likely a bottleneck.
 * <p/>
 * Range search
 * Instead of checking whether the query rectangle intersects the rectangle corresponding to a node,
 * it suffices to check only whether the query rectangle intersects the splitting line segment: if it does,
 * then recursively search both subtrees; otherwise, recursively search the one subtree where points intersecting
 * the query rectangle could be.
 * <p/>
 * Save memory
 * You are not required to explictily store a RectHV in each 2d-tree node (though it is probably wise in your first version).
 */
public class KdTree {

    private Node root;
    private int size = 0;

    /**
     * Node data type.
     * There are several reasonable ways to represent a node in a 2d-tree.
     * One approach is to include the point, a link to the left/bottom subtree,
     * a link to the right/top subtree, and an axis-aligned rectangle corresponding
     * to the node.
     * <p/>
     * Unlike the Node class for BST, this Node class is static because it does not
     * refer to a generic Key or Value type that depends on the object associated
     * with the outer class.
     * <p/>
     * This saves the 8-byte inner class object overhead.
     * (Making the Node class static in BST is also possible if you make the Node type
     * itself generic as well). Also, since we don't need to implement the rank and
     * select operations, there is no need to store the subtree size.
     */
    private static class Node {
        private Point2D p;      // the point
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, Node lb, Node rt) {
            this.p = p;
            this.lb = lb;
            this.rt = rt;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private int size(Node node) {
        int left = 0;
        int right = 0;
        if (node.lb != null)
            left = size(node.lb);
        if (node.rt != null)
            right = size(node.rt);
        return left + right + 1;
    }

    // Note that insert() and contains() are best implemented by using private helper methods
    // analogous to those found on page 399 of the book or by looking at BST.java
    // We recommend using orientation as an argument to these helper methods.

    /**
     * Does everything except set up the RectHV for each node.
     *
     * @param p Point2D
     */
    public void insert(Point2D p) {
        root = insert(root, p, true);
    }

    private Node insert(Node n, Point2D p, boolean v) {

        Node x = n;
        if (x == null) {
            x = new Node(null, null, null);
            size += 1;
        }

        if (x.p == null) {
            x.p = p;
            return x;
        }
        if (x.p.equals(p)) {
            return x;
        }
        if (v) {
            if (p.x() < x.p.x()) {
                x.lb = insert(x.lb, p, !v);
            } else {
                x.rt = insert(x.rt, p, !v);
            }
        } else {
            if (p.y() < x.p.y()) {
                x.lb = insert(x.lb, p, !v);
            } else {
                x.rt = insert(x.rt, p, !v);
            }
        }
        return x;
    }

    /**
     * Use this to test that insert() was implemented properly
     *
     * @param p Point2D
     * @return boolean
     */
    public boolean contains(Point2D p) {
        return contains(root, p, true);
    }

    private boolean contains(Node x, Point2D p, boolean v) {
        if (x == null) return false;
        if (x.p.equals(p))
            return true;
        if (v) {
            if (p.x() < x.p.x()) return contains(x.lb, p, !v);
            else return contains(x.rt, p, !v);
        } else {
            if (p.y() < x.p.y()) return contains(x.lb, p, !v);
            else return contains(x.rt, p, !v);
        }
    }

    // <---- Now add the code to insert() which sets up the RectHV for each Node.
    // Save memory. You are not required to explicitly store a RectHV in each 2d-tree node (though it is probably wise in your first version).

    public void draw() {
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
        rect.draw();
        draw(root, rect, true);
    }

    /**
     * use this to test these rectangles
     */
    private void draw(Node node, RectHV rect, boolean v) {
        // before drawing the points;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        if (v)
            StdDraw.setPenColor(StdDraw.RED);
        else
            StdDraw.setPenColor(StdDraw.BLUE);

        if (node.p != null) {
            node.p.draw();
            StdDraw.setPenRadius();
            if (v)
                StdDraw.line(node.p.x(), rect.ymin(), node.p.x(), rect.ymax());
            else
                StdDraw.line(rect.xmin(), node.p.y(), rect.xmax(), node.p.y());

            if (node.lb != null)
                draw(node.lb, getNodeRect(rect, node.p, true, v), !v);

            if (node.rt != null)
                draw(node.rt, getNodeRect(rect, node.p, false, v), !v);
        }
    }

    /**
     * Range search. To find all points contained in a given query rectangle,
     * start at the root and recursively search for points in both subtrees
     * using the following pruning rule:
     *
     * if the query rectangle does not
     * intersect the rectangle corresponding to a node,
     * there is no need to
     * explore that node (or its subtrees). A subtree is searched only if it
     * might contain a point contained in the query rectangle.
     *
     */


    /**
     * Range search
     * Instead of checking whether the query rectangle intersects the rectangle corresponding to a node,
     * it suffices to check only whether the query rectangle intersects the splitting line segment: if it does,
     * then recursively search both subtrees; otherwise, recursively search the one subtree where points intersecting
     * the query rectangle could be.
     *
     * @param rect Rectangle
     * @return Iterable<Point2D>
     */
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> q = new Queue<Point2D>();
        range(root, rect, q, true);
        return q;
    }

    /**
     * Range
     *
     * @param rect Rectangle
     */
    private void range(Node node, RectHV rect, Queue<Point2D> q, boolean v) {
        if (node == null || node.p == null)
            return;

        if (rect.contains(node.p))
            q.enqueue(node.p);

        if ((v && rect.xmax() >= node.p.x()) || (!v && rect.ymax() >= node.p.y()))
            range(node.rt, rect, q, !v);

        if ((v && rect.xmin() <= node.p.x()) || (!v && rect.ymin() <= node.p.y()))
            range(node.lb, rect, q, !v);
    }

    /**
     * Nearest neighbor search. To find a closest point to a given query point,
     * start at the root and recursively search in both subtrees using the
     * following pruning rule:
     *
     * if the closest point discovered so far is closer than
     * the distance between the query point and the rectangle corresponding to a node,
     * there is no need to explore that node (or its subtrees).
     * That is, a node is searched only if it might contain a point that is closer
     * than the best one found so far.
     *
     * The effectiveness of the pruning rule depends on quickly finding a nearby point.
     * To do this, organize your recursive method so that when there are two possible
     * subtrees to go down, you always choose the subtree that is on the same side of
     * the splitting line as the query point as the first subtree to explore—the closest
     * point found while exploring the first subtree may enable pruning of the second
     * subtree.
     */

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     *
     * @param p Point
     * @return Point
     */
    public Point2D nearest(Point2D p) {
        if (root == null)
            return null;
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        return nearest(root, p, root.p, rect, true);
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     *
     * @param p Point
     * @return Point
     */
    private Point2D nearest(Node node, Point2D p, Point2D nearest, RectHV rect, boolean v) {
        double d;
        RectHV r;
        Point2D point = nearest;
        d = point.distanceSquaredTo(p);
        if (node.p != null) {
            Node first, second;
            boolean firstLb, secondLb;

            if ((v && p.x() < node.p.x()) || (!v && p.y() < node.p.y())) {
                first = node.lb;
                firstLb = true;
                second = node.rt;
                secondLb = false;
            } else {
                first = node.rt;
                firstLb = false;
                second = node.lb;
                secondLb = true;
            }

            if (first != null) {
                r = getNodeRect(rect, node.p, firstLb, v);
                if (r.distanceSquaredTo(p) < d) {
                    point = nearest(first, p, point, r, !v);
                    d = point.distanceSquaredTo(p);
                }
            }

            if (second != null) {
                r = getNodeRect(rect, node.p, secondLb, v);
                if (r.distanceSquaredTo(p) < d) {
                    point = nearest(second, p, point, r, !v);
                    d = point.distanceSquaredTo(p);
                }
            }

            if (node.p.distanceSquaredTo(p) < d) {
                point = node.p;
            }
        }

        return point;
    }


    /**
     * Gets left bottom rect
     *
     * @param rect RectHV
     * @param p    Point2D
     * @param v    If vertical
     * @return RectHV
     */
    private RectHV getNodeRect(RectHV rect, Point2D p, boolean lb, boolean v) {
        RectHV r;
        if (lb) {
            if (v)
                r = new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax());
            else
                r = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
        } else {
            if (v)
                r = new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax());
            else
                r = new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
        }
        return r;
    }

    /**
     * Unit testing of the methods (optional)
     *
     * @param args Arguments
     */
    public static void main(String[] args) {

        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.setPenRadius(.001);

        String filename = args[0];
        In in = new In(filename);

        // initialize the data structures with N points from standard input
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        kdtree.draw();


        Point2D q = new Point2D(0.81, 0.30);
        kdtree.nearest(q).draw();
        q = new Point2D(0.206107, 0.095492);
        StdOut.println(kdtree.contains(q));
        q = new Point2D(0.975528, 0.654508);
        StdOut.println(kdtree.contains(q));
        q = new Point2D(0.024472, 0.345492);
        StdOut.println(kdtree.contains(q));
        q = new Point2D(0.793893, 0.095492);
        StdOut.println(kdtree.contains(q));
        q = new Point2D(0.793893, 0.904508);
        StdOut.println(kdtree.contains(q));
        q = new Point2D(0.975528, 0.345492);
        StdOut.println(kdtree.contains(q));
        q = new Point2D(0.206107, 0.904508);
        StdOut.println(kdtree.contains(q));
        q = new Point2D(0.500000, 0.000000);
        StdOut.println(kdtree.contains(q));
        q = new Point2D(0.024472, 0.654508);
        StdOut.println(kdtree.contains(q));
        q = new Point2D(0.500000, 1.000000);
        StdOut.println(kdtree.contains(q));

        StdOut.println(kdtree.size());
    }

}
