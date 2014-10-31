/*************************************************************************
 * Name: Valeriya Slovikovskaya
 * Email: vslovik@gmail.com
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new BySlope();   // compare points by slope to this point

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    /**
     * The SLOPE_ORDER comparator should compare points by the slopes they make with the invoking point (x0, y0).
     * Formally, the point (x1, y1) is less than the point (x2, y2) if and only if the slope (y1 − y0) / (x1 − x0)
     * is less than the slope (y2 − y0) / (x2 − x0).
     * Treat horizontal, vertical, and degenerate line segments as in the slopeTo() method.
     */
    private class BySlope implements Comparator<Point> {

        public int compare(Point v, Point w) {
            double vSlope = slopeTo(v);
            double wSlope = slopeTo(w);
            if (vSlope < wSlope) return -1;
            if (vSlope > wSlope) return 1;
            return 0;
        }
    }

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point

    /**
     * The slopeTo() method should return the slope between the invoking point (x0, y0)
     * and the argument point (x1, y1), which is given by the formula (y1 − y0) / (x1 − x0).
     * Treat the slope of a horizontal line segment as positive zero;
     * treat the slope of a vertical line segment as positive infinity;
     * treat the slope of a degenerate line segment (between a point and itself)
     * as negative infinity.
     *
     * @param that Point
     * @return double
     */
    public double slopeTo(Point that) {
        if ((this.x == that.x) && (this.y == that.y)) return Double.NEGATIVE_INFINITY;
        if (this.x == that.x) return Double.POSITIVE_INFINITY;
        if (this.y == that.y) return 0.0;

        return (double) (that.y - this.y) / (double) (that.x - this.x);
    }

    /**
     * The compareTo() method should compare points by their y-coordinates,
     * breaking ties by their x-coordinates. Formally, the invoking point
     * (x0, y0) is less than the argument point (x1, y1) if and only if
     * either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that Point
     * @return int
     */
    public int compareTo(Point that) {
        if (this.y < that.y) return -1;
        if (this.y > that.y) return +1;
        if (this.x < that.x) return -1;
        if (this.x > that.x) return +1;
        return 0;
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) {
        // Test slopeTo
        Point p, q, r;
        //positive infinite slope, where p and q have coordinates in [0, 500)
        p = new Point(379, 445);
        q = new Point(379, 415);
        assert p.slopeTo(q) == Double.POSITIVE_INFINITY;
        // positive infinite slope, where p and q have coordinates in [0, 32768)
        p = new Point(30200, 23443);
        q = new Point(30200, 4472);
        assert p.slopeTo(q) == Double.POSITIVE_INFINITY;
        // negative infinite slope, where p and q have coordinates in [0, 500)
        p = new Point(270, 243);
        q = new Point(270, 243);
        assert p.slopeTo(q) == Double.NEGATIVE_INFINITY;
        // negative infinite slope, where p and q have coordinates in [0, 32768)

        p = new Point(6, 7);
        q = new Point(6, 7);
        assert p.slopeTo(q) == Double.NEGATIVE_INFINITY;

        p = new Point(29522, 31539);
        q = new Point(29522, 31539);
        assert p.slopeTo(q) == Double.NEGATIVE_INFINITY;
        // positive zero     slope, where p and q have coordinates in [0, 500)
        p = new Point(385, 292);
        q = new Point(340, 292);
        assert p.slopeTo(q) == 0.0;
        // positive zero     slope, where p and q have coordinates in [0, 32768)
        p = new Point(29714, 3463);
        q = new Point(17787, 3463);
        assert p.slopeTo(q) == 0.0;
        // symmetric  for random points p and q with coordinates in [0, 500)
        p = new Point(290, 197);
        q = new Point(290, 267);
        assert p.slopeTo(q) == Double.POSITIVE_INFINITY;
        assert q.slopeTo(p) == Double.POSITIVE_INFINITY;
        // symmetric  for random points p and q with coordinates in [0, 32768)
        p = new Point(28203, 22401);
        q = new Point(28203, 24932);
        assert p.slopeTo(q) == Double.POSITIVE_INFINITY;
        assert q.slopeTo(p) == Double.POSITIVE_INFINITY;

        p = new Point(227, 190);
        q = new Point(227, 3);
        assert p.slopeTo(q) == Double.POSITIVE_INFINITY;

        p = new Point(23965, 10672);
        q = new Point(23965, 7025);
        assert p.slopeTo(q) == Double.POSITIVE_INFINITY;

        p = new Point(3, 3);
        q = new Point(3, 1);
        assert p.slopeTo(q) == Double.POSITIVE_INFINITY;

        // Test compareTo

        p = new Point(321, 499);
        q = new Point(190, 386);
        r = new Point(438, 484);

        assert p.compareTo(q) == 1;
        assert p.compareTo(r) == 1;
        assert p.slopeTo(q) == 0.8625954198473282;
        assert p.slopeTo(r) == -0.1282051282051282;

        p = new Point(15779, 13978);
        q = new Point(5804, 17491);
        r = new Point(2249, 32476);

        assert p.compareTo(q) == 1;
        assert p.compareTo(r) == 1;
        assert p.slopeTo(q) == -0.35218045112781954;
        assert p.slopeTo(r) == -1.3671840354767184;

        p = new Point(3, 1);
        q = new Point(1, 0);
        r = new Point(9, 6);

        assert p.compareTo(q) == -1;
        assert p.compareTo(r) == -1;
        assert p.slopeTo(q) == 0.5;
        assert p.slopeTo(r) == 0.8333333333333334;

        // Test comparator

        p = new Point(368, 211);
        q = new Point(260, 63);
        r = new Point(368, 211);

        assert p.SLOPE_ORDER.compare(q, r) == 1;
        assert p.slopeTo(q) == 1.3703703703703705;
        assert p.slopeTo(r) == Double.NEGATIVE_INFINITY;

        StdOut.println(p.SLOPE_ORDER.compare(q, r));

        p = new Point(9, 6);
        q = new Point(2, 2);
        r = new Point(9, 6);

        assert p.SLOPE_ORDER.compare(q, r) == 1;
        assert p.slopeTo(q) == 0.5714285714285714;
        assert p.slopeTo(r) == Double.NEGATIVE_INFINITY;

    }
}
