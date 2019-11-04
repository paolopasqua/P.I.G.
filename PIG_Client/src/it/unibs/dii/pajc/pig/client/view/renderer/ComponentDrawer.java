package it.unibs.dii.pajc.pig.client.view.renderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public interface ComponentDrawer {

    static Dimension DEFAULT_SIZE = new Dimension(50,50);

    /** Returns the planned size of the component **/
    Dimension getPlannedSize();
    /** Graphics where draw, zero point up-left and the scale factor to apply **/
    void draw(Graphics g, Point zeroPoint, float scaleFactor);
    /** Returns the tooltip text to view on screen **/
    String getTooltipText();
    /** Check if the coordinates are hover the component drawer **/
    boolean isHover(Point coordinates);

    /** Adds the action listener **/
    void addDoubleClickListener(ActionListener lst);
    /** Removes the action listener **/
    void removeDoubleClickListener(ActionListener lst);
    /** Launches the double click event **/
    void fireDoubleClickAction(ActionEvent evt);
    /** Returns the component drawed **/
    Object getComponent();
}
