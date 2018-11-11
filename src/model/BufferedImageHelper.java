package model;

import java.awt.image.BufferedImage;

public class BufferedImageHelper extends BufferedImage {

    public BufferedImageHelper(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    @Override
    public synchronized void setRGB(int x, int y, int rgb) {
        // now I can move out of window (and no Exceptions)
        if (x > 0 && y > 0 && x < this.getWidth() && y < this.getHeight())
            super.setRGB(x, y, rgb);
    }

    @Override
    public int getRGB(int x, int y) {
        // only get rgb from defined area
        if (x > 0 && y > 0 && x < this.getWidth() && y < this.getHeight())
            return super.getRGB(x, y);

        return 0;
    }

}
