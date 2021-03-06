package it.unibs.dii.pajc.pig.client.view;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.generic.Action;
import it.unibs.dii.pajc.pig.client.bean.generic.Activity;
import it.unibs.dii.pajc.pig.client.model.ThemeDataSource;
import it.unibs.dii.pajc.pig.client.utility.CalendarFormatter;
import it.unibs.dii.pajc.pig.client.view.component.PIGDialog;
import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.LabeledComponent;
import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.RadioButtonGroupPanel;
import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.TimePicker;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.WindowListener;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.UUID;

public class ActivityDialog implements PIGView {

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/view/ActivityDialog");

    private ListModel<Device> deviceDataSource;
    private JFrame owner;
    private PIGDialog dialog;
    private JPanel backgroundPanel;
    private JComboBox<Device> deviceComboBox;
    private JComboBox<Action> actionComboBox;
    private JPanel timingDataPanel;
    private JDatePickerImpl executionDatePicker;
    private TimePicker timePicker;
    private JSpinner duration;
    private JSpinner repeat;
    private RadioButtonGroupPanel repeatUnits;

    public ActivityDialog(JFrame owner, ListModel<Device> deviceDataSource) {
        this.owner = owner;
        this.deviceDataSource = deviceDataSource;

        $$$setupUI$$$();
        initComponent();
    }

    private void initComponent() {
        GridBagConstraints gbc;

        /***** BACKGROUND SETUP ******/
        backgroundPanel.setLayout(new GridBagLayout());

        /***** DATA SETUP ******/
        deviceComboBox = new JComboBox<>();
        actionComboBox = new JComboBox<>();

        /***** DEVICE SETUP ******/
        deviceComboBox.setRenderer((jList, device, i, b, b1) -> device != null ? new JLabel(device.getDescription()) : null);
        loadDeviceCombo();
        deviceComboBox.addItemListener(itemEvent -> {
            if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                Device device = (Device) itemEvent.getItem();

                if (device != null) {
                    loadActionCombo(device);
                }
            }
        });
        LabeledComponent deviceLabeledCombobox = new LabeledComponent(deviceComboBox, localizationBundle.getString("combobox.device.label"), LabeledComponent.LABEL_ORIENTATION.WEST);

        /***** ACTION SETUP ******/
        actionComboBox.setRenderer((jList, action, i, b, b1) -> action != null ? new JLabel(action.getDescription()) : null);
        LabeledComponent actionLabeledCombobox = new LabeledComponent(actionComboBox, localizationBundle.getString("combobox.action.label"), LabeledComponent.LABEL_ORIENTATION.WEST);

        /***** TIMING SETUP ******/
        timingDataPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.ipadx = 0;
        gbc.ipady = 0;
        gbc.weighty = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        UtilDateModel model = new UtilDateModel();
        model.setValue(new Date());
        Properties dateProp = new Properties();
        dateProp.put("text.today", localizationBundle.getString("panel.timingdata.execution.datepicker.text.today"));
        dateProp.put("text.month", localizationBundle.getString("panel.timingdata.execution.datepicker.text.month"));
        dateProp.put("text.year", localizationBundle.getString("panel.timingdata.execution.datepicker.text.year"));
        JDatePanelImpl datePanel = new JDatePanelImpl(model, dateProp);
        executionDatePicker = new JDatePickerImpl(datePanel, new CalendarFormatter());
        executionDatePicker.setToolTipText(localizationBundle.getString("panel.timingdata.execution.datepicker.tooltip"));
        LabeledComponent execution = new LabeledComponent(executionDatePicker, localizationBundle.getString("panel.timingdata.execution.datepicker.label"), LabeledComponent.LABEL_ORIENTATION.WEST);
        gbc.gridy = 0;
        timingDataPanel.add(execution, gbc);

        timePicker = new TimePicker();
        timePicker.setToolTipText(localizationBundle.getString("panel.timingdata.execution.timepicker.tooltip"));
        LabeledComponent executionTime = new LabeledComponent(timePicker, localizationBundle.getString("panel.timingdata.execution.timepicker.label"), LabeledComponent.LABEL_ORIENTATION.WEST);
        gbc.gridy = 1;
        timingDataPanel.add(executionTime, gbc);

//        SpinnerDateModel dateModel = new SpinnerDateModel();
//        JSpinner duration = new JSpinner(dateModel);
//        LabeledComponent durationLabeled = new LabeledComponent(duration, "Execution:", LabeledComponent.LABEL_ORIENTATION.WEST);
//        timingDataPanel.add(durationLabeled);

        SpinnerNumberModel durationModel = new SpinnerNumberModel();
        durationModel.setMinimum(0);
        durationModel.setStepSize(1);
        duration = new JSpinner(durationModel);
        duration.setToolTipText(localizationBundle.getString("panel.timingdata.duration.tooltip"));
        LabeledComponent durationMinuteLabel = new LabeledComponent(duration, localizationBundle.getString("panel.timingdata.duration.unit.label"), LabeledComponent.LABEL_ORIENTATION.EAST);
        LabeledComponent durationLabeled = new LabeledComponent(durationMinuteLabel, localizationBundle.getString("panel.timingdata.duration.label"), LabeledComponent.LABEL_ORIENTATION.WEST);
        gbc.gridy = 2;
        timingDataPanel.add(durationLabeled, gbc);

        SpinnerNumberModel repeatModel = new SpinnerNumberModel();
        repeatModel.setMinimum(0);
        repeatModel.setStepSize(1);
        repeat = new JSpinner(repeatModel);
        repeat.setToolTipText(localizationBundle.getString("panel.timingdata.repeat.tooltip"));
        repeatUnits = new RadioButtonGroupPanel();
        repeatUnits.add(new JRadioButton(Activity.REPETITION.values()[0].getLongRepresentation(), true));
        for (int i = 1; i < Activity.REPETITION.values().length; i++)
            repeatUnits.add(new JRadioButton(Activity.REPETITION.values()[i].getLongRepresentation(), false));
        JPanel repeatWithUnit = new JPanel(new BorderLayout());
        repeatWithUnit.add(repeat, BorderLayout.CENTER);
        repeatWithUnit.add(repeatUnits, BorderLayout.SOUTH);
        LabeledComponent repeatLabeled = new LabeledComponent(repeatWithUnit, localizationBundle.getString("panel.timingdata.repeat.label"), LabeledComponent.LABEL_ORIENTATION.WEST);
        gbc.gridy = 3;
        timingDataPanel.add(repeatLabeled, gbc);


        /***** COMPONENTS ADDING ******/
        gbc = new GridBagConstraints();
        gbc.ipadx = 0;
        gbc.ipady = 0;
        gbc.weighty = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        gbc.gridy = 0;
        backgroundPanel.add(deviceLabeledCombobox, gbc);

        gbc.gridy = 1;
        backgroundPanel.add(actionLabeledCombobox, gbc);

        gbc.gridy = 2;
        backgroundPanel.add(timingDataPanel, gbc);


        /***** THEME SETUP *****/
        ThemeDataSource theme = ThemeDataSource.getInstance();

        Color c = theme.getDialogActivityBackground();
        if (c != null)
            setBackgroundRecursivly(backgroundPanel, c);

        c = theme.getDialogActivityForeground();
        if (c != null)
            setForegroundRecursivly(backgroundPanel, c);

        Border b = theme.getDialogActivityBorder();
        backgroundPanel.setBorder(b);

        c = theme.getDialogActivityTimingDataBackground();
        if (c != null)
            setBackgroundRecursivly(timingDataPanel, c);

        c = theme.getDialogActivityTimingDataForeground();
        if (c != null)
            setForegroundRecursivly(timingDataPanel, c);

        b = theme.getDialogActivityTimingDataBorder(localizationBundle.getString("panel.timingdata.title"));
        timingDataPanel.setBorder(b);

        /***** DIALOG SETUP ******/
        dialog = new PIGDialog(owner, localizationBundle.getString("dialog.title"), true);
        dialog.setPreferredSize(new Dimension(400, 300));
        dialog.setSize(new Dimension(400, 300));
        dialog.setContentPane(backgroundPanel);
        dialog.pack();
    }

    private void setBackgroundRecursivly(Component comp, Color c) {
        if (comp instanceof Container) {
            Container container = (Container)comp;
            container.setBackground(c);
            for (Component component : container.getComponents())
                setBackgroundRecursivly(component, c);
        }
        else {
            comp.setBackground(c);
        }
    }

    private void setForegroundRecursivly(Component comp, Color c) {
        if (comp instanceof Container) {
            Container container = (Container)comp;
            container.setForeground(c);
            for (Component component : container.getComponents())
                setForegroundRecursivly(component, c);
        }
        else {
            comp.setForeground(c);
        }
    }

    private void loadDeviceCombo() {
        if (deviceDataSource != null) {
            for (int i = 0; i < deviceDataSource.getSize(); i++) {
                deviceComboBox.addItem(deviceDataSource.getElementAt(i));
            }

            if (deviceDataSource.getSize() > 0)
                loadActionCombo(((Device) deviceComboBox.getSelectedItem()));
        }
    }

    private void loadActionCombo(Device device) {
        actionComboBox.removeAllItems();

        if (device.getActions() != null) {
            for (int i = 0; i < device.getActions().length; i++) {
                actionComboBox.addItem(device.getActions()[i]);
            }
        }
    }


    public PIGDialog getFrame() {
        return dialog;
    }

    public void close() {
        getFrame().dispose();
    }

    @Override
    public void show() {
        dialog.setVisible(true);
    }

    public void hide() {
        dialog.setVisible(false);
    }

    @Override
    public void addWindowListener(WindowListener lst) {
        getFrame().addWindowListener(lst);
    }

    @Override
    public void removeWindowListener(WindowListener lst) {
        getFrame().removeWindowListener(lst);
    }

    public void setTimingDataVisible(boolean visible) {
        timingDataPanel.setVisible(visible);
    }

    public boolean isTimingDataVisible() {
        return timingDataPanel.isVisible();
    }

    public Activity getActivity() {
        return getActivity(UUID.randomUUID().toString());
    }

    public Activity getActivity(String forceID) {
        Date execDateTime = (Date) executionDatePicker.getModel().getValue();
        execDateTime.setHours(timePicker.getTime().getHours());
        execDateTime.setMinutes(timePicker.getTime().getMinutes());
        execDateTime.setSeconds(0);

        Device deviceSelected = (Device) deviceComboBox.getSelectedItem();
        Action actionSelected = (Action) actionComboBox.getSelectedItem();
        Activity activity = new Activity(forceID, deviceSelected, actionSelected, execDateTime);

        if (timingDataPanel.isVisible()) {
            activity.setDuration(Integer.parseInt(duration.getValue() + ""));
            activity.setRepetitionValue(Integer.parseInt(repeat.getValue() + ""));
            activity.setRepetitionUnits(Activity.REPETITION.getByDescription(repeatUnits.getSelected().getText(), true));
        }

        return activity;
    }

    public void setDeviceSelected(Device d) {
        int index = -1;
        if (d != null) {// && (index = getDeviceIndex(d)) != -1) {
            //deviceComboBox.setSelectedItem(index);
            deviceComboBox.setSelectedItem(d);
        }
    }

    private int getDeviceIndex(Device d) {
        for (int i = 0; i < deviceComboBox.getModel().getSize(); i++)
            if (deviceComboBox.getModel().getElementAt(i).equals(d))
                return i;
        return -1;
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        backgroundPanel.setPreferredSize(new Dimension(400, 300));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return backgroundPanel;
    }

}
