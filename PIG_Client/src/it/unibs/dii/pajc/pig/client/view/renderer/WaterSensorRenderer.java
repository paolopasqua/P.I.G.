package it.unibs.dii.pajc.pig.client.view.renderer;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

public class WaterSensorRenderer extends ComponentDrawerAdapter {

    private static final int maxData = 100;
    private static final int minData = 0;

    private Sensor component;
    private EventListenerList doubleClickListeners;
    private String tooltiptext = null;
    private int data;
    private Point topLeft, bottomRight;
    private GeneralPath shape;

    public WaterSensorRenderer(Sensor component) {
        this.component = component;
        doubleClickListeners = new EventListenerList();
        topLeft = new Point(0,0);
        bottomRight = new Point(0,0);
        shape = new GeneralPath();
        initShapes();
    }

    private void initShapes() {
        shape.moveTo(0,0);
        shape.append(new Rectangle2D.Float(5,5,30,45), false);

        for(int i = 0; i < 3; i++) {
            shape.moveTo(5, 20+15*i);
            shape.lineTo(35, 20+15*i);
        }
    }

    private float getDataSpacing() {
        return 45.0f;
    }

    @Override
    public Dimension getPlannedSize() {
        return new Dimension(40, 50); //40x50
    }

    @Override
    public void draw(Graphics g, Point zeroPoint, float scaleFactor) {
        Graphics2D g2D = (Graphics2D) g.create();

        AffineTransform af = new AffineTransform();
        af.translate(zeroPoint.x, zeroPoint.y);
        af.scale(scaleFactor,scaleFactor);

        g2D.setColor(Color.CYAN);
        GeneralPath dataShape = new GeneralPath();
        float yPosition = getDataSpacing() + 5 - (getDataSpacing()*getData()*1.0f/(maxData-minData));
        dataShape.append(new Rectangle2D.Float(5, yPosition, 30, getDataSpacing()-yPosition+5), false);
        g2D.fill(dataShape.createTransformedShape(af));

        g2D.setColor(getForeground());
        Shape scaledShape = shape.createTransformedShape(af);
        g2D.draw(shape.createTransformedShape(af));

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
