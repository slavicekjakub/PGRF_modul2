package model;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private List<Point> points = new ArrayList<>();

    public Polygon(List<Point> points) {
        this.points = points;
    }

    public Polygon(Polygon polygon) {
        this(polygon.points);
    }

    public Polygon() {
    }

    public void add(Point point) {
        points.add(point);
    }

    public void remove(Point point) {
        points.remove(point);
    }

    public Point getAt(int index) {
        if (index < 0 || index >= points.size())
            throw new IndexOutOfBoundsException();

        return points.get(index);
    }

    public int size() {
        return points.size();
    }

    public void clear() {
        points.clear();
    }

    public List<Point> getPoints() {
        return points;
    }
}
