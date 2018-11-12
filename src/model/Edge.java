package model;

public class Edge {
    private final Point a;
    private final Point b;

    public Edge(Point a, Point b) {
        this.a = a;
        this.b = b;
    }

    public Point getA() {
        return a;
    }

    public Point getB() {
        return b;
    }

    public boolean isHorizontal() {
        return ((int)a.getY() == (int)b.getY());
    }

    public boolean isIntersection(int y) {
        return (y >= a.getY() && y < b.getY());
    }

    public Edge getOriented() {
        if(a.getY() < b.getY()) {
            return new Edge(a, b);
        } else {
            return new Edge(b, a);
        }
    }

    public int getIntersection(int y) {
        float k = ((b.getX() - a.getX()) / (b.getY() - a.getY()));
        float q = a.getX() - (k * a.getY());

        return (int)((k * y) + q);
    }

    public Point getIntersection(Point p1, Point p2) {
        double denominator = (
                (((p1.getX() - p2.getX())
                        *
                        (a.getY() - b.getY()))
                        -
                        ((p1.getY() - p2.getY())
                                *
                                (a.getX() - b.getX())))
        );

        double x = (
                (((p1.getX() * p2.getY()) - (p2.getX() * p1.getY()))
                        *
                        (a.getX() - b.getX()))
                        -
                        (((a.getX() * b.getY()) - (b.getX() * a.getY()))
                                *
                                (p1.getX() - p2.getX()))
        )
                /
                denominator;

        double y = (
                (((p1.getX() * p2.getY()) - (p2.getX() * p1.getY()))
                        *
                        (a.getY() - b.getY()))
                        -
                        (((a.getX() * b.getY()) - (b.getX() * a.getY()))
                                *
                                (p1.getY() - p2.getY()))
        )
                /
                denominator;

        return new Point((float)x, (float)y);
    }


    public boolean isInside(Point point) {
        double result = (
                (point.getX() - a.getX())
                        *
                        (-(b.getY() - a.getY()))
        )
                +
                (
                        (point.getY() - a.getY())
                                *
                                (b.getX() - a.getX())
                );

        return (result <= 0);
    }

    @Override
    public String toString() {
        return "A: " + a + " | B: " + b;
    }
}
