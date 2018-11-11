package renderer;

import model.BufferedImageHelper;
import model.Polygon;

public class PolygonRenderer extends Renderer {

    LineRenderer lr;

    public PolygonRenderer(BufferedImageHelper img) {
        super(img);

        lr = new LineRenderer(img);
    }

    public void draw(Polygon polygon) {
        draw(polygon, true);
    }

    public void draw(Polygon polygon, boolean connectLast) {
        // default color (yellow)
        draw(polygon, connectLast, 0xffff00);
    }

    public void draw(Polygon polygon, boolean connectLast, int color) {
        if (polygon.size() < 2)
            return;

        for (int i = 0; i < polygon.size() - ((connectLast) ? 0 : 1); i++) {
            lr.drawDDA(polygon.getAt(i), polygon.getAt((i + 1) % polygon.size()), color);
        }
    }
}
