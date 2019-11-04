package it.unibs.dii.pajc.pig.client.view.component.generalpurpouse;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;

public class GifComponent extends JComponent {

    public static enum GIF_ALIGNMENT { CENTER, TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT };

    private ImageIcon gif;
    private Dimension gifSize;
    private GIF_ALIGNMENT alignment;

    public GifComponent(String resourcePath) throws IOException {
        this(resourcePath, null);
    }

    public GifComponent(String resourcePath, int width, int height) throws IOException {
        this(resourcePath, new Dimension(width, height));
    }

    public GifComponent(String resourcePath, Dimension size) throws IOException {
        super();
        URL iconURl = getClass().getResource(resourcePath);
        gif = new ImageIcon(iconURl);

        alignment = GIF_ALIGNMENT.CENTER;

        setGifSize(size);
    }

    public GIF_ALIGNMENT getAlignment() {
        return alignment;
    }

    public void setAlignment(GIF_ALIGNMENT alignment) {
        this.alignment = alignment;
    }

    public void setGifSize(Dimension size) {
        this.gifSize = size;
    }

    public Dimension getGifSize() {
        return gifSize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g.create();
        double zoomX = getGifSize().getWidth() / gif.getIconWidth();
        double zoomY = getGifSize().getHeight() / gif.getIconHeight();
        Point where = getWhere();
        AffineTransform at = new AffineTransform();

        at.scale(zoomX, zoomY);
        g2d.setTransform(at);
        gif.paintIcon(this, g2d, (int)(where.x/zoomX), (int)(where.y/zoomY));
    }

    private Point getWhere() {
        Point p = new Point(0, 0);
        Dimension gifSize = getGifSize();
        Dimension size = getSize();

        if (gifSize == null)
            gifSize = size;

        switch (alignment) {
            case TOP:
                //y = 0
                p.x = (size.width - gifSize.width)/2;
                break;
            case RIGHT:
                p.y = (size.height - gifSize.height)/2;
            case TOP_RIGHT:
                //y = 0
                p.x = (size.width - gifSize.width);
                break;
            case LEFT:
                //x = 0
                p.y = (size.height - gifSize.height)/2;
                break;
            case BOTTOM:
                p.x = (size.width - gifSize.width)/2;
            case BOTTOM_LEFT:
                //x = 0
                p.y = (size.height - gifSize.height);
                break;
            case BOTTOM_RIGHT:
                p.y = (size.height - gifSize.height);
                p.x = (size.width - gifSize.width);
                break;
            case CENTER:
                p.y = (size.height - gifSize.height)/2;
                p.x = (size.width - gifSize.width)/2;
                break;
            case TOP_LEFT:
                //x = 0 ; y = 0
                break;
        }

        return p;
    }
}
