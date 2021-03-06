
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Jacky
 */
public class FastCollinearPoints {

    private final LineSegment[] segments;
    private Point[] endpoints;
    private int count;

    public FastCollinearPoints(Point[] inpoints) {    // finds all line segments containing 4 or more points
        if (inpoints.length == 0) throw new java.lang.NullPointerException();
        if (inpoints == null) throw new java.lang.NullPointerException();
        Point[] points = copy(inpoints);
        int N = points.length;
        count = 0;
        endpoints = new Point[4];
        Arrays.sort(points);
        for (int i = 0; i < N; i++) {
            if (points[i] == null) throw new java.lang.NullPointerException();
            Point[] sortedcopy = copy(points);
            Comparator<Point> p = points[i].slopeOrder();
            Arrays.sort(sortedcopy, p);
//            StdOut.println("Sorting" + points[i]);
            double tempslope = Double.NaN;
            int pointcount = 0;
            for (int j = 1; j < N; j++) {
                Point point = sortedcopy[j];
                if (point.compareTo(points[i]) == 0) throw new java.lang.IllegalArgumentException();
//                StdOut.println(point);
//                StdOut.println(points[i].slopeTo(point));
                double slope = points[i].slopeTo(point);
                if (slope != tempslope) {
//                    StdOut.println(pointcount);
                    if (pointcount >= 3) {
                        //check if it's already in there
                        boolean addPoint = true;
                        for (int k = 0; k < pointcount; k++) {
                            if (points[i].compareTo(sortedcopy[j - k - 1]) > 0) {
                                addPoint = false;
                                break;
                            }
                        }
                        if (addPoint) {
                            if (count * 2 == endpoints.length) {
                                resize(4 * endpoints.length);    // quadruple size of array if necessary
                            }
                            endpoints[count * 2] = points[i];
                            endpoints[count * 2 + 1] = sortedcopy[j - 1];
                            count++;
                        }
                    }
                    tempslope = slope;
                    pointcount = 1;
                } else if (j == N - 1) {
//                    StdOut.println(pointcount);
                    if (j == N - 1 && pointcount >= 2) {
                        //check if it's already in there
                        boolean addPoint = true;
                        for (int k = 0; k <= pointcount; k++) {
                            if (points[i].compareTo(sortedcopy[j - k]) > 0) {
                                addPoint = false;
                                break;
                            }
                        }
                        if (addPoint) {
                            if (count * 2 == endpoints.length) {
                                resize(4 * endpoints.length);    // quadruple size of array if necessary
                            }
                            endpoints[count * 2] = points[i];
                            endpoints[count * 2 + 1] = sortedcopy[j];
                            count++;
                        }
                    }
                    tempslope = slope;
                    pointcount = 1;
                } else {
                    pointcount++;
                }
            }
        }
        segments = new LineSegment[count];
        for (int i = 0; i < count; i++) {
            segments[i] = new LineSegment(endpoints[2 * i], endpoints[2 * i + 1]);
        }
    }

    public int numberOfSegments() {        // the number of line segments
        return count;
    }

    public LineSegment[] segments() {               // the line segments
        return segments.clone();
    }

    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= count * 2;
        Point[] temp = new Point[capacity];
        for (int i = 0; i < count * 2; i++) {
            temp[i] = endpoints[i];
        }
        endpoints = temp;
    }

    private static Point[] copy(Point[] points) {
        int N = points.length;
        Point[] newpoints = new Point[N];
        for (int i = 0; i < N; i++) {
            newpoints[i] = points[i];
        }
        return newpoints;
    }

    public static void main(String[] args) {
        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.show(0);
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}
