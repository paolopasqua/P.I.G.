package it.unibs.dii.pajc.pig.client.view.renderer;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.device.emulated.EmulatedFan;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

public class FanRenderer extends ComponentDrawerAdapter {

    private Device component;
    private EventListenerList doubleClickListeners;
    private String tooltiptext = null;
    private EmulatedFan.FAN_STATUS status = null;
    private Point topLeft, bottomRight;
    private GeneralPath shape, statusShape;

    public FanRenderer(Device component) {
        this.component = component;
        doubleClickListeners = new EventListenerList();
        topLeft = new Point(0,0);
        bottomRight = new Point(0,0);
        shape = new GeneralPath();
        statusShape = new GeneralPath();
        initShapes();
    }

    private void initShapes() {
        shape.moveTo(0,0);
        shape.append(new Rectangle(0,0,50,50), false);
        shape.append(new Rectangle(1,1,48,48), false);

        shape.append(new Ellipse2D.Float(5, 5, 40, 40), false);
        shape.append(new Ellipse2D.Float(20, 20, 10, 10), false);

        //helix 1
        shape.moveTo(20.1, 24);
        shape.curveTo(20.1, 24, 18.5f, 23, 10, 11.77f);
        shape.moveTo(21.5f, 21.43f);
        shape.curveTo(21.5f, 21.43f, 25, 17, 18, 6.26f);

        //helix 2
        shape.moveTo(29, 22);
        shape.curveTo(29, 22, 32, 21, 39, 10.7f);
        shape.moveTo(29.8f, 23.6f);
        shape.curveTo(29.8f, 23.6f, 33, 30, 44.6f, 21);

        //helix 3
        shape.moveTo(25.8f, 29.9f);
        shape.curveTo(25.8f, 29.9f, 25, 31, 28, 44.77f);
        shape.moveTo(24, 29.97f);
        shape.curveTo(24, 29.97f, 18, 31, 16, 42.86f);
        //shape.closePath();

        //status
        statusShape.append(new Ellipse2D.Float(3, 3, 3, 3), false);
        statusShape.closePath();
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

        g2D.draw(shape.createTransformedShape(af));

        if (status != null && !status.equals(EmulatedFan.FAN_STATUS.OFF)) {
            g2D.setColor(Color.GREEN);
        } else {
            //Defaut: Fan OFF
            g2D.setColor(Color.RED);
        }
        g2D.fill(statusShape.createTransformedShape(af));

        topLeft.setLocation(zeroPoint);
        bottomRight.setLocation(zeroPoint.x + (int)(getPlannedSize().width*scaleFactor), zeroPoint.y + (int)(getPlannedSize().height*scaleFactor));
    }

    private Point getHelixDrawingPoint(int startX, int startY, int radius, int xShift, boolean northSouth) {
        int x, y, heightCalc;

        x = startX + xShift;
        heightCalc = (int)(Math.pow(radius,2) - Math.pow(radius-xShift,2));

        if (northSouth) {
            //south
            y = radius + heightCalc;
        } else {
            //north
            y = radius - heightCalc;
        }

        return new Point(x, y);
    }

    public EmulatedFan.FAN_STATUS getStatus() {
        return status;
    }

    public void setStatus(EmulatedFan.FAN_STATUS status) {
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
