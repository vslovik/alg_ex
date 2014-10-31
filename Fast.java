import java.util.Arrays;

/**
 * Examines 4 points at a time and checks whether they all lie on the same line segment, printing out any
 * such line segments to standard output and drawing them using standard drawing
 * <p/>
 * Given a point p, the following method determines whether p participates in a set of 4 or more collinear points.
 * Think of p as the origin.
 * For each other point q, determine the slope it makes with p.
 * Sort the points according to the slopes they makes with p.
 * Check if any 3 (or more) adjacent points in the sorted order have equal slopes with respect to p.
 * If so, these points, together with p, are collinear.
 * <p/>
 * The algorithm solves the problem because points that have equal slopes with respect to p are collinear,
 * and sorting brings such points together. The algorithm is fast because the bottleneck operation is sorting.
 * <p/>
 * The order of growth of the running time of your program should be N2 log N
 * in the worst case and it should use space proportional to N.
 */
public class Fast {

    private static class Pair implements Comparable<Pair> {
        private Point start;
        private Point end;

        public Pair(Point start, Point end) {
            this.start = start;
            this.end = end;
        }

        public int compareTo(Pair that) {
            int cmp = this.start.compareTo(that.start);
            if (cmp == 0) {
                return this.end.compareTo(that.end);
            }
            return cmp;
        }
    }

    /**
     * Does not print permutations of points on a line segment
     * (e.g., if outputs p→q→r→s, does not also output either s→r→q→p or p→r→q→s).
     * <p/>
     * Does not print or plot subsegments of a line segment containing 5 or more points
     * (e.g., if outputs p→q→r→s→t, does not also output either p→q→s→t or q→r→s→t);
     * you should print out such subsegments in Brute.java.
     * <p/>
     * Arrays.sort(a, lo, hi) sorts the subarray from a[lo] to a[hi-1] according to
     * the natural order of a[]. You can use a Comparator as the fourth argument
     * to sort according to an alternate order.
     *
     * @param points Point[]
     */
    private static void check(Point[] points) {
        int count, start;
        double prev, slope;
        SET<Pair> set = new SET<Pair>();
        Point[] sample = new Point[points.length];

        for (int i = 0; i < points.length; i++) {
            sample[i] = points[i];
        }

        for (int i = 0; i < points.length; i++) {
            Arrays.sort(sample, points[i].SLOPE_ORDER);
            prev = sample[0].slopeTo(sample[1]);
            count = 0;
            start = 0;
            for (int j = 1; j < sample.length; j++) {
                slope = sample[0].slopeTo(sample[j]);
                if (j != 1 && slope == prev) {
                    if (count == 0) {
                        start = j - 1;
                        count = 2;
                    } else count += 1;
                }
                if (start > 0 && slope != prev) {
                    show(sample, set, count, start);
                    start = 0;
                    count = 0;
                }
                prev = slope;
            }
            show(sample, set, count, start);
        }
    }

    /**
     * Shows segment
     *
     * @param sample Point[]
     * @param set    SET<Pair>
     * @param count  int
     * @param start  int
     */
    private static void show(Point[] sample, SET<Pair> set, int count, int start) {
        Point[] segment;
        Pair pair;
        if (count > 2) {
            segment = new Point[count + 1];
            for (int k = start; k < start + count; k++)
                segment[k - start] = sample[k];
            segment[count] = sample[0];
            Insertion.sort(segment);
            pair = new Pair(segment[0], segment[count - 1]);
            if (!set.contains(pair)) {
                print(segment);
                draw(segment);
            }
            set.add(pair);
        }
    }

    /**
     * Draws the points using draw() and draw the line segments using drawTo().
     * Programs calls draw() once for each point in the input file and calls drawTo()
     * once for each line segment discovered.
     * Before drawing, use StdDraw.setXscale(0, 32768) and StdDraw.setYscale(0, 32768)
     * to rescale the coordinate system.
     * <p/>
     * Draws the line segment p→q→r→s, with either p.drawTo(s) or s.drawTo(p).
     *
     * @param segment Point[]
     */
    private static void draw(Point[] segment) {
        segment[0].drawTo(segment[segment.length - 1]);
    }

    private static void print(Point[] segment) {
        String str = "";
        for (int i = 0; i < segment.length; i++) {
            if (i > 0) {
                str += " -> ";
            }
            str += segment[i].toString();
        }
        System.out.println(str);
    }

    /**
     * Takes the name of an input file as a command-line argument,
     * reads the input file, prints to standard output the line
     * segments discovered, and draws to standard draw the line
     * segments discovered.
     * <p/>
     * Input format. Read the points from an input file in the following format:
     * An integer N, followed by N pairs of integers (x, y), each between
     * 0 and 32,767. Below are two examples.
     * <p/>
     * % more input6.txt       % more input8.txt
     * 6                       8
     * 19000  10000             10000      0
     * 18000  10000                 0  10000
     * 32000  10000              3000   7000
     * 21000  10000              7000   3000
     * 1234   5678             20000  21000
     * 14000  10000              3000   4000
     * 14000  15000
     * 6000   7000
     * <p/>
     * Output format. Print to standard output the line segments that your program
     * discovers, one per line. Print each line segment as an ordered sequence of
     * its constituent points, separated by " -> ".
     * <p/>
     * % java Fast input6.txt
     * (14000, 10000) -> (18000, 10000) -> (19000, 10000) -> (21000, 10000)
     * (14000, 10000) -> (18000, 10000) -> (19000, 10000) -> (32000, 10000)
     * (14000, 10000) -> (18000, 10000) -> (21000, 10000) -> (32000, 10000)
     * (14000, 10000) -> (19000, 10000) -> (21000, 10000) -> (32000, 10000)
     * (18000, 10000) -> (19000, 10000) -> (21000, 10000) -> (32000, 10000)
     *
     * @param args String[]
     */
    public static void main(String[] args) {
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);
        StdDraw.setPenRadius(0.01);

        In stdIn = new In(args[0]);
        int N = stdIn.readInt();
        Point[] points = new Point[N];
        int i = 0;
        while (!stdIn.isEmpty() && i < N) {
            int x = stdIn.readInt();
            int y = stdIn.readInt();
            Point p = new Point(x, y);
            points[i] = p;
            i++;
            p.draw();
        }
        if (points.length > 3) check(points);
        StdDraw.show(0);
        StdDraw.setPenRadius();
    }
}
