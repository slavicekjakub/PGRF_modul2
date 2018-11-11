package fill;

import java.util.ArrayList;
import java.util.List;

import model.BufferedImageHelper;
import model.Edge;
import model.Point;
import model.Polygon;
import renderer.LineRenderer;
import renderer.PolygonRenderer;
import renderer.Renderer;

public class ScanLine extends Renderer {
    private List<Edge> edges;
    private List<Integer> intersections;
    private LineRenderer lr;
    private PolygonRenderer pr;

    /**
     * Create new ScanLine.
     * @param img
     */
    public ScanLine(BufferedImageHelper img) {
        super(img);
        edges = new ArrayList<>();
        intersections = new ArrayList<>();

        lr = new LineRenderer(img);
        pr = new PolygonRenderer(img);
    }

    /**
     * Fill polygon with default color.
     * @param polygon
     */
    public void fill(Polygon polygon) {
        fill(polygon, 0x992266);
    }

    /**
     * Fill polygon with custom color.
     * @param polygon
     * @param color
     */
    public void fill(Polygon polygon, int color) {
        edges.clear();

        int yMin = (int) polygon.getAt(0).getY();
        int yMax = (int) polygon.getAt(0).getY();

        // all edges
        for(int i = 0; i < polygon.size(); i++) {
            Point a = polygon.getAt(i);
            Point b = polygon.getAt((i + 1) % polygon.size());

            // set borders
            if(a.getY() < yMin)
                yMin = (int) a.getY();
            if(a.getY() > yMax)
                yMax = (int) a.getY();

            Edge edge = new Edge(a, b);

            if(!edge.isHorizontal()) {
                edges.add(edge.getOriented());
            }
        }

        // from min to max
        for(int y = yMin; y <= yMax; y++) {
            intersections.clear();

            for (Edge edge : edges) {
                if(edge.isIntersection(y)) {
                    intersections.add(edge.getIntersection(y));
                }
            }

            // sort intersections
            sort();

            for (int x = 0; x < intersections.size()-1; x = x+2) {
                lr.drawDDA(new Point(intersections.get(x), y), new Point(intersections.get(x+1), y), color);
            }
        }

        // highlight borders
        pr.draw(polygon);
    }

    /**
     * Sort intersections with insertion sort.
     */
    private void sort() {
        for(int i=1;i<intersections.size();i++){
            int temp = intersections.get(i);
            for(int j= i-1;j>=0;j--){
                if(temp<intersections.get(j)){
                    intersections.set(j+1,intersections.get(j));
                    if(j==0)
                        intersections.set(0, temp);
                }else{
                    intersections.set(j+1,temp);
                    break;
                }
            }
        }
    }
}
