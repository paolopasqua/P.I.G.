package it.unibs.dii.pajc.pig.client.view.renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class GreenhouseRenderer extends JComponent implements StructureDrawer {

    /*
    ____________________________________
    |       |                           |
    | TOP   |        DEFAULT            |
    |  S    |___________________________|
    |  E    |                           |
    |  N    |       TOP DEVICE          |
    |  O    |                           |
    |  R    |                           |
    |___________________________________|
    |       |                           |
    | B S   |                           |
    | O E   |                           |
    | T N   |                           !
    | T O   |     BOTTOM DEVICE         |
    | O R   |                           |
    | M     |                           !
    |___________________________________|

     */

    public static final double DRAWING_DEVICE_TOP_HEIGHT_USED = 50.0;
    public static final double DRAWING_DEVICE_TOP_WIDTH_USED = 40.0;
    public static final double DRAWING_DEVICE_BOTTOM_WIDTH_USED = 80.0;
    public static final double DRAWING_GREENHOUSE_BASE_WIDTH = 10.0;
    public static final double DRAWING_GREENHOUSE_ROOF_WIDTH = 50.0;

    public static final int DEFAULT_DEVICE_TOP_ZONES_SPACING = 10;
    public static final int SENSOR_DEVICE_ZONES_SPACING = 10;

    private ArrayList<ComponentDrawer> sensorTopZone, sensorBottomZone, deviceTopZone, deviceBottomZone, defaultZone;
    private Dimension sensorTopSize, sensorBottomSize, deviceTopSize, deviceBottomSize, defaultSize;
    private Point drawPointSensorTop, drawPointSensorBottom, drawPointDeviceTop, drawPointDeviceBottom, drawPointDefault;

    public GreenhouseRenderer() {
        sensorTopZone = new ArrayList<>();
        sensorBottomZone = new ArrayList<>();
        deviceTopZone = new ArrayList<>();
        deviceBottomZone = new ArrayList<>();
        defaultZone = new ArrayList<>();

        sensorTopSize = new Dimension(0, 0);
        sensorBottomSize = new Dimension(0,0);
        deviceTopSize = new Dimension(0,0);
        deviceBottomSize = new Dimension(0,0);
        defaultSize = new Dimension(0,0);

        drawPointSensorTop = new Point(0, 0);
        drawPointDefault = new Point(0 ,0);
        drawPointSensorBottom = new Point(0, 0);
        drawPointDeviceTop = new Point(0, 0);
        drawPointDeviceBottom = new Point(0, 0);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                setComponentTooltipText(e);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() >= 2) {
                    fireComponentDoubleClick(e);
                }
            }
        });
    }

    @Override
    public void add(ComponentDrawer drawer) {
        if (isSensorTopZone(drawer)) {
            sensorTopZone.add(drawer);
            recalcZoneSize(sensorTopZone, sensorTopSize, false);
        }
        else if (isSensorBottomZone(drawer)) {
            sensorBottomZone.add(drawer);
            recalcZoneSize(sensorBottomZone, sensorBottomSize, false);
        }
        else if (isDeviceTopZone(drawer)) {
            deviceTopZone.add(drawer);
            recalcZoneSize(deviceTopZone, deviceTopSize, true);
        }
        else if (isDeviceBottomZone(drawer)) {
            deviceBottomZone.add(drawer);
            recalcZoneSize(deviceBottomZone, deviceBottomSize, true);
        }
        else {
            defaultZone.add(drawer);
            recalcZoneSize(defaultZone, defaultSize, true);
        }
    }

    @Override
    public void remove(ComponentDrawer drawer) {
        if (isSensorTopZone(drawer)) {
            sensorTopZone.remove(drawer);
            recalcZoneSize(sensorTopZone, sensorTopSize, false);
        }
        else if (isSensorBottomZone(drawer)) {
            sensorBottomZone.remove(drawer);
            recalcZoneSize(sensorBottomZone, sensorBottomSize, false);
        }
        else if (isDeviceTopZone(drawer)) {
            deviceTopZone.remove(drawer);
            recalcZoneSize(deviceTopZone, deviceTopSize, true);
        }
        else if (isDeviceBottomZone(drawer)) {
            deviceBottomZone.remove(drawer);
            recalcZoneSize(deviceBottomZone, deviceBottomSize, true);
        }
        else {
            defaultZone.remove(drawer);
            recalcZoneSize(defaultZone, defaultSize, true);
        }
    }

    @Override
    public void removeAll() {
        super.removeAll();
        sensorTopZone.clear();
        sensorBottomZone.clear();
        deviceTopZone.clear();
        deviceBottomZone.clear();
        defaultZone.clear();
    }

    @Override
    public Dimension getPlannedSize() {
        int drawingDeviceTopScaledHeight = getDrawingDeviceTopResizedHeight();
        int drawingDeviceTopScaledWidth = getDrawingDeviceTopResizedWidth();
        int drawingDeviceBottomScaledWidth = getDrawingDeviceBottomResizedWidth();
        //declare temp width and height
        //start width value: sensor top zone width
        //start height value: default zone height + device top zone height
        int width = sensorTopSize.width, height = defaultSize.height + drawingDeviceTopScaledHeight + DEFAULT_DEVICE_TOP_ZONES_SPACING;


        //if sensor top zone height is bigger than current height then overwrites
        if (height < sensorTopSize.height)
            height = sensorTopSize.height;
        //adds the biggest between sensor bottom zone height and device bottom zone height
        height += Math.max(sensorBottomSize.height, deviceBottomSize.height);
        //end height logic

        //if sensor bottom zone width is bigger than current width then overwrites
        if (width < sensorBottomSize.width)
            width = sensorBottomSize.width;
        //adds the biggest between defaul zone width, device top zone width and device bottom zone width
        width += Math.max(defaultSize.width, Math.max(drawingDeviceTopScaledWidth, drawingDeviceBottomScaledWidth)) + SENSOR_DEVICE_ZONES_SPACING;
        //end width logic

        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension plannedSize = getPlannedSize();
        float scaleFactor = getSize().height * 1.0f / plannedSize.height;

        if ((getSize().width * 1.0f / plannedSize.width) < scaleFactor)
            scaleFactor = (getSize().width * 1.0f / plannedSize.width);

        Point zeroPoint = new Point(0,0);
        zeroPoint.y = (int)Math.ceil((getSize().height - plannedSize.height*scaleFactor)/2);
        zeroPoint.x = (int)Math.ceil((getSize().width - plannedSize.width*scaleFactor)/2);

        draw(g, zeroPoint, scaleFactor);
    }

    @Override
    public void draw(Graphics g, Point zeroPoint, float scaleFactor) {
        Dimension plannedSize = getPlannedSize();
        int sensorZoneWidth = Math.max(sensorTopSize.width, sensorBottomSize.width);
        int rightZoneWidth = plannedSize.width - sensorZoneWidth - SENSOR_DEVICE_ZONES_SPACING;
        int drawingDeviceTopResizedHeight = getDrawingDeviceTopResizedHeight();

        int deviceTopZoneTop = defaultSize.height + DEFAULT_DEVICE_TOP_ZONES_SPACING + (drawingDeviceTopResizedHeight - deviceTopSize.height);
        //start drawing at sensor width plus zone empty space plus empty space by resizing to center the devices
        int deviceTopZoneLeft = sensorZoneWidth + SENSOR_DEVICE_ZONES_SPACING + (int)Math.ceil((rightZoneWidth*(100-DRAWING_DEVICE_TOP_WIDTH_USED)/100)/2) + (int)Math.ceil((rightZoneWidth*DRAWING_DEVICE_TOP_WIDTH_USED/100 - deviceTopSize.width)/2);

        int deviceBottomZoneLeft = sensorZoneWidth + SENSOR_DEVICE_ZONES_SPACING + (int)Math.ceil((rightZoneWidth*(100-DRAWING_DEVICE_BOTTOM_WIDTH_USED)/100)/2);

        Point greenhouseTop = new Point(), greenhouseLeft = new Point(), greenhouseRight = new Point();
        greenhouseTop.x = zeroPoint.x + (int)Math.ceil((sensorZoneWidth + SENSOR_DEVICE_ZONES_SPACING + rightZoneWidth*DRAWING_GREENHOUSE_ROOF_WIDTH/100.0)*scaleFactor);
        greenhouseTop.y = zeroPoint.y + (int)Math.ceil((defaultSize.getHeight() + DEFAULT_DEVICE_TOP_ZONES_SPACING)*scaleFactor);

        greenhouseLeft.x = zeroPoint.x + (int)Math.ceil((sensorZoneWidth + SENSOR_DEVICE_ZONES_SPACING + rightZoneWidth*DRAWING_GREENHOUSE_BASE_WIDTH/100.0)*scaleFactor);
        greenhouseLeft.y = greenhouseTop.y + (int)Math.ceil(drawingDeviceTopResizedHeight*scaleFactor);

        greenhouseRight.x = zeroPoint.x + (int)Math.ceil((sensorZoneWidth + SENSOR_DEVICE_ZONES_SPACING + rightZoneWidth - rightZoneWidth*DRAWING_GREENHOUSE_BASE_WIDTH/100.0)*scaleFactor);
        greenhouseRight.y = greenhouseLeft.y;

        g.setColor(Color.WHITE);
        g.fillRect(zeroPoint.x, zeroPoint.y, (int)Math.ceil(plannedSize.width*scaleFactor), (int)Math.ceil(plannedSize.height*scaleFactor));
        g.setColor(Color.BLACK);

        Point drawingPoint = new Point();
        /*DRAW SENSOR TOP ZONE*/
        drawingPoint.x = zeroPoint.x;
        drawingPoint.y = zeroPoint.y;
        drawPointSensorTop.setLocation(drawingPoint);
        for(ComponentDrawer c : sensorTopZone) {
            if (isComponentToDraw(c)) {
                //Dimension scaledPlannedSize = new Dimension((int) (c.getPlannedSize().width * scaleFactor), (int) (c.getPlannedSize().height * scaleFactor));
                c.draw(g, drawingPoint, scaleFactor);
                drawingPoint.y += c.getPlannedSize().height * scaleFactor;
            }
        }

        /*DRAW SENSOR BOTTOM ZONE*/
        drawingPoint.x = zeroPoint.x;
        drawingPoint.y = zeroPoint.y + (int)Math.floor(plannedSize.height * scaleFactor);
        drawPointSensorBottom.setLocation(drawingPoint.x, drawingPoint.y - sensorBottomSize.height*scaleFactor);
        for(ComponentDrawer c : sensorBottomZone) {
            if (isComponentToDraw(c)) {
                //Dimension scaledPlannedSize = new Dimension((int) (c.getPlannedSize().width * scaleFactor), (int) (c.getPlannedSize().height * scaleFactor));
                drawingPoint.y -= c.getPlannedSize().height * scaleFactor;
                c.draw(g, drawingPoint, scaleFactor);
            }
        }

        /*DRAW DEFAULT ZONE*/
        drawingPoint.x = zeroPoint.x + (int)Math.floor((sensorZoneWidth+SENSOR_DEVICE_ZONES_SPACING)*scaleFactor);
        drawingPoint.y = zeroPoint.y;
        drawPointDefault.setLocation(drawingPoint);
        for(ComponentDrawer c : defaultZone) {
            if (isComponentToDraw(c)) {
                //Dimension scaledPlannedSize = new Dimension((int) (c.getPlannedSize().width * scaleFactor), (int) (c.getPlannedSize().height * scaleFactor));
                c.draw(g, drawingPoint, scaleFactor);
                drawingPoint.x += c.getPlannedSize().width * scaleFactor;
            }
        }

        /*DRAW DEVICE TOP ZONE*/
        drawingPoint.x = zeroPoint.x + (int)Math.floor(deviceTopZoneLeft*scaleFactor);
        drawingPoint.y = zeroPoint.y + (int)Math.floor(deviceTopZoneTop * scaleFactor);
        drawPointDeviceTop.setLocation(drawingPoint);
        for(ComponentDrawer c : deviceTopZone) {
            if (isComponentToDraw(c)) {
                //Dimension scaledPlannedSize = new Dimension((int) (c.getPlannedSize().width * scaleFactor), (int) (c.getPlannedSize().height * scaleFactor));
                c.draw(g, drawingPoint, scaleFactor);
                drawingPoint.x += c.getPlannedSize().width * scaleFactor;
            }
        }
        g.drawLine(greenhouseLeft.x,greenhouseLeft.y,greenhouseTop.x,greenhouseTop.y);
        g.drawLine(greenhouseRight.x,greenhouseRight.y,greenhouseTop.x,greenhouseTop.y);
        g.drawLine(zeroPoint.x + (int)Math.floor(deviceTopZoneLeft*scaleFactor), zeroPoint.y + (int)Math.floor(deviceTopZoneTop*scaleFactor), zeroPoint.x + (int)Math.floor((deviceTopZoneLeft+deviceTopSize.width)*scaleFactor), zeroPoint.y + (int)Math.floor(deviceTopZoneTop*scaleFactor));

        /*DRAW DEVICE BOTTOM ZONE*/
        drawingPoint.x = zeroPoint.x + (int)Math.floor(deviceBottomZoneLeft*scaleFactor);
        drawingPoint.y = zeroPoint.y + (int)Math.floor(plannedSize.height * scaleFactor);
        drawPointDeviceBottom.setLocation(drawingPoint.x, drawingPoint.y - deviceBottomSize.height*scaleFactor);
        for(ComponentDrawer c : deviceBottomZone) {
            if (isComponentToDraw(c)) {
                //Dimension scaledPlannedSize = new Dimension((int) (c.getPlannedSize().width * scaleFactor), (int) (c.getPlannedSize().height * scaleFactor));
                drawingPoint.y -= c.getPlannedSize().height * scaleFactor;
                c.draw(g, drawingPoint, scaleFactor);
                drawingPoint.x += c.getPlannedSize().width * scaleFactor;
                drawingPoint.y += c.getPlannedSize().height * scaleFactor;
            }
        }
        g.drawLine(zeroPoint.x+(int)Math.floor((sensorZoneWidth+SENSOR_DEVICE_ZONES_SPACING)*scaleFactor),drawingPoint.y,greenhouseLeft.x,greenhouseLeft.y);
        g.drawLine(zeroPoint.x+(int)Math.floor((plannedSize.width)*scaleFactor),drawingPoint.y,greenhouseRight.x,greenhouseRight.y);
    }

    private boolean isComponentToDraw (ComponentDrawer c) {
        if (c != null && c.getPlannedSize() != null)
            return true;
        else
            return false;
    }

    private void fireComponentDoubleClick(MouseEvent e) {
        ComponentDrawer c = getComponentByPoint(e.getPoint());
        if (c != null) {
            ActionEvent evt = new ActionEvent(c.getComponent() == null ? c : c.getComponent(), ActionEvent.ACTION_PERFORMED, null, e.getWhen(), 0);
            c.fireDoubleClickAction(evt);
        }
    }

    private void setComponentTooltipText(MouseEvent e) {
        ComponentDrawer c = getComponentByPoint(e.getPoint());
        if (c != null)
            setToolTipText(c.getTooltipText());
        else
            setToolTipText(null);
    }

    private ComponentDrawer getComponentByPoint(Point coordinates) {
        ArrayList<ComponentDrawer> zone = getZoneByPoint(coordinates);
        for (ComponentDrawer c : zone) {
            if (isComponentToDraw(c) && c.isHover(coordinates)) {
                return c;
            }
        }
        return null;
    }

    private ArrayList<ComponentDrawer> getZoneByPoint(Point coordinates) {
        if (coordinates.getY() >= drawPointSensorBottom.getY()) {
            //SENSOR BOTTOM ZONE or DEVICE BOTTOM ZONE
            if (coordinates.getX() >= drawPointDeviceBottom.getX()) {
                //DEVICE BOTTOM ZONE
                return deviceBottomZone;
            } else {
                //SENSOR BOTTOM ZONE
                return sensorBottomZone;
            }
        } else {
            //SENSOR TOP ZONE or DEFAULT ZONE or DEVICE TOP ZONE
            if (coordinates.getX() >= drawPointDeviceTop.getX()) {
                //DEVICE TOP ZONE
                return deviceTopZone;
            } else if (coordinates.getX() >= drawPointDefault.getX()) {
                //DEFAULT ZONE
                return defaultZone;
            } else {
                //SENSOR TOP ZONE
                return sensorTopZone;
            }
        }
    }

    private int getDrawingDeviceTopResizedHeight() {
        //drawing structure: half of the device top zone is empty in height for drawing
        return (int)Math.ceil(deviceTopSize.getHeight() * 100.0/DRAWING_DEVICE_TOP_HEIGHT_USED);
    }

    private int getDrawingDeviceTopResizedWidth() {
        //drawing structure: only 40% in width of device top zone is for the devices drawing
        return (int)Math.ceil(deviceTopSize.getWidth() * 100.0/DRAWING_DEVICE_TOP_WIDTH_USED);
    }

    private int getDrawingDeviceBottomResizedWidth() {
        //drawing structure: only 80% in width of device bottom zone is for the devices drawing
        return (int)Math.ceil(deviceBottomSize.getWidth() * 100.0/DRAWING_DEVICE_BOTTOM_WIDTH_USED);
    }

    private boolean isSensorTopZone(ComponentDrawer drawer) {
        if (drawer instanceof TempSensorRenderer)
            return true;
        return false;
    }

    private boolean isSensorBottomZone(ComponentDrawer drawer) {
        if (drawer instanceof WaterSensorRenderer)
            return true;
        return false;
    }

    private boolean isDeviceTopZone(ComponentDrawer drawer) {
        if (drawer instanceof LampRenderer)
            return true;
        return false;
    }

    private boolean isDeviceBottomZone(ComponentDrawer drawer) {
        if (drawer instanceof PumpRenderer)
            return true;
        if (drawer instanceof TempResistorRenderer)
            return true;
        return false;
    }

    /*
    * DEFAULT ZONE:
    * FanRenderer
    **/

    private void recalcZoneSize(ArrayList<ComponentDrawer> zone, Dimension size, boolean appendOriz) {
        size.setSize(0,0);
        int height = 0, width = 0;
        for (ComponentDrawer d: zone) {
            if (isComponentToDraw(d)) {
                height += d.getPlannedSize().getHeight();
                width += d.getPlannedSize().getWidth();

                //se appendo in verticale e la lunghezza del componente attuale e' maggiore della dimensione allora la imposto come nuova lunghezza
                if (!appendOriz && d.getPlannedSize().getWidth() > size.getWidth())
                    size.setSize(d.getPlannedSize().getWidth(), 0);
                    //se appendo in orizzontale e l'altezza del componente attuale e' maggiore della dimensione allora la imposto come nuova altezza
                else if (appendOriz && d.getPlannedSize().getHeight() > size.getHeight())
                    size.setSize(0, d.getPlannedSize().getHeight());
            }
        }

        //se appendo in verticale aggiorno l'altezza calcolata
        if (!appendOriz)
            size.setSize(size.getWidth(), height);
        //se appendo in orizzontale aggiorno la lunghezza calcolata
        else
            size.setSize(width,size.getHeight());
    }
}
