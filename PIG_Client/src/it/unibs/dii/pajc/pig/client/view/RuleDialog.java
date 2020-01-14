package it.unibs.dii.pajc.pig.client.view;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;
import it.unibs.dii.pajc.pig.client.bean.generic.Action;
import it.unibs.dii.pajc.pig.client.bean.generic.Activity;
import it.unibs.dii.pajc.pig.client.bean.generic.Rule;
import it.unibs.dii.pajc.pig.client.model.ThemeDataSource;
import it.unibs.dii.pajc.pig.client.view.component.PIGDialog;
import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.LabeledComponent;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.EventListenerList;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.UUID;

public class RuleDialog implements PIGView {

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/view/RuleDialog");

    private EventListenerList confirmListeners;
    private ListModel<Device> deviceDataSource;
    private ListModel<Sensor> sensorDataSource;
    private JFrame owner;
    private PIGDialog dialog;
    private ActivityDialog activityDialog;
    private JPanel backgroundPanel;
    private JComboBox<Sensor> sensorComboBox;
    private JComboBox<Rule.COMPARATOR> comparatorComboBox;
    private JFormattedTextField comparingValue;

    public RuleDialog(JFrame owner, ListModel<Device> deviceDataSource, ListModel<Sensor> sensorDataSource) {
        this.owner = owner;
        this.deviceDataSource = deviceDataSource;
        this.sensorDataSource = sensorDataSource;

        initComponent();
    }

    private void initComponent() {
        GridBagConstraints gbc;
        confirmListeners = new EventListenerList();

        /***** BACKGROUND SETUP ******/
        backgroundPanel.setLayout(new GridBagLayout());

        /***** DATA SETUP ******/
        sensorComboBox = new JComboBox<>();
        comparatorComboBox = new JComboBox<>();

        /***** SENSOR SETUP ******/
        sensorComboBox.setRenderer((jList, sensor, i, b, b1) -> sensor != null ? new JLabel(sensor.getDescription()) : null);
        loadSensorCombo();
        LabeledComponent sensorLabeledCombobox = new LabeledComponent(sensorComboBox, localizationBundle.getString("combobox.sensor.label"), LabeledComponent.LABEL_ORIENTATION.WEST);

        /***** COMPARATOR SETUP ******/
        comparatorComboBox.setRenderer((jList, comparator, i, b, b1) -> comparator != null ? new JLabel(comparator.getSymbol()) : null);
        loadComparatorCombo();
        LabeledComponent comparatorLabeledCombobox = new LabeledComponent(comparatorComboBox, localizationBundle.getString("combobox.comparator.label"), LabeledComponent.LABEL_ORIENTATION.WEST);

        /***** VALUE SETUP ******/
        NumberFormatter valueFormat = new NumberFormatter(NumberFormat.getIntegerInstance());
        comparingValue = new JFormattedTextField(valueFormat);
        comparingValue.setText("0");
        comparingValue.setToolTipText(localizationBundle.getString("textfield.comparingvalue.tooltip"));
        LabeledComponent labeledComparingValue = new LabeledComponent(comparingValue, localizationBundle.getString("textfield.comparingvalue.label"), LabeledComponent.LABEL_ORIENTATION.WEST);


        /***** COMPONENTS ADDING ******/
        gbc = new GridBagConstraints();
        gbc.ipadx = 0;
        gbc.ipady = 0;
        gbc.weighty = 10;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        gbc.gridy = 0;
        backgroundPanel.add(sensorLabeledCombobox, gbc);

        gbc.gridy = 1;
        backgroundPanel.add(comparatorLabeledCombobox, gbc);

        gbc.gridy = 2;
        backgroundPanel.add(labeledComparingValue, gbc);

        /***** THEME SETUP *****/
        ThemeDataSource theme = ThemeDataSource.getInstance();

        Color c = theme.getDialogRuleBackground();
        if (c != null)
            setBackgroundRecursivly(backgroundPanel, c);

        c = theme.getDialogRuleForeground();
        if (c != null)
            setForegroundRecursivly(backgroundPanel, c);

        Border b = theme.getDialogRuleBorder();
        backgroundPanel.setBorder(b);

        /***** DIALOG SETUP ******/
        dialog = new PIGDialog(owner, localizationBundle.getString("dialog.title"), true);
        dialog.setPreferredSize(new Dimension(400, 300));
        dialog.setSize(new Dimension(400, 300));
        dialog.setContentPane(backgroundPanel);
        dialog.setButtonOKText(localizationBundle.getString("dialog.buttonok.text"));
        dialog.addButtonOKActionListener(actionEvent -> {
            hide();
            activityDialog.show();
        });
        dialog.pack();


        /***** ACTIVITY DIALOG SETUP ******/
        activityDialog = new ActivityDialog(owner, deviceDataSource);
        activityDialog.setTimingDataVisible(false);
        activityDialog.getFrame().setDisposeOnClosing(false);
        activityDialog.getFrame().addButtonOKActionListener(this::fireConfirmActionListener);
        activityDialog.getFrame().setButtonCancelText(localizationBundle.getString("dialog.activity.buttoncancel.text"));
        activityDialog.getFrame().addHelpActionListener(getFrame()::fireHelpActionListener);
        activityDialog.getFrame().addOnClosingActionListener(actionEvent1 -> {
            activityDialog.hide();
            show();
        });
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

    private void loadSensorCombo() {
        if (sensorDataSource != null) {
            for (int i = 0; i < sensorDataSource.getSize(); i++) {
                sensorComboBox.addItem(sensorDataSource.getElementAt(i));
            }
        }
    }

    private void loadComparatorCombo() {
        for (int i = 0; i < Rule.COMPARATOR.values().length; i++) {
            comparatorComboBox.addItem(Rule.COMPARATOR.values()[i]);
        }
    }

    public PIGDialog getFrame() {
        return dialog;
    }

    public void close() {
        getFrame().dispose();
        if (activityDialog != null)
            activityDialog.close();
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

    public void addConfirmActionListener(ActionListener lst) {
        confirmListeners.add(ActionListener.class, lst);
    }

    public void removeConfirmActionListener(ActionListener lst) {
        confirmListeners.remove(ActionListener.class, lst);
    }

    public void fireConfirmActionListener(ActionEvent evt) {
        ActionListener[] listeners = confirmListeners.getListeners(ActionListener.class);

        if (listeners != null && listeners.length != 0)
            for (ActionListener l : listeners)
                l.actionPerformed(evt);
    }

    public Rule getRule() {
        return getRule(UUID.randomUUID().toString());
    }

    public Rule getRule(String forceID) {
        Rule rule = new Rule(
                forceID,
                (Sensor) sensorComboBox.getSelectedItem(),
                (Rule.COMPARATOR) comparatorComboBox.getSelectedItem(),
                Integer.parseInt(comparingValue.getText()),
                activityDialog.getActivity(forceID).getAction()
        );
        return rule;
    }

    public void setSensorSelected(Sensor s) {
        if (s != null) {
            sensorComboBox.setSelectedItem(s);
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
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
        backgroundPanel.setLayout(new GridBagLayout());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return backgroundPanel;
    }

}
