package renderer;

import model.BufferedImageHelper;
import model.Point;

public class LineRenderer extends Renderer {
    public LineRenderer(BufferedImageHelper img) {
        super(img);
    }

    public void drawDDA(Point firstPoint, Point secondPoint) {
        // using default color (yellow)
        drawDDA(firstPoint, secondPoint, 0xffff00);
    }

    public void drawDDA(Point firstPoint, Point secondPoint, int lineColor) {
        float k, x, y;

        k = ((secondPoint.getY() - firstPoint.getY()) / (secondPoint.getX() - firstPoint.getX()));

        // switch points
        if ((Math.abs(k) <= 1 && firstPoint.getX() > secondPoint.getX())
                || (Math.abs(k) > 1 && firstPoint.getY() > secondPoint.getY())) {
            Point temp = firstPoint;
            firstPoint = secondPoint;
            secondPoint = temp;
        }

        x = firstPoint.getX();
        y = firstPoint.getY();

        if (Math.abs(k) <= 1) {
            do {
                img.setRGB((int) x, (int) y, lineColor);
                x = x + 1;
                y = y + k;
            } while (x <= secondPoint.getX());
        } else if (Math.abs(k) > 1) {
            do {
                img.setRGB((int) x, (int) y, lineColor);
                x = (x + 1 / k);
                y = y + 1;
            } while (y <= secondPoint.getY());
        }
    }
}
