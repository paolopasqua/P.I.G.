
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Hashtable;

public class Sensors {

    private JPanel panel;
    private JSlider slider;
    private JLabel status;
    private String name;

    public Sensors (String name) {

        this.name = name;

        panel = new JPanel();
        slider = new JSlider();
        slider.setMinorTickSpacing(10);
        slider.setPaintTicks(true);

        slider.setPaintLabels(true);

        Hashtable<Integer, JLabel> position = new Hashtable<Integer, JLabel>();
        position.put(0, new JLabel("0"));
        position.put(50, new JLabel("50"));
        position.put(100, new JLabel("100"));

        slider.setLabelTable(position);
        status = new JLabel(this.name+"Sensor", JLabel.CENTER);
        addListener();
        slider.setValue(20);

    }

    public void addListener () {

        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                status.setText(name+"Sensor - value:"+((JSlider)e.getSource()).getValue());
            }
        });

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
}
