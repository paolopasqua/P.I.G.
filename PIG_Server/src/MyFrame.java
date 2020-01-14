
import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;
import java.util.ArrayList;


public class MyFrame {

    private JFrame frame;
    private JPanel content;
    private JScrollPane console;
    public ArrayList<Devices> devicesArrayList;
    public ArrayList<Sensors> sensorsArrayList;

    public MyFrame () {

        sensorsArrayList = new ArrayList<>();
        devicesArrayList = new ArrayList<>();

        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("Slider with change listener");
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        content = new JPanel(new GridLayout(3, 1));
        frame.add(content, BorderLayout.CENTER);

        addNewSensor("Water");
        addNewSensor("Temp");

        addNewDevice("Lamp");
        addNewDevice("Fan");
        addNewDevice("HeatResistor");
        addNewDevice("Pump");

        JTextArea ta = new JTextArea();
        TextAreaOutputStream taos = new TextAreaOutputStream(ta, 60);
        PrintStream ps = new PrintStream(taos);
        System.setOut(ps);
        System.setErr(ps);

        console = new JScrollPane(ta);
        console.setMaximumSize(new Dimension(800, 300));

        frame.add(console, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);

    }

    public void addNewSensor (String name) {

        Sensors sensor = new Sensors(name);
        content.add(sensor.getPanel().add(sensor.getSlider()));
        content.add(sensor.getStatus());
        sensorsArrayList.add(sensor);

    }

    public void addNewDevice (String name) {

        Devices device = new Devices(name);
        content.add(device.getPanel().add(device.getSlider()));
        content.add(device.getStatus());
        devicesArrayList.add(device);

    }

}
