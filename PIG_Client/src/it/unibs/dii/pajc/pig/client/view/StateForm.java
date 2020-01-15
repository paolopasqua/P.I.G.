package it.unibs.dii.pajc.pig.client.view;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;
import it.unibs.dii.pajc.pig.client.bean.generic.Activity;
import it.unibs.dii.pajc.pig.client.bean.generic.Rule;
import it.unibs.dii.pajc.pig.client.model.ThemeDataSource;
import it.unibs.dii.pajc.pig.client.utility.LogicActionEvent;
import it.unibs.dii.pajc.pig.client.utility.LogicActionListener;
import it.unibs.dii.pajc.pig.client.utility.UtilityConstant;
import it.unibs.dii.pajc.pig.client.view.component.PIGDialog;
import it.unibs.dii.pajc.pig.client.view.component.PIGForm;
import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.GifComponent;
import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.IconButton;
import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.LabeledComponent;
import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.ListManagerPanel;
import it.unibs.dii.pajc.pig.client.view.renderer.ActivityRenderer;
import it.unibs.dii.pajc.pig.client.view.renderer.GreenhouseRenderer;
import it.unibs.dii.pajc.pig.client.view.renderer.RuleRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class StateForm implements ManagementView {

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/view/StateForm");

    private static final int TOOLBAR_HEIGHT = 52;

    private ListModel<Device> deviceDataSource;
    private ListModel<Sensor> sensorDataSource;

    private PIGDialog waitForDataDialog;
    private LabeledComponent loadingStatus;
    private ActionListener rendererDoubleClick;

    private String serverAddress, serverInfo;
    private EventListenerList settingsListeners, disconnectListeners;
    private EventListenerList deviceListeners, ruleListeners, activityListeners, sensorListeners;
    private PIGForm frame;
    private JPanel backgroundPanel;
    private JPanel rightPanel;
    private JToolBar utilityBar;
    private ListManagerPanel<Activity> activitiesListPanel;
    private JPanel leftPanel;
    private ListManagerPanel<Rule> rulesListPanel;
    private GreenhouseRenderer greenhouseRenderer1;

    public StateForm() {
        initComponent();
    }

    private void initComponent() {
        settingsListeners = new EventListenerList();
        disconnectListeners = new EventListenerList();
        deviceListeners = new EventListenerList();
        ruleListeners = new EventListenerList();
        activityListeners = new EventListenerList();
        sensorListeners = new EventListenerList();

        //INIT DOUBLE CLICK LISTENER ON A RENDERER
        rendererDoubleClick = actionEvent -> {
            Object source = actionEvent.getSource();

            if (source instanceof Device) {
                Device d = (Device) source;
                openActivityDialog(d);
            } else if (source instanceof Sensor) {
                Sensor s = (Sensor) source;
                openRuleDialog(s);
            }
        };

        /***** FRAME SETUP ******/
        frame = new PIGForm(localizationBundle.getString("form.default.title"));
        frame.setContentPane(this.backgroundPanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setHelpButtonVisible(false);
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                fireDisconnectActionListener(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED, "", new Date().getTime(), 0));
            }
        });

        /***** LOADING STATUS COMPONENT SETUP ******/
        try {
            GifComponent gif = new GifComponent(UtilityConstant.RESOURCES_LOADING_SYMBOL, UtilityConstant.ICON_DIMENSION_64, UtilityConstant.ICON_DIMENSION_64);
            loadingStatus = new LabeledComponent(gif);
        } catch (IOException e) {
            e.printStackTrace();
            loadingStatus = new LabeledComponent(new JLabel(localizationBundle.getString("dialog.waitfordata.status.component.alternative")));
        }
        loadingStatus.setLabel("", LabeledComponent.LABEL_ORIENTATION.SOUTH);
        loadingStatus.setLabelHorizontalAlignment(LabeledComponent.ALGNMENT_CENTER);

        /***** WAITING DATA DIALOG SETUP ******/
        JPanel waitForDataPanel = new JPanel(new BorderLayout());
        //p.setBorder(BorderFactory.createTitledBorder(localizationBundle.getString("dialog.waitfordata.title")));
        waitForDataPanel.add(loadingStatus, BorderLayout.CENTER);

        waitForDataDialog = new PIGDialog(getFrame(), "", false);
        waitForDataDialog.setContentPane(waitForDataPanel);
        waitForDataDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        waitForDataDialog.setButtonOKVisible(false);
        waitForDataDialog.setButtonCancelVisible(false);
        waitForDataDialog.setGenericToolBarVisible(false);
        waitForDataDialog.setButtonsPanelVisible(false);
        waitForDataDialog.setPreferredSize(new Dimension(200, 150));
        waitForDataDialog.setSize(waitForDataDialog.getPreferredSize());
        waitForDataDialog.setResizable(false);
        waitForDataDialog.setLocationRelativeTo(getFrame());
        waitForDataDialog.setLocation((getFrame().getWidth() - waitForDataDialog.getWidth()) / 2, (getFrame().getHeight() - waitForDataDialog.getHeight()) / 2);
        waitForDataDialog.setUndecorated(true);
        waitForDataDialog.pack();

        /***** ACTIVITY LIST PANEL SETUP ******/
        activitiesListPanel.setTitle(localizationBundle.getString("panel.activitieslist.title"));
        activitiesListPanel.setRenderer(new ActivityRenderer());

        IconButton addActivityButton = new IconButton(UtilityConstant.RESOURCES_ADD_SYMBOL, "A", ListManagerPanel.TOOLBAR_ICON_HEIGHT);
        addActivityButton.setToolTipText(localizationBundle.getString("panel.activitieslist.button.add.tooltip"));
        addActivityButton.addActionListener(actionEvent -> this.openActivityDialog());
        activitiesListPanel.addToToolbar(addActivityButton);

        IconButton removeActivityButton = new IconButton(UtilityConstant.RESOURCES_TRASH_SYMBOL, "R", ListManagerPanel.TOOLBAR_ICON_HEIGHT);
        removeActivityButton.setToolTipText(localizationBundle.getString("panel.activitieslist.button.remove.tooltip"));
        removeActivityButton.addActionListener(this::removeActivityAction);
        activitiesListPanel.addToToolbar(removeActivityButton);

        /***** TOOLBAR SETUP ******/
        utilityBar.setPreferredSize(new Dimension(0, TOOLBAR_HEIGHT));
        utilityBar.setFloatable(false);
        //utilityBar.setOpaque(false);
        /*utilityBar.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(1, 2, 1, 2),
                        BorderFactory.createLineBorder(Color.BLACK)
                ));*/
        // add some glue so subsequent items are pushed to the right
        //utilityBar.add(Box.createHorizontalGlue());
        utilityBar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        IconButton disconnectButton = new IconButton(UtilityConstant.RESOURCES_EXIT_SYMBOL, "E", UtilityConstant.ICON_DIMENSION_48);
        disconnectButton.setToolTipText(localizationBundle.getString("toolbar.utility.button.exit.tooltip"));
        disconnectButton.addActionListener(this::fireDisconnectActionListener);
        utilityBar.add(disconnectButton);

        IconButton settingsButton = new IconButton(UtilityConstant.RESOURCES_GEAR_SYMBOL, "S", UtilityConstant.ICON_DIMENSION_48);
        settingsButton.setToolTipText(localizationBundle.getString("toolbar.utility.button.setting.tooltip"));
        settingsButton.addActionListener(this::fireSettingActionListener);
        //utilityBar.add(settingsButton); //TODO: future feature

        IconButton helpButton = new IconButton(UtilityConstant.RESOURCES_HELP_SYMBOL, "H", UtilityConstant.ICON_DIMENSION_48);
        helpButton.setToolTipText(localizationBundle.getString("toolbar.utility.button.help.tooltip"));
        helpButton.addActionListener(getFrame()::fireHelpActionListener);
        utilityBar.add(helpButton);

        /***** RULES LIST PANEL SETUP ******/
        rulesListPanel.setTitle(localizationBundle.getString("panel.rulelist.title"));
        rulesListPanel.setRenderer(new RuleRenderer());

        IconButton addRuleButton = new IconButton(UtilityConstant.RESOURCES_ADD_SYMBOL, "A", ListManagerPanel.TOOLBAR_ICON_HEIGHT);
        addRuleButton.setToolTipText(localizationBundle.getString("panel.rulelist.button.add.tooltip"));
        addRuleButton.addActionListener(actionEvent -> this.openRuleDialog());
        rulesListPanel.addToToolbar(addRuleButton);

        IconButton removeRuleButton = new IconButton(UtilityConstant.RESOURCES_TRASH_SYMBOL, "R", ListManagerPanel.TOOLBAR_ICON_HEIGHT);
        removeRuleButton.setToolTipText(localizationBundle.getString("panel.rulelist.button.remove.tooltip"));
        removeRuleButton.addActionListener(this::removeRuleAction);
        rulesListPanel.addToToolbar(removeRuleButton);


        /***** THEME SETUP *****/
        ThemeDataSource theme = ThemeDataSource.getInstance();

        Color c = theme.getStateFormBackground();
        if (c != null) {
            backgroundPanel.setBackground(c);
            leftPanel.setBackground(c);
            rightPanel.setBackground(c);
            rulesListPanel.setPanelBackground(c);
            activitiesListPanel.setPanelBackground(c);
        }

        c = theme.getStateFormForeground();
        if (c != null) {
            backgroundPanel.setForeground(c);
            leftPanel.setForeground(c);
            rightPanel.setForeground(c);
            rulesListPanel.setPanelForeground(c);
            activitiesListPanel.setPanelForeground(c);
        }

        Border b = theme.getStateFormBorder();
        backgroundPanel.setBorder(b);

        c = theme.getStateFormWaitForDataBackground();
        if (c != null)
            setBackgroundRecursivly(waitForDataPanel, c);

        c = theme.getStateFormWaitForDataForeground();
        if (c != null)
            setForegroundRecursivly(waitForDataPanel, c);

        b = theme.getStateFormWaitForDataBorder(localizationBundle.getString("dialog.waitfordata.title"));
        waitForDataPanel.setBorder(b);

        c = theme.getStateFormRuleListBackground();
        rulesListPanel.setListBackground(c);

        c = theme.getStateFormRuleListForeground();
        rulesListPanel.setListForeground(c);

        b = theme.getStateFormRuleListBorder();
        rulesListPanel.setListBorder(b);

        c = theme.getStateFormRuleListToolbarBackground();
        rulesListPanel.setToolbarBackground(c);

        c = theme.getStateFormRuleListToolbarForeground();
        rulesListPanel.setToolbarForeground(c);

        b = theme.getStateFormRuleListToolbarBorder();
        rulesListPanel.setToolbarBorder(b);

        c = theme.getStateFormRuleListScrollBackground();
        rulesListPanel.setScrollBackground(c);

        c = theme.getStateFormRuleListScrollForeground();
        rulesListPanel.setScrollForeground(c);


        c = theme.getStateFormGreenhouseRendererBackground();
        if (c != null)
            greenhouseRenderer1.setBackground(c);

        c = theme.getStateFormGreenhouseRendererForeground();
        if (c != null)
            greenhouseRenderer1.setForeground(c);

        b = theme.getStateFormGreenhouseRendererBorder();
        greenhouseRenderer1.setBorder(b);


        c = theme.getStateFormActivityListBackground();
        activitiesListPanel.setListBackground(c);

        c = theme.getStateFormActivityListForeground();
        activitiesListPanel.setListForeground(c);

        b = theme.getStateFormActivityListBorder();
        activitiesListPanel.setListBorder(b);

        c = theme.getStateFormActivityListToolbarBackground();
        activitiesListPanel.setToolbarBackground(c);

        c = theme.getStateFormActivityListToolbarForeground();
        activitiesListPanel.setToolbarForeground(c);

        b = theme.getStateFormActivityListToolbarBorder();
        activitiesListPanel.setToolbarBorder(b);

        c = theme.getStateFormActivityListScrollBackground();
        activitiesListPanel.setScrollBackground(c);

        c = theme.getStateFormActivityListScrollForeground();
        activitiesListPanel.setScrollForeground(c);


        c = theme.getStateFormUtilitybarBackground();
        if (c != null) {
            utilityBar.setBackground(c);
        }

        c = theme.getStateFormUtilitybarForeground();
        if (c != null) {
            utilityBar.setForeground(c);
        }

        b = theme.getStateFormUtilitybarBorder();
        utilityBar.setBorder(b);
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

    private void updateFrameTitle() {
        getFrame().setTitle(serverAddress + " " + serverInfo);
    }

    private void openActivityDialog() {
        openActivityDialog(null);
    }

    private void openActivityDialog(Device d) {
        ActivityDialog activityDialog = new ActivityDialog(getFrame(), deviceDataSource);

        activityDialog.setDeviceSelected(d);
        activityDialog.getFrame().addHelpActionListener(getFrame()::fireHelpActionListener);
        activityDialog.getFrame().addButtonOKActionListener(actionEvent -> {
            Activity activity = activityDialog.getActivity();
            Activity[] data = new Activity[1];
            data[0] = activity;

            LogicActionEvent<Activity> evt = new LogicActionEvent<>(this, LogicActionEvent.INSERT_LOGIC_ACTION, new Date().getTime(), data);
            if (fireActivityInsertLogicActionListener(evt))
                activityDialog.close();
        });

        activityDialog.show();
    }

    private void removeActivityAction(ActionEvent evt) {
        List<Activity> activities = activitiesListPanel.getSelectedItemsList();
        Activity[] activityData;

        if (activities.isEmpty()) {
            activityData = new Activity[0];
        } else {
            activityData = activities.toArray(value -> new Activity[value]);
        }

        LogicActionEvent<Activity> event = new LogicActionEvent<>(this, LogicActionEvent.REMOVE_LOGIC_ACTION, evt.getWhen(), activityData);
        fireActivityRemoveLogicActionListener(event);
    }

    private void openRuleDialog() {
        openRuleDialog(null);
    }

    private void openRuleDialog(Sensor s) {
        RuleDialog ruleDialog = new RuleDialog(getFrame(), deviceDataSource, sensorDataSource);

        ruleDialog.setSensorSelected(s);
        ruleDialog.getFrame().addHelpActionListener(getFrame()::fireHelpActionListener);
        ruleDialog.addConfirmActionListener(actionEvent -> {
            Rule rule = ruleDialog.getRule();
            Rule[] data = new Rule[1];
            data[0] = rule;

            LogicActionEvent<Rule> evt = new LogicActionEvent<>(this, LogicActionEvent.INSERT_LOGIC_ACTION, new Date().getTime(), data);
            if (fireRuleInsertLogicActionListener(evt))
                ruleDialog.close();
        });

        ruleDialog.show();
    }

    private void removeRuleAction(ActionEvent evt) {
        List<Rule> rules = rulesListPanel.getSelectedItemsList();
        Rule[] ruleData;

        if (rules.isEmpty()) {
            ruleData = new Rule[0];
        } else {
            ruleData = rules.toArray(value -> new Rule[value]);
        }

        LogicActionEvent<Rule> event = new LogicActionEvent<>(this, LogicActionEvent.REMOVE_LOGIC_ACTION, evt.getWhen(), ruleData);
        fireRuleRemoveLogicActionListener(event);
    }

    private void fireActionListener(ActionEvent evt, EventListenerList listenerList) {
        ActionListener[] listeners = listenerList.getListeners(ActionListener.class);

        if (listeners != null && listeners.length != 0)
            for (ActionListener l : listeners)
                l.actionPerformed(evt);
    }

    private boolean fireInsertLogicActionListener(LogicActionEvent evt, EventListenerList listenerList) {
        LogicActionListener[] listeners = listenerList.getListeners(LogicActionListener.class);
        boolean ris = true;

        if (listeners != null && listeners.length != 0)
            for (LogicActionListener l : listeners)
                ris = ris && l.performInsertAction(evt);

        return ris;
    }

    private boolean fireChangeLogicActionListener(LogicActionEvent evt, EventListenerList listenerList) {
        LogicActionListener[] listeners = listenerList.getListeners(LogicActionListener.class);
        boolean ris = true;

        if (listeners != null && listeners.length != 0)
            for (LogicActionListener l : listeners)
                ris = ris && l.performChangeAction(evt);

        return ris;
    }

    private boolean fireRemoveLogicActionListener(LogicActionEvent evt, EventListenerList listenerList) {
        LogicActionListener[] listeners = listenerList.getListeners(LogicActionListener.class);
        boolean ris = true;

        if (listeners != null && listeners.length != 0)
            for (LogicActionListener l : listeners)
                ris = ris && l.performRemoveAction(evt);

        return ris;
    }

    private void setEnableRecursive(Container comp, boolean enable) {
        if (comp.getComponents().length > 0) {
            for (Component c : comp.getComponents())
                if (c instanceof Container)
                    setEnableRecursive((Container) c, enable);
                else
                    c.setEnabled(enable);
        }
        comp.setEnabled(enable);
    }

    public PIGForm getFrame() {
        return frame;
    }

    @Override
    public void refreshGraphics() {
        greenhouseRenderer1.repaint();
    }

    @Override
    public void setWaitForData(boolean waitForData) {
        //new Thread(() -> {
        setEnableRecursive(backgroundPanel, !waitForData);
        waitForDataDialog.setVisible(waitForData);
        //}).start();
    }

    @Override
    public void setWaitForDataStatus(String status) {
        loadingStatus.setLabel(status);
    }

    @Override
    public void show() {
        getFrame().setVisible(true);
    }

    @Override
    public void addWindowListener(WindowListener lst) {
        getFrame().addWindowListener(lst);
    }

    @Override
    public void removeWindowListener(WindowListener lst) {
        getFrame().removeWindowListener(lst);
    }

    @Override
    public void close() {
        getFrame().dispose();
    }

    @Override
    public void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(getFrame(), message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public boolean askUser(String title, String message) {
        return JOptionPane.showConfirmDialog(getFrame(), message, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    @Override
    public void setDeviceDataSource(ListModel<Device> datasource) {
        if (deviceDataSource != null) {
            for (int i = 0; i < deviceDataSource.getSize(); i++) {
                greenhouseRenderer1.remove(deviceDataSource.getElementAt(i).getDrawer());
                deviceDataSource.getElementAt(i).getDrawer().addDoubleClickListener(rendererDoubleClick);
            }
        }

        deviceDataSource = datasource;

        if (deviceDataSource != null) {
            for (int i = 0; i < deviceDataSource.getSize(); i++) {
                greenhouseRenderer1.add(deviceDataSource.getElementAt(i).getDrawer());
                deviceDataSource.getElementAt(i).getDrawer().addDoubleClickListener(rendererDoubleClick);
            }
        }
    }

    @Override
    public void setSensorDataSource(ListModel<Sensor> datasource) {
        if (sensorDataSource != null) {
            for (int i = 0; i < sensorDataSource.getSize(); i++) {
                greenhouseRenderer1.remove(sensorDataSource.getElementAt(i).getDrawer());
                sensorDataSource.getElementAt(i).getDrawer().removeDoubleClickListener(rendererDoubleClick);
            }
        }

        sensorDataSource = datasource;

        if (sensorDataSource != null) {
            for (int i = 0; i < sensorDataSource.getSize(); i++) {
                greenhouseRenderer1.add(sensorDataSource.getElementAt(i).getDrawer());
                sensorDataSource.getElementAt(i).getDrawer().addDoubleClickListener(rendererDoubleClick);
            }
        }
    }

    @Override
    public void setActivityDataSource(ListModel<Activity> datasource) {
        activitiesListPanel.setModel(datasource);
    }

    @Override
    public void setRuleDataSource(ListModel<Rule> datasource) {
        rulesListPanel.setModel(datasource);
    }

    @Override
    public void setServerInfo(String address, String info) {
        this.serverAddress = address;
        this.serverInfo = info;

        updateFrameTitle();
    }

    @Override
    public void addHelpActionListener(ActionListener lst) {
        getFrame().addHelpActionListener(lst);
    }

    @Override
    public void removeHelpActionListener(ActionListener lst) {
        getFrame().removeHelpActionListener(lst);
    }

    @Override
    public void addSettingActionListener(ActionListener lst) {
        settingsListeners.add(ActionListener.class, lst);
    }

    @Override
    public void removeSettingActionListener(ActionListener lst) {
        settingsListeners.remove(ActionListener.class, lst);
    }

    public void fireSettingActionListener(ActionEvent evt) {
        fireActionListener(evt, settingsListeners);
    }

    @Override
    public void addDisconnectActionListener(ActionListener lst) {
        disconnectListeners.add(ActionListener.class, lst);
    }

    @Override
    public void removeDisconnectActionListener(ActionListener lst) {
        disconnectListeners.remove(ActionListener.class, lst);
    }

    public void fireDisconnectActionListener(ActionEvent evt) {
        fireActionListener(evt, disconnectListeners);
    }

    @Override
    public void addDeviceLogicActionListener(LogicActionListener<Device> lst) {
        deviceListeners.add(LogicActionListener.class, lst);
    }

    @Override
    public void removeDeviceLogicActionListener(LogicActionListener<Device> lst) {
        deviceListeners.remove(LogicActionListener.class, lst);
    }

    public boolean fireDeviceInsertLogicActionListener(LogicActionEvent<Device> evt) {
        return fireInsertLogicActionListener(evt, deviceListeners);
    }

    public boolean fireDeviceChangeLogicActionListener(LogicActionEvent<Device> evt) {
        return fireChangeLogicActionListener(evt, deviceListeners);
    }

    public boolean fireDeviceRemoveLogicActionListener(LogicActionEvent<Device> evt) {
        return fireRemoveLogicActionListener(evt, deviceListeners);
    }

    @Override
    public void addSensorLogicActionListener(LogicActionListener<Sensor> lst) {
        sensorListeners.add(LogicActionListener.class, lst);
    }

    @Override
    public void removeSensorLogicActionListener(LogicActionListener<Sensor> lst) {
        sensorListeners.remove(LogicActionListener.class, lst);
    }

    public boolean fireSensorInsertLogicActionListener(LogicActionEvent<Sensor> evt) {
        return fireInsertLogicActionListener(evt, sensorListeners);
    }

    public boolean fireSensorChangeLogicActionListener(LogicActionEvent<Sensor> evt) {
        return fireChangeLogicActionListener(evt, sensorListeners);
    }

    public boolean fireSensorRemoveLogicActionListener(LogicActionEvent<Sensor> evt) {
        return fireRemoveLogicActionListener(evt, sensorListeners);
    }

    @Override
    public void addActivityLogicActionListener(LogicActionListener<Activity> lst) {
        activityListeners.add(LogicActionListener.class, lst);
    }

    @Override
    public void removeActivityLogicActionListener(LogicActionListener<Activity> lst) {
        activityListeners.remove(LogicActionListener.class, lst);
    }

    public boolean fireActivityInsertLogicActionListener(LogicActionEvent<Activity> evt) {
        return fireInsertLogicActionListener(evt, activityListeners);
    }

    public boolean fireActivityChangeLogicActionListener(LogicActionEvent<Activity> evt) {
        return fireChangeLogicActionListener(evt, activityListeners);
    }

    public boolean fireActivityRemoveLogicActionListener(LogicActionEvent<Activity> evt) {
        return fireRemoveLogicActionListener(evt, activityListeners);
    }

    @Override
    public void addRuleLogicActionListener(LogicActionListener<Rule> lst) {
        ruleListeners.add(LogicActionListener.class, lst);
    }

    @Override
    public void removeRuleLogicActionListener(LogicActionListener<Rule> lst) {
        ruleListeners.remove(LogicActionListener.class, lst);
    }

    public boolean fireRuleInsertLogicActionListener(LogicActionEvent<Rule> evt) {
        return fireInsertLogicActionListener(evt, ruleListeners);
    }

    public boolean fireRuleChangeLogicActionListener(LogicActionEvent<Rule> evt) {
        return fireChangeLogicActionListener(evt, ruleListeners);
    }

    public boolean fireRuleRemoveLogicActionListener(LogicActionEvent<Rule> evt) {
        return fireRemoveLogicActionListener(evt, ruleListeners);
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
        backgroundPanel.setLayout(new BorderLayout(0, 0));
        backgroundPanel.setFocusable(false);
        backgroundPanel.setPreferredSize(new Dimension(900, 600));
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(0, 0));
        backgroundPanel.add(rightPanel, BorderLayout.EAST);
        utilityBar = new JToolBar();
        utilityBar.setMinimumSize(new Dimension(0, 48));
        utilityBar.setPreferredSize(new Dimension(0, 48));
        rightPanel.add(utilityBar, BorderLayout.SOUTH);
        activitiesListPanel = new ListManagerPanel();
        activitiesListPanel.setMinimumSize(new Dimension(100, 20));
        activitiesListPanel.setPreferredSize(new Dimension(200, 159));
        rightPanel.add(activitiesListPanel, BorderLayout.CENTER);
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout(0, 0));
        backgroundPanel.add(leftPanel, BorderLayout.WEST);
        rulesListPanel = new ListManagerPanel();
        rulesListPanel.setMinimumSize(new Dimension(200, 20));
        rulesListPanel.setPreferredSize(new Dimension(200, 159));
        leftPanel.add(rulesListPanel, BorderLayout.CENTER);
        greenhouseRenderer1 = new GreenhouseRenderer();
        backgroundPanel.add(greenhouseRenderer1, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return backgroundPanel;
    }

}
