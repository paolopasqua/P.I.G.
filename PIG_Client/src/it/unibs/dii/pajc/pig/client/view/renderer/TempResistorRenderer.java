package it.unibs.dii.pajc.pig.client.view.renderer;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.device.emulated.EmulatedTempResistor;

import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

public class TempResistorRenderer extends ComponentDrawerAdapter {

    private Device component;
    private EventListenerList doubleClickListeners;
    private String tooltiptext = null;
    private EmulatedTempResistor.TEMP_RESISTOR_STATUS status = null;
    private Point topLeft, bottomRight;
    private GeneralPath shape, statusON;

    public TempResistorRenderer(Device component) {
        this.component = component;
        doubleClickListeners = new EventListenerList();
        topLeft = new Point(0,0);
        bottomRight = new Point(0,0);
        shape = new GeneralPath();
        statusON = new GeneralPath();
        initShapes();
    }

    private void initShapes() {
        shape.moveTo(2, 30);
        shape.lineTo(5, 27);
        shape.lineTo(13, 27);
        shape.lineTo(17, 27);
        shape.lineTo(25, 27);
        shape.lineTo(28, 30);
        shape.closePath();

        for (int i = 0; i < 3; i++){
            int offset = i*8;
            statusON.moveTo(7+offset, 20);
            statusON.curveTo(5+offset, 17, 3+offset, 14, 3+offset, 13);
            statusON.curveTo(3+offset, 13, 4+offset, 10, 5+offset, 6);
            statusON.lineTo(6+offset, 6);
            statusON.moveTo(7+offset, 20);
            statusON.lineTo(8+offset, 20);
            statusON.curveTo(6+offset, 17, 4+offset, 14, 4+offset, 13);
            statusON.curveTo(4+offset, 13, 5+offset, 10, 6+offset, 6);
        }
    }

    @Override
    public Dimension getPlannedSize() {
        return new Dimension(30, 30);/*30x30*/
    }

    @Override
    public void draw(Graphics g, Point zeroPoint, float scaleFactor) {
        Graphics2D g2D = (Graphics2D) g.create();

        AffineTransform af = new AffineTransform();
        af.translate(zeroPoint.x, zeroPoint.y);
        af.scale(scaleFactor,scaleFactor);

        Shape scaledShape = shape.createTransformedShape(af);

        g2D.setColor(getForeground());

        g2D.draw(scaledShape);

        if (status != null && status.equals(EmulatedTempResistor.TEMP_RESISTOR_STATUS.ON)) {
            g2D.setColor(new Color(255, 0, 0, 80));
            g2D.fill(statusON.createTransformedShape(af));
        }

        topLeft.setLocation(zeroPoint);
        bottomRight.setLocation(zeroPoint.x + (int)(getPlannedSize().width*scaleFactor), zeroPoint.y + (int)(getPlannedSize().height*scaleFactor));
    }

    public EmulatedTempResistor.TEMP_RESISTOR_STATUS getStatus() {
        return status;
    }

    public void setStatus(EmulatedTempResistor.TEMP_RESISTOR_STATUS status) {
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
