package it.unibs.dii.pajc.pig.client.view.renderer;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

public class TempSensorRenderer extends ComponentDrawerAdapter {

    private static final int maxData = 100;
    private static final int minData = 0;

    private Sensor component;
    private EventListenerList doubleClickListeners;
    private String tooltiptext = null;
    private int data;
    private Point topLeft, bottomRight;
    private GeneralPath shape, dataShape;

    public TempSensorRenderer(Sensor component) {
        this.component = component;
        doubleClickListeners = new EventListenerList();
        topLeft = new Point(0,0);
        bottomRight = new Point(0,0);
        shape = new GeneralPath();
        dataShape = new GeneralPath();
        initShapes();
    }

    private void initShapes() {
        shape.moveTo(0,0);
        shape.append(new Rectangle(5,0,30, 50),false);

        shape.moveTo(12, 40); //3.5455339
        shape.curveTo(12,40, 13,44.5f, 17, 45);
        shape.curveTo(17,45, 21,44.5f, 22, 40);
        shape.lineTo(22,5);
        shape.lineTo(12, 5);
        shape.lineTo(12,40);

        for (int i = 0; i < 11; i++) {
            float offset = i*getDataSpacing()/10;
            shape.moveTo(12, 5 + offset);
            shape.lineTo(17,5 + offset);
        }

        dataShape.moveTo(0,0);
        dataShape.append(new Ellipse2D.Float(12, 35, 10,10), false);
        dataShape.append(new Rectangle2D.Float(12,35, 10, 5), false);
    }

    private float getDataSpacing() {
        return 30.0f;
    }

    @Override
    public Dimension getPlannedSize() {
        return new Dimension(40, 50); //30x50
    }

    @Override
    public void draw(Graphics g, Point zeroPoint, float scaleFactor) {
        Graphics2D g2D = (Graphics2D) g.create();

        AffineTransform af = new AffineTransform();
        af.translate(zeroPoint.x, zeroPoint.y);
        af.scale(scaleFactor,scaleFactor);

        g2D.setColor(Color.RED);
        GeneralPath dataShapeComplete = (GeneralPath) dataShape.clone();
        float yPosition = getDataSpacing() + 5 - (getDataSpacing()*getData()*1.0f/(maxData-minData));
        dataShapeComplete.append(new Rectangle2D.Float(12, yPosition, 10, getDataSpacing()-yPosition+5), false);
        g2D.fill(dataShapeComplete.createTransformedShape(af));

        g2D.setColor(getForeground());
        Shape scaledShape = shape.createTransformedShape(af);
        g2D.draw(shape.createTransformedShape(af));

        String text = getData() + "°C";
        FontRenderContext frc = new FontRenderContext(af,true,true);
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 64);
        while (font.getStringBounds(text, frc).getWidth() > 10*scaleFactor) {
            font = new Font(Font.MONOSPACED, Font.PLAIN, font.getSize()/2);
        }
        g2D.setFont(font);
        g2D.drawString(text, zeroPoint.x + 24*scaleFactor,zeroPoint.y + 5*scaleFactor + (int)font.getStringBounds(text, frc).getHeight());

        topLeft.setLocation(zeroPoint);
        bottomRight.setLocation(zeroPoint.x + (int)(getPlannedSize().width*scaleFactor), zeroPoint.y + (int)(getPlannedSize().height*scaleFactor));
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public void setTooltiptext(String tooltiptext) {
        this.tooltiptext = tooltiptext;
    }

    @Override
    public String getTooltipText() {
        return tooltiptext;
    }

    @Override
    public boolean isHover(Point coordinates) {
        if (coordinates.getX() >= topLeft.getX() && coordinates.getX() <= bottomRight.getX())
            if (coordinates.getY() >= topLeft.getY() && coordinates.getY() <= bottomRight.getY())
                return true;
        return false;
    }

    @Override
    public void addDoubleClickListener(ActionListener lst) {
        doubleClickListeners.add(ActionListener.class, lst);
    }

    @Override
    public void removeDoubleClickListener(ActionListener lst) {
        doubleClickListeners.remove(ActionListener.class, lst);
    }

    @Override
    public void fireDoubleClickAction(ActionEvent evt) {
        if (doubleClickListeners != null && doubleClickListeners.getListenerCount() > 0) {
            ActionListener[] listeners = doubleClickListeners.getListeners(ActionListener.class);
            for (ActionListener l : listeners) {
                l.actionPerformed(evt);
            }
        }
    }

    @Override
    public Object getComponent() {
        return component;
    }
}
