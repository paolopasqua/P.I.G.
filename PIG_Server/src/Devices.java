

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Hashtable;

public class Devices {

    private JPanel panel;
    private JSlider slider;
    private JLabel status;
    private String name;

    public Devices (String name) {

        this.name = name;

        panel = new JPanel();
        slider = new JSlider();
        slider.setMinorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setValue(50);
        slider.setEnabled(false);

        slider.setPaintLabels(true);

        Hashtable<Integer, JLabel> position = new Hashtable<Integer, JLabel>();
        position.put(0, new JLabel("0"));
        position.put(50, new JLabel("50"));
        position.put(100, new JLabel("100"));

        slider.setLabelTable(position);
        status = new JLabel(name+"Device", JLabel.CENTER);
        setValue(0);

    }

    public JSlider getSlider() {
        return slider;
    }

    public JLabel getStatus() {
        return status;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setValue (int value) {

        slider.setValue(value);
        status.setText(name+"Device - value:" + value);

    }

}
