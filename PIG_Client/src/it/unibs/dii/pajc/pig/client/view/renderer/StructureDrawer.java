package it.unibs.dii.pajc.pig.client.view.renderer;

import java.awt.*;

public interface StructureDrawer {

    /** Adds new component drawer to the structure **/
    void add(ComponentDrawer drawer);
    /** Removes the component drawer **/
    void remove(ComponentDrawer drawer);
    /** Removes all components drawer **/
    void removeAll();

    /** Returns the planned size of the component **/
    Dimension getPlannedSize();
    /** Graphics where draw, zero point up-left and the scale factor to apply **/
    void draw(Graphics g, Point zeroPoint, float scaleFactor);

}
