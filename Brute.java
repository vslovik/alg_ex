/**
 * Brute.java examines 4 points at a time and checks whether they all lie on the same line segment,
 * printing out any such line segments to standard output and drawing them using standard drawing.
 * To check whether the 4 points p, q, r, and s are collinear, checks whether the slopes
 * between p and q, between p and r, and between p and s are all equal.
 * The order of growth of the running time of your program should be N4 in the worst case and it
 * should use space proportional to N.
 */
public class Brute {

    private static void check(Point[] points) {
        double slop;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                slop = points[i].slopeTo(points[j]);
                for (int k = j + 1; k < points.length; k++) {
                    if (slop != points[i].slopeTo(points[k])) {
                        continue;
                    }
                    for (int m = k + 1; m < points.length; m++) {
                        if (slop == points[i].slopeTo(points[m])) {
                            Point[] segment = {points[i], points[j], points[k], points[m]};
                            Insertion.sort(segment);
                            print(segment);
                            draw(segment);
                        }
                    }
                }
            }
        }
    }

    /**
     * Draws the points using draw() and draw the line segments using drawTo().
     * Programs calls draw() once for each point in the input file and calls drawTo()
     * once for each line segment discovered.
     * Before drawing, use StdDraw.setXscale(0, 32768) and StdDraw.setYscale(0, 32768)
     * to rescale the coordinate system.
     *
     * @param segment Point[]
     */
    private static void draw(Point[] segment) {
        segment[0].drawTo(segment[3]);
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
     * % java Brute input6.txt
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
        while (!stdIn.isEmpty()) {
            int x = stdIn.readInt();
            int y = stdIn.readInt();
            Point p = new Point(x, y);
            points[i++] = p;
            p.draw();
        }
        check(points);

        // display to screen all at once
        StdDraw.show(0);

        // reset the pen radius
        StdDraw.setPenRadius();
    }
}
