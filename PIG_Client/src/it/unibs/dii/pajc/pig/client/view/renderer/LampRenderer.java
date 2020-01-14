package it.unibs.dii.pajc.pig.client.view.renderer;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.device.emulated.EmulatedLamp;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

public class LampRenderer extends ComponentDrawerAdapter {

    private Device component;
    private EventListenerList doubleClickListeners;
    private String tooltiptext = null;
    private EmulatedLamp.LAMP_STATUS status = null;
    private Point topLeft, bottomRight;
    private GeneralPath shape, statusON;

    public LampRenderer(Device component) {
        this.component = component;
        doubleClickListeners = new EventListenerList();
        topLeft = new Point(0,0);
        bottomRight = new Point(0,0);
        shape = new GeneralPath();
        statusON = new GeneralPath();
        initShapes();
    }

    private void initShapes() {
        shape.moveTo(25, 0);
        shape.lineTo(20, 5);
        shape.lineTo(30, 5);
        shape.lineTo(25,0);

        shape.moveTo(20,5);
        shape.lineTo(20,15);
        shape.moveTo(30,5);
        shape.lineTo(30,15);

        for (int i = 1; i <= 10; i++) {
            shape.moveTo(20, 5+i);
            shape.lineTo(30, 5+i);
        }

        shape.lineTo(50, 45);
        shape.lineTo(0, 45);
        shape.lineTo(20, 15);

        shape.moveTo(23, 15);
        shape.lineTo(14, 45);
        shape.moveTo(25,15);
        shape.lineTo(25, 45);
        shape.moveTo(27,15);
        shape.lineTo(36,45);

        shape.closePath();


        statusON.moveTo(11, 0);
        for (int i = 0; i < 5; i++) {
            statusON.lineTo(2+i, 5);
            statusON.lineTo(12+i,0);
        }

        statusON.moveTo(21, 0);
        for (int i = 0; i < 5; i++) {
            statusON.lineTo(16+i, 5);
            statusON.lineTo(22+i,0);
        }

        statusON.moveTo(31, 0);
        for (int i = 0; i < 5; i++) {
            statusON.lineTo(36-i, 5);
            statusON.lineTo(30-i,0);
        }

        statusON.moveTo(39, 0);
        for (int i = 0; i < 5; i++) {
            statusON.lineTo(48-i, 5);
            statusON.lineTo(38-i,0);
        }
        statusON.closePath();
    }

    @Override
    public Dimension getPlannedSize() {
        return DEFAULT_SIZE;/*50x50*/
    }

    @Override
    public void draw(Graphics g, Point zeroPoint, float scaleFactor) {
        Graphics2D g2D = (Graphics2D) g.create();

        AffineTransform af = new AffineTransform();
        af.translate(zeroPoint.x, zeroPoint.y);
        af.scale(scaleFactor,scaleFactor);

        g2D.setColor(getForeground());

        Shape scaledShape = shape.createTransformedShape(af);
        g2D.draw(scaledShape);

        if (status != null && status.equals(EmulatedLamp.LAMP_STATUS.ON)) {
            g2D.setColor(Color.MAGENTA);
            af = new AffineTransform();
            af.translate(zeroPoint.x, zeroPoint.y + scaledShape.getBounds().height);
            af.scale(scaleFactor, scaleFactor);
            g2D.draw(statusON.createTransformedShape(af));
        }

        topLeft.setLocation(zeroPoint);
        bottomRight.setLocation(zeroPoint.x + (int)(getPlannedSize().width*scaleFactor), zeroPoint.y + (int)(getPlannedSize().height*scaleFactor));
    }

    public EmulatedLamp.LAMP_STATUS getStatus() {
        return status;
    }

    public void setStatus(EmulatedLamp.LAMP_STATUS status) {
        this.status = status;
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
