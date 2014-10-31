/**
 * Represents a set of points in the unit square.
 * Implemened using a red-black BST (using either SET from algs4.jar or java.util.TreeSet).
 * Supports insert() and contains() in time proportional to the logarithm of the number
 * of points in the set in the worst case; it should support nearest() and range() in
 * time proportional to the number of points in the set.
 */
public class PointSET {

    private SET<Point2D> set;

    /**
     * Construct an empty set of points
     */
    public PointSET() {
        set = new SET<Point2D>();
    }

    /**
     * Is the set empty?
     *
     * @return boolean
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Number of points in the set
     *
     * @return int
     */
    public int size() {
        return set.size();
    }

    /**
     * Add the point to the set (if it is not already in the set)
     *
     * @param p Point
     */
    public void insert(Point2D p) {
        set.add(p);
    }

    /**
     * Does the set contain point p?
     *
     * @param p Point
     * @return boolean
     */
    public boolean contains(Point2D p) {
        return set.contains(p);
    }

    /**
     * Draw all points to standard draw
     */
    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    /**
     * All points that are inside the rectangle
     *
     * @param rect Rectangle
     * @return Iterable
     */
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> q = new Queue<Point2D>();
        for (Point2D p : set) {
            if (p.x() >= rect.xmin() && p.x() <= rect.xmax() && p.y() >= rect.ymin() && p.y() <= rect.ymax()) {
                q.enqueue(p);
            }
        }
        return q;
    }

    /**
     * A nearest neighbor in the set to point p; null if the set is empty
     *
     * @param p Point
     * @return Point
     */
    public Point2D nearest(Point2D p) {
        Point2D point = null;
        double d = Double.POSITIVE_INFINITY;
        double dd;
        for (Point2D next : set) {
            dd = next.distanceSquaredTo(p);
            if (dd < d) {
                point = next;
                d = dd;
            }
        }

        return point;
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
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }

        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        brute.draw();
    }
}
