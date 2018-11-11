package renderer;

import model.BufferedImageHelper;

public abstract class Renderer {
    protected final BufferedImageHelper img;

    public Renderer(BufferedImageHelper img) {
        this.img = img;
    }
}
