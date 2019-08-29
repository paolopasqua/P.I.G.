package it.unibs.dii.pajc.pig.client.controller;

import it.unibs.dii.pajc.pig.client.bean.CommunicationData;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;
import it.unibs.dii.pajc.pig.client.bean.device.emulated.EmulatedFan;
import it.unibs.dii.pajc.pig.client.bean.device.emulated.EmulatedLamp;
import it.unibs.dii.pajc.pig.client.bean.device.emulated.EmulatedPump;
import it.unibs.dii.pajc.pig.client.bean.device.emulated.EmulatedTempResistor;
import it.unibs.dii.pajc.pig.client.bean.generic.Activity;
import it.unibs.dii.pajc.pig.client.bean.generic.Rule;
import it.unibs.dii.pajc.pig.client.bean.sensor.EmulatedSensor;
import it.unibs.dii.pajc.pig.client.utility.LogicActionEvent;
import it.unibs.dii.pajc.pig.client.utility.LogicActionListener;
import it.unibs.dii.pajc.pig.client.view.HelpView;
import it.unibs.dii.pajc.pig.client.view.ManagementView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManagementManager implements ManagementController, ConnectionObserver<CommunicationData> {

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/controller/ManagementManager");

    private HelpView helpView;
    private ManagementView view;
    private ConnectionController<CommunicationData> connectionController;
    private ArrayList<ManagementObserver<ManagementController>> observers;

    private DefaultListModel<Device> deviceDataSource;
    private DefaultListModel<Sensor> sensorDataSource;
    private DefaultListModel<Rule> ruleDataSource;
    private DefaultListModel<Activity> activityDataSource;

    public ManagementManager() {
        observers = new ArrayList<>();
        deviceDataSource = new DefaultListModel<>();
        sensorDataSource = new DefaultListModel<>();
        ruleDataSource = new DefaultListModel<>();
        activityDataSource = new DefaultListModel<>();

        //TODO: remove test
        deviceDataSource.addElement(new EmulatedFan("1", "Fan"));
        deviceDataSource.lastElement().setStatus(EmulatedFan.FAN_STATUS.OFF);
        deviceDataSource.addElement(new EmulatedLamp("2", "Lamp"));
        deviceDataSource.lastElement().setStatus(EmulatedLamp.LAMP_STATUS.OFF);
        deviceDataSource.addElement(new EmulatedPump("3", "Pump"));
        deviceDataSource.lastElement().setStatus(EmulatedPump.PUMP_STATUS.OFF);
        deviceDataSource.addElement(new EmulatedTempResistor("4", "Temperature Resistor"));
        deviceDataSource.lastElement().setStatus(EmulatedTempResistor.TEMP_RESISTOR_STATUS.OFF);
        sensorDataSource.addElement(new EmulatedSensor("5", "Generic Sensor"));
    }

    private void validateView(ManagementView view, String location) throws IllegalArgumentException {
        if (view == null)
            throw new IllegalArgumentException(location + ": View can't be null.");
    }

    private void initView() {
        view.setServerInfo(connectionController.getServerAddress(), connectionController.getServerInfo());
        view.setDeviceDataSource(deviceDataSource);
        view.setSensorDataSource(sensorDataSource);
        view.setActivityDataSource(activityDataSource);
        view.setRuleDataSource(ruleDataSource);

        view.addHelpActionListener(actionEvent -> {
            if (helpView != null) {
                helpView.focus(actionEvent.getSource());
                helpView.show();
            } else {
                view.showAlert(localizationBundle.getString("alert.help.opening.title"), localizationBundle.getString("alert.help.opening.message"));
            }
        });
        view.addDisconnectActionListener(actionEvent -> this.close());
        //view.addSettingActionListener(actionEvent -> ); //TODO: future implementation
        //TODO: logic listeners implementation
        view.addSensorLogicActionListener(new LogicActionListener<Sensor>() {
            @Override
            public boolean performInsertAction(LogicActionEvent<Sensor> evt) {
                return false;
            }

            @Override
            public boolean performChangeAction(LogicActionEvent<Sensor> evt) {
                return false;
            }

            @Override
            public boolean performRemoveAction(LogicActionEvent<Sensor> evt) {
                return false;
            }
        });
        view.addDeviceLogicActionListener(new LogicActionListener<Device>() {
            @Override
            public boolean performInsertAction(LogicActionEvent<Device> evt) {
                return false;
            }

            @Override
            public boolean performChangeAction(LogicActionEvent<Device> evt) {
                return false;
            }

            @Override
            public boolean performRemoveAction(LogicActionEvent<Device> evt) {
                return false;
            }
        });
        view.addRuleLogicActionListener(new LogicActionListener<Rule>() {
            @Override
            public boolean performInsertAction(LogicActionEvent<Rule> evt) {
                return false;
            }

            @Override
            public boolean performChangeAction(LogicActionEvent<Rule> evt) {
                return false;
            }

            @Override
            public boolean performRemoveAction(LogicActionEvent<Rule> evt) {
                return false;
            }
        });
        view.addActivityLogicActionListener(new LogicActionListener<Activity>() {
            @Override
            public boolean performInsertAction(LogicActionEvent<Activity> evt) {
                return false;
            }

            @Override
            public boolean performChangeAction(LogicActionEvent<Activity> evt) {
                return false;
            }

            @Override
            public boolean performRemoveAction(LogicActionEvent<Activity> evt) {
                return false;
            }
        });

    }

    private void elaborateDataToDataSource(DefaultListModel dataSource, String command, Object data) {
        //TODO: manage command string (insert/update/remove)

        int index = dataSource.indexOf(data);
        if (index < 0)
            dataSource.addElement(data);
        else
            dataSource.set(index, data);
    }

    @Override
    public void close() {
        observers.forEach(observer -> observer.closingAlert(this));

        connectionController.close();
        view.close();
    }

    @Override
    public void start() throws IllegalArgumentException {
        validateView(view, "ManagementManager.start()");
        initView();
        view.show();
        view.setWaitForData(true);
        view.setWaitForDataStatus(localizationBundle.getString("dialog.waitfordata.status.component.label.loading"));

        //TODO:REMOVE
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        view.setWaitForDataStatus(localizationBundle.getString("dialog.waitfordata.status.component.label.opening"));
        connectionController.start();

        //TODO:REMOVE
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //TODO: data request
        view.setWaitForDataStatus(localizationBundle.getString("dialog.waitfordata.status.component.label.requesting"));
        //connectionController.send(null);

        //TODO:REMOVE
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        view.setWaitForData(false);
    }

    @Override
    public void attachObserver(ManagementObserver<ManagementController> observer) {
        observers.add(observer);
    }

    @Override
    public void detachObserver(ManagementObserver<ManagementController> observer) {
        observers.remove(observer);
    }

    @Override
    public void elaborateData(CommunicationData data) {
        String command = data.getCommand();

        //TODO: manage other possible command

        data.getDevices().forEach(device -> elaborateDataToDataSource(deviceDataSource, command, device));
        data.getSensors().forEach(sensor -> elaborateDataToDataSource(sensorDataSource, command, sensor));
        data.getActivities().forEach(activity -> elaborateDataToDataSource(activityDataSource, command, activity));
        data.getRules().forEach(rule -> elaborateDataToDataSource(ruleDataSource, command, rule));
    }

    public void setView(ManagementView view) throws IllegalArgumentException {
        validateView(view, "ManagementManager.setView()");
        this.view = view;
    }

    public void removeView() {
        this.view = null;
    }

    public void setHelpView(HelpView helpView) {
        this.helpView = helpView;
    }

    public void removeHelpView() {
        this.helpView = null;
    }

    public void setConnectionController(ConnectionController<CommunicationData> connectionController) {
        connectionController.attachObserver(this);
        this.connectionController = connectionController;
    }

    public void removeConnectionController() {
        this.connectionController.detachObserver(this);
        this.connectionController = null;
    }
}
