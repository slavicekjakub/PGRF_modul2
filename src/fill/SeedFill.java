package fill;

import model.BufferedImageHelper;
import renderer.Renderer;

public class SeedFill extends Renderer{
    private int color;

    private int background;
    private int borderColor;

    private int purple = 0xa642f7;
    private int orange = 0xffca59;

    // pattern for filling
    // 10 x 10
    private int[][] pattern = {{orange, orange, purple, purple, purple, purple, purple, purple, orange, orange},
            {purple, orange, orange, purple, purple, purple, purple, orange, orange, purple},
            {purple, purple, orange, orange, purple, purple, orange, orange, purple, purple},
            {purple, purple, purple, orange, orange, orange, orange, purple, purple, purple},
            {purple, purple, purple, orange, orange, orange, orange, purple, purple, purple},
            {purple, purple, purple, orange, orange, orange, orange, purple, purple, purple},
            {purple, purple, orange, orange, purple, purple, orange, orange, purple, purple},
            {purple, orange, orange, purple, purple, purple, purple, orange, orange, purple},
            {orange, orange, purple, purple, purple, purple, purple, purple, orange, orange},
            {orange, orange, purple, purple, purple, purple, purple, purple, orange, orange}};


    public SeedFill(BufferedImageHelper img) {
        super(img);
    }

    /**
     * Fill area with default color.
     *
     * @param x
     * @param y
     * @param background
     */
    public void fill(int x, int y, int background) {
        fill(x, y, 0x42f4df, background);
    }

    /**
     * Fill area with custom color.
     *
     * @param x
     * @param y
     * @param color
     * @param background
     */
    public void fill(int x, int y, int color, int background) {
        this.color = color;
        this.background = background;

        fill(x, y);
    }

    private void fill(int x, int y) {
        if(img.getRGB(x, y) == background && img.getRGB(x, y) != borderColor) {
            img.setRGB(x, y, color);
            fill(x, y+1);
            fill(x, y-1);
            fill(x+1, y);
            fill(x-1, y);
        }
    }

    /**
     * Fill area with pattern.
     *
     * @param x
     * @param y
     * @param background
     */
    public void fillPattern(int x, int y, int background) {
        this.background = background;

        fillPattern(x, y);
    }

    /**
     * Fill position with pattern.
     * Only for recursion.
     *
     * @param x
     * @param y
     */
    private void fillPattern(int x, int y) {
        color = pattern[(x % pattern.length)][(y % pattern.length)];
        if(img.getRGB(x, y) == background && img.getRGB(x, y) != borderColor) {
            img.setRGB(x, y, color);
            fillPattern(x, y+1);
            fillPattern(x, y-1);
            fillPattern(x+1, y);
            fillPattern(x-1, y);
        }
    }

    public void setBackground(int background) {
        this.background = background;
    }


    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }
}
