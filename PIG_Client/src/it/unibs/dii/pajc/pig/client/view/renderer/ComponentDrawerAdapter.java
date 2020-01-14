package it.unibs.dii.pajc.pig.client.view.renderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class ComponentDrawerAdapter implements ComponentDrawer {

    private Color bg = Color.WHITE, fg = Color.BLACK;

    @Override
    public void setBackground(Color bg) {
        this.bg = bg;
    }

    @Override
    public Color getBackground() {
        return bg;
    }

    @Override
    public void setForeground(Color fg) {
        this.fg = fg;
    }

    @Override
    public Color getForeground() {
        return fg;
    }

    @Override
    public Dimension getPlannedSize() {
        return null;
    }

    @Override
    public void draw(Graphics g, Point zeroPoint, float scaleFactor) {

    }

    @Override
    public String getTooltipText() {
        return null;
    }

    @Override
    public boolean isHover(Point coordinates) {
        return false;
    }

    @Override
    public void addDoubleClickListener(ActionListener lst) {

    }

    @Override
    public void removeDoubleClickListener(ActionListener lst) {

    }

    @Override
    public void fireDoubleClickAction(ActionEvent evt) {

    }

    @Override
    public Object getComponent() {
        return null;
    }
}
