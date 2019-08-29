package it.unibs.dii.pajc.pig.client.view.component.generalpurpouse;

import javax.swing.*;
import java.awt.*;
import java.sql.Time;
import java.util.Date;

public class TimePicker extends JPanel {

    public static final int HOUR_MIN_VALUE = 0;
    public static final int HOUR_MAX_VALUE = 23;
    public static final int HOUR_STEP_VALUE = 1;

    public static final int MINUTE_MIN_VALUE = 0;
    public static final int MINUTE_MAX_VALUE = 59;
    public static final int MINUTE_STEP_VALUE = 1;

    private JSpinner hour;
    private JSpinner minute;

    public TimePicker() {
        this(new Date().getTime());
    }

    public TimePicker(long time) {
        this(new Time(time));
    }

    public TimePicker(Time time) {
        this(time.getHours(), time.getMinutes());
    }

    private TimePicker(int hour, int minute) {
        initComponent();

        this.hour.setValue(hour);
        this.minute.setValue(minute);
    }

    private void initComponent() {
        super.setLayout(new GridLayout(1, 0, 5, 0));

        SpinnerNumberModel hourModel = new SpinnerNumberModel(0, HOUR_MIN_VALUE, HOUR_MAX_VALUE, HOUR_STEP_VALUE);
        hour = new JSpinner(hourModel);
        LabeledComponent labeledHour = new LabeledComponent(hour, "h", LabeledComponent.LABEL_ORIENTATION.WEST);

        SpinnerNumberModel minuteModel = new SpinnerNumberModel(0, MINUTE_MIN_VALUE, MINUTE_MAX_VALUE, MINUTE_STEP_VALUE);
        minute = new JSpinner(minuteModel);
        LabeledComponent labeledMinute = new LabeledComponent(minute, "m", LabeledComponent.LABEL_ORIENTATION.WEST);

        super.add(labeledHour);
        super.add(labeledMinute);
    }

    public Time getTime() {
        return new Time(Integer.parseInt(hour.getValue()+""), Integer.parseInt(minute.getValue()+""), 0);
    }

    public long getTimeLong() {
        return getTime().getTime();
    }


    @Override
    public Component add(Component comp) {
        return null;
    }

    @Override
    public Component add(String name, Component comp) {
        return null;
    }

    @Override
    public Component add(Component comp, int index) {
        return null;
    }

    @Override
    public void remove(int index) {
        //Do Nothing
    }

    @Override
    public void remove(Component comp) {
        //Do Nothing
    }

    @Override
    public void removeAll() {
        //Do Nothing
    }

    @Override
    public void setLayout(LayoutManager mgr) {
        //Do Nothing
    }
}
