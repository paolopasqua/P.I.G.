package it.unibs.dii.pajc.pig.client.view.component.generalpurpouse;

import javax.swing.*;
import java.awt.*;

/**
 * Class to add a label on a JTextComponent.
 * The label can be put at North, West, South or East by the labelOrientation properties. Refer to LABEL_ORIENTATION enum for values.
 */
public class LabeledComponent extends JPanel {
    private JLabel labelComp;
    private JComponent component;
    private LABEL_ORIENTATION labelOrientation;

    private static final LABEL_ORIENTATION DEF_ORIENTATION = LABEL_ORIENTATION.WEST;

    public static final int ALGNMENT_CENTER = JLabel.CENTER;
    public static final int ALGNMENT_BOTTOM = JLabel.BOTTOM;
    public static final int ALGNMENT_RIGHT = JLabel.RIGHT;
    public static final int ALGNMENT_TOP = JLabel.TOP;
    public static final int ALGNMENT_LEFT = JLabel.LEFT;

    /**
     * Enum of labelOrientation property values of LabeledComponent class.
     */
    public enum LABEL_ORIENTATION {
        NORTH(BorderLayout.NORTH),
        WEST(BorderLayout.WEST),
        SOUTH(BorderLayout.SOUTH),
        EAST(BorderLayout.EAST);

        private Object value;
        private LABEL_ORIENTATION(Object _value) {
            value = _value;
        }

        public Object getValue() {
            return value;
        }
    }

    public LabeledComponent(JComponent component) {
        this(component, null);
    }

    public LabeledComponent(JComponent component, String label) {
        this(component, label, DEF_ORIENTATION);
    }

    public LabeledComponent(JComponent component, String label, LABEL_ORIENTATION label_orientation) {
        this.component = component;
        setLabelOrientation(label_orientation);
        setLabel(label);

        initComponent();
    }

    private void initComponent() {
        if (component != null) {
            this.setLayout(new BorderLayout());

            this.add(component, BorderLayout.CENTER);

            if (labelComp != null) {
                addLabel();
            }
        }
    }

    private void addLabel() {
        labelComp.setLabelFor(component);
        this.add(labelComp, labelOrientation.getValue());
    }

    public void setLabel(String label) {
        setLabel(label, labelOrientation);
    }

    public void setLabel(String label, LABEL_ORIENTATION label_orientation) {
        if (label != null) {
            if (labelComp == null) {
                labelComp = new JLabel();
                addLabel();
            }
            labelComp.setText(label);

            setLabelOrientation(label_orientation);
        }
        else if (labelComp != null){
            this.remove(labelComp);
            labelComp = null;
        }
    }

    public void setLabelHorizontalAlignment(int alignment) {
        labelComp.setHorizontalAlignment(alignment);
    }

    public void setLabelVerticalAlignment(int alignment) {
        labelComp.setVerticalAlignment(alignment);
    }

    public JLabel getLabel() {
        return labelComp;
    }

    public void setLabelOrientation(LABEL_ORIENTATION label_orientation) {
        if (label_orientation == null)
            label_orientation = DEF_ORIENTATION;

        this.labelOrientation = label_orientation;
        if (labelComp != null) {
            this.remove(labelComp);
            addLabel();
        }
    }

    public LABEL_ORIENTATION getLabelOrientation() {
        return labelOrientation;
    }
}
