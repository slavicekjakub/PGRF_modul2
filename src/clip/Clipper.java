package clip;

import model.Edge;
import model.Point;
import model.Polygon;

public class Clipper {
    Polygon clArea;
    Polygon out;

    /**
     * Create new Clipper.
     *
     * @param clippingArea
     */
    public Clipper(Polygon clippingArea) {
        this.clArea = clippingArea;

        out = new Polygon();
    }

    /**
     * Clip in polygon with clipping area.
     * @param in polygon for clipping
     * @return clipped polygon
     */
    public Polygon clip(Polygon in) {
        Polygon temp = new Polygon(in.getPoints());

        for (int i = 0; i < clArea.size(); i++) {
            out.clear();
            Edge edge = new Edge(clArea.getAt(i), clArea.getAt((i + 1) % clArea.size()));

            if (temp.size() > 0) {
                Point v1 = temp.getAt(temp.size() - 1);
                for (Point v2 : temp.getPoints()) {
                    if (edge.isInside(v2)) {
                        if (!edge.isInside(v1))
                            out.add(edge.getIntersection(v1, v2));
                        out.add(v2);
                    } else {
                        if (edge.isInside(v1))
                            out.add(edge.getIntersection(v1, v2));
                    }
                    v1 = v2;
                }

                // copy out to temp
                temp.clear();
                for (Point point : out.getPoints()) {
                    temp.add(point);
                }
            }
        }

        return out;
    }
}
