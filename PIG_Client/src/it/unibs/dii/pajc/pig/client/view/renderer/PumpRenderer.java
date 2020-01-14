package it.unibs.dii.pajc.pig.client.view.renderer;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.device.emulated.EmulatedPump;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

public class PumpRenderer extends ComponentDrawerAdapter {

    private Device component;
    private EventListenerList doubleClickListeners;
    private String tooltiptext = null;
    private EmulatedPump.PUMP_STATUS status = null;
    private Point topLeft, bottomRight;
    private GeneralPath shape;
    private Shape drop1, drop2, drop3;

    public PumpRenderer(Device component) {
        this.component = component;
        doubleClickListeners = new EventListenerList();
        topLeft = new Point(0,0);
        bottomRight = new Point(0,0);
        shape = new GeneralPath();
        initShapes();
    }

    private void initShapes() {
        //50x30
        shape.moveTo(20,20);
        shape.append(new Ellipse2D.Float(21, 20, 8, 8), false);
        shape.append(new Rectangle2D.Float(24,28, 2, 22), false);

        GeneralPath drop = new GeneralPath();
        //5x5
        drop.moveTo(0,0);
        drop.curveTo(0,0, 2.2f,1, 2, 2.5f);
        drop.curveTo(2.5f,3, 3,5, 4.5f, 4);
        drop.curveTo(5,4, 5, 3, 4.5f, 2);
        drop.curveTo(4, 1.6f, 3.5f,0.4f, 2.8f, 0.8f);
        drop.curveTo(2, 0.6f, 1, 0.3f, 0.5f,0.1f);
        drop.lineTo(0,0);
        drop.closePath();

        AffineTransform af = new AffineTransform();
        af.translate(16, 18);
        af.scale(1.3, 1.3);
        af.rotate(-2.7);
        drop1 = drop.createTransformedShape(af);

        af = new AffineTransform();
        af.translate(25, 15);
        af.scale(1.3, 1.3);
        af.rotate(-2);
        drop2 = drop.createTransformedShape(af);

        af = new AffineTransform();
        af.translate(34, 18);
        af.scale(1.3, 1.3);
        af.rotate(-1.3);
        drop3 = drop.createTransformedShape(af);
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

        g2D.fill(shape.createTransformedShape(af));

        if (status != EmulatedPump.PUMP_STATUS.OFF) {

            g2D.setColor(Color.cyan);
            g2D.fill(af.createTransformedShape(drop1));
            g2D.setColor(getForeground());
            g2D.draw(af.createTransformedShape(drop1));

            if (status == EmulatedPump.PUMP_STATUS.POWER_2 || status == EmulatedPump.PUMP_STATUS.POWER_3) {
                g2D.setColor(Color.cyan);
                g2D.fill(af.createTransformedShape(drop2));
                g2D.setColor(getForeground());
                g2D.draw(af.createTransformedShape(drop2));

                if (status == EmulatedPump.PUMP_STATUS.POWER_3) {
                    g2D.setColor(Color.cyan);
                    g2D.fill(af.createTransformedShape(drop3));
                    g2D.setColor(getForeground());
                    g2D.draw(af.createTransformedShape(drop3));
                }
            }
        }

        topLeft.setLocation(zeroPoint);
        bottomRight.setLocation(zeroPoint.x + (int)(getPlannedSize().width*scaleFactor), zeroPoint.y + (int)(getPlannedSize().height*scaleFactor));
    }

    public EmulatedPump.PUMP_STATUS getStatus() {
        return status;
    }

    public void setStatus(EmulatedPump.PUMP_STATUS status) {
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
