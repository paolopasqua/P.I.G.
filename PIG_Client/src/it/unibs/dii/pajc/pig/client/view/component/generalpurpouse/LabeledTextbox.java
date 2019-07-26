package it.unibs.dii.pajc.pig.client.view.component.generalpurpouse;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Class to add a label on a JTextComponent.
 * The label can be put at North, West, South or East by the labelOrientation properties. Refer to LABEL_ORIENTATION enum for values.
 */
public class LabeledTextbox extends JPanel {
    private JLabel labelComp;
    private JTextComponent textComp;
    private LABEL_ORIENTATION labelOrientation;

    private static final LABEL_ORIENTATION DEF_ORIENTATION = LABEL_ORIENTATION.WEST;

    /**
     * Enum of labelOrientation property values of LabeledTextbox class.
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

    public LabeledTextbox(JTextComponent textbox) {
        this(textbox, null);
    }

    public LabeledTextbox(JTextComponent textbox, String label) {
        this(textbox, label, DEF_ORIENTATION);
    }

    public LabeledTextbox(JTextComponent textbox, String label, LABEL_ORIENTATION label_orientation) {
        textComp = textbox;
        setLabel(label);
        setLabelOrientation(label_orientation);

        initComponent();
    }

    private void initComponent() {
        if (textComp != null) {
            this.setLayout(new BorderLayout());

            this.add(textComp, BorderLayout.CENTER);

            if (labelComp != null) {
                labelComp.setLabelFor(textComp);
                this.add(labelComp, labelOrientation.getValue());
            }
        }
    }

    public void setLabel(String label) {
        setLabel(label, null);
    }

    public void setLabel(String label, LABEL_ORIENTATION label_orientation) {
        if (label != null) {
            if (labelComp == null) {
                labelComp = new JLabel();
            }
            labelComp.setText(label);

            setLabelOrientation(label_orientation);
        }
        else if (labelComp != null){
            this.remove(labelComp);
            labelComp = null;
        }
    }

    public JLabel getLabel() {
        return labelComp;
    }

    public void setLabelOrientation(LABEL_ORIENTATION label_orientation) {
        if (label_orientation == null)
            label_orientation = DEF_ORIENTATION;

        this.labelOrientation = label_orientation;
    }

    public LABEL_ORIENTATION getLabelOrientation() {
        return labelOrientation;
    }
}
