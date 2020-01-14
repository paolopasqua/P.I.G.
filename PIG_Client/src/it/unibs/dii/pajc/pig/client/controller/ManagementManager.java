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
import it.unibs.dii.pajc.pig.client.bean.sensor.EmulatedTempSensor;
import it.unibs.dii.pajc.pig.client.bean.sensor.EmulatedWaterSensor;
import it.unibs.dii.pajc.pig.client.model.CommunicationProtocol;
import it.unibs.dii.pajc.pig.client.utility.LogicActionAdapter;
import it.unibs.dii.pajc.pig.client.utility.LogicActionEvent;
import it.unibs.dii.pajc.pig.client.view.HelpView;
import it.unibs.dii.pajc.pig.client.view.ManagementView;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ManagementManager implements ManagementController, ConnectionObserver<CommunicationData> {

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/controller/ManagementManager");

    private HelpView helpView;
    private ManagementView view;
    private ConnectionController<CommunicationData> connectionController;
    private ArrayList<ManagementObserver<ManagementController>> observers;
    private Semaphore dataRecoveryWait;
    private HashMap<String, Semaphore> replySemaphores;
    private HashMap<String, String> replyErrors;


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
        dataRecoveryWait = new Semaphore(0);
        replySemaphores = new HashMap<>();
        replyErrors = new HashMap<>();
        /*
        //TODO: remove test
        deviceDataSource.addElement(new EmulatedFan("1"));
        deviceDataSource.lastElement().setStatus(EmulatedFan.FAN_STATUS.OFF);
        deviceDataSource.addElement(new EmulatedLamp("2"));
        deviceDataSource.lastElement().setStatus(EmulatedLamp.LAMP_STATUS.OFF);
        deviceDataSource.addElement(new EmulatedPump("3"));
        deviceDataSource.lastElement().setStatus(EmulatedPump.PUMP_STATUS.OFF);
        deviceDataSource.addElement(new EmulatedTempResistor("4"));
        deviceDataSource.lastElement().setStatus(EmulatedTempResistor.TEMP_RESISTOR_STATUS.OFF);
        EmulatedTempSensor ts = new EmulatedTempSensor("5");
        ts.setData(10);
        sensorDataSource.addElement(ts);
        EmulatedWaterSensor ws = new EmulatedWaterSensor("6");
        ws.setData(50);
        sensorDataSource.addElement(ws);
        */
    }

    private void validateView(ManagementView view, String location) throws IllegalArgumentException {
        if (view == null)
            throw new IllegalArgumentException(location + ": View can't be null.");
    }

    private void initViewDataSource() {
        view.setDeviceDataSource(deviceDataSource);
        view.setSensorDataSource(sensorDataSource);
        view.setActivityDataSource(activityDataSource);
        view.setRuleDataSource(ruleDataSource);
    }

    private void initView() {
        view.setServerInfo(connectionController.getServerAddress(), connectionController.getServerInfo());
        initViewDataSource();

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
        //TODO: future implementation view.addSensorLogicActionListener
        //TODO: future implementation view.addDeviceLogicActionListener

        view.addRuleLogicActionListener(new LogicActionAdapter<Rule>() {
            @Override
            public boolean performInsertAction(LogicActionEvent<Rule> evt) {
                boolean response = false;

                CommunicationData data = new CommunicationData(CommunicationProtocol.CMD_ADD_ENTITY.getCommandString());
                for (Rule r : evt.getData()){
                    data.addRule(r);
                }

                response = elaborateSend(data);

                if (response) {
                    for (Rule r : evt.getData()) {
                        ruleDataSource.addElement(r);
                    }
                }

                return response;
            }
            @Override
            public boolean performRemoveAction(LogicActionEvent<Rule> evt) {
                boolean response = false;

                CommunicationData data = new CommunicationData(CommunicationProtocol.CMD_REMOVE_ENTITY.getCommandString());
                for (Rule r : evt.getData()){
                    data.addRule(r);
                }

                response = elaborateSend(data);

                if (response) {
                    for (Rule r : evt.getData()) {
                        ruleDataSource.removeElement(r);
                    }
                }

                return response;
            }
        });
        view.addActivityLogicActionListener(new LogicActionAdapter<Activity>() {
            @Override
            public boolean performInsertAction(LogicActionEvent<Activity> evt) {
                boolean response = false;

                CommunicationData data = new CommunicationData(CommunicationProtocol.CMD_ADD_ENTITY.getCommandString());
                for (Activity a : evt.getData()){
                    data.addActivity(a);
                }

                response = elaborateSend(data);

                if (response) {
                    for (Activity a : evt.getData()) {
                        activityDataSource.addElement(a);
                    }
                }

                return response;
            }
            @Override
            public boolean performRemoveAction(LogicActionEvent<Activity> evt) {
                boolean response = false;

                CommunicationData data = new CommunicationData(CommunicationProtocol.CMD_REMOVE_ENTITY.getCommandString());
                for (Activity a : evt.getData()){
                    data.addActivity(a);
                }

                response = elaborateSend(data);

                if (response) {
                    for (Activity a : evt.getData()) {
                        activityDataSource.removeElement(a);
                    }
                }

                return response;
            }
        });

    }

    private boolean elaborateSend(CommunicationData data) {
        return elaborateSend(data, true);
    }
    private boolean elaborateSend(CommunicationData data, boolean waitAReply) {
        String error = "";
        boolean response = false;

        view.setWaitForDataStatus("Sending...");
        //view.setWaitForData(true);

        String packID = connectionController.send(data);

        if (waitAReply) {
            replySemaphores.put(packID, new Semaphore(0));

            view.setWaitForDataStatus("Waiting response...");
            try {
                response = replySemaphores.get(packID).tryAcquire(CommunicationProtocol.TIMEOUT_RESPONSE.getTime(), TimeUnit.MILLISECONDS);

                if (response) {
                    view.setWaitForDataStatus("Decoding response...");

                    error = replyErrors.get(packID); //recover the error string: if packID not in the map then response is ACK else ERROR
                    response = (error == null);
                } else {
                    error = "Timeout error";
                }

            } catch (InterruptedException e) {
                error = e.getStackTrace().toString() + "\n" + "Please retry";
                response = false;
            }

            view.setWaitForData(false);

            if (!response) {
                view.showAlert("Communication Error", error);
            }
        }
        else
            response = true;

        return response;
    }

    @Override
    public void close() {
        CommunicationData d = new CommunicationData(CommunicationProtocol.CMD_DISCONNECTION.getCommandString());
        elaborateSend(d, false);

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

        //Starting connection controller
        view.setWaitForDataStatus(localizationBundle.getString("dialog.waitfordata.status.component.label.opening"));
        connectionController.start();

        //Sending data request
        view.setWaitForDataStatus(localizationBundle.getString("dialog.waitfordata.status.component.label.requesting"));
        //Server send data without request
        //CommunicationData pack = new CommunicationData(CommunicationProtocol.CMD_DATA_REQUEST.getCommandString());
        //connectionController.send(pack);

        //Wait for data recovery
        try {
            dataRecoveryWait.drainPermits(); //make sure data recovery is future.
            dataRecoveryWait.acquire(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //initViewDataSource();

        //Enable form
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

        //possible command: SUMMARY, STATE, ACK, ERROR
        if (command.equals((CommunicationProtocol.CMD_CONFIRM.getCommandString()))) {
            replySemaphores.get(data.getPackID()).release();
        }
        else if (command.equals(CommunicationProtocol.CMD_ERROR.getCommandString())) {
            String error = "";
            if (!data.getStrings().isEmpty())
                error = data.getStrings().get(0);
            replyErrors.put(data.getPackID(), error);
            replySemaphores.get(data.getPackID()).release();
        }
        else if (command.equals(CommunicationProtocol.CMD_DATA_REQUEST.getCommandString()) ||
                command.equals(CommunicationProtocol.CMD_STATE_UPDATE.getCommandString())) {
            //SUMMARY, STATE
            if (data.getDevices() != null)
                data.getDevices().forEach(device -> {
                    int index = deviceDataSource.indexOf(device);
                    //if there's not into datasource and the command is not remove then add
                    if (index < 0) {
                        if (!CommunicationProtocol.CMD_REMOVE_ENTITY.getCommandString().equals(command)) {
                            deviceDataSource.addElement(device);
                            initViewDataSource();
                        }
                        //else if it's remove command then remove
                    } else if (CommunicationProtocol.CMD_REMOVE_ENTITY.getCommandString().equals(command)) {
                        deviceDataSource.removeElement(device);
                        //else update data
                    } else {
                        deviceDataSource.get(index).setStatus(device.getStatus());
                        view.refreshGraphics();
                    }
                });
            if (data.getSensors() != null)
                data.getSensors().forEach(sensor -> {
                    int index = sensorDataSource.indexOf(sensor);
                    //if there's not into datasource and the command is not remove then add
                    if (index < 0) {
                        if (!CommunicationProtocol.CMD_REMOVE_ENTITY.getCommandString().equals(command)) {
                            sensorDataSource.addElement(sensor);
                            initViewDataSource();
                        }
                        //else if it's remove command then remove
                    } else if (CommunicationProtocol.CMD_REMOVE_ENTITY.getCommandString().equals(command)) {
                        sensorDataSource.removeElement(sensor);
                        //else update data
                    } else {
                        sensorDataSource.get(index).setData(sensor.getData());
                        view.refreshGraphics();
                    }
                });
            if (data.getActivities() != null)
                data.getActivities().forEach(activity -> {
                    int index = activityDataSource.indexOf(activity);
                    //if there's not into datasource and the command is not remove then add
                    if (index < 0) {
                        if (!CommunicationProtocol.CMD_REMOVE_ENTITY.getCommandString().equals(command)) {
                            activityDataSource.addElement(activity);
                            initViewDataSource();
                        }
                        //else if it's remove command then remove
                    } else if (CommunicationProtocol.CMD_REMOVE_ENTITY.getCommandString().equals(command)) {
                        activityDataSource.removeElement(activity);
                        //else update data
                    } else {
                        activityDataSource.set(index, activity);
                        //view.refreshGraphics();
                    }
                });
            if (data.getRules() != null)
                data.getRules().forEach(rule -> {
                    int index = ruleDataSource.indexOf(rule);
                    //if there's not into datasource and the command is not remove then add
                    if (index < 0) {
                        if (!CommunicationProtocol.CMD_REMOVE_ENTITY.getCommandString().equals(command)) {
                            ruleDataSource.addElement(rule);
                            initViewDataSource();
                        }
                        //else if it's remove command then remove
                    } else if (CommunicationProtocol.CMD_REMOVE_ENTITY.getCommandString().equals(command)) {
                        ruleDataSource.removeElement(rule);
                        //else update data
                    } else {
                        ruleDataSource.set(index, rule);
                        //view.refreshGraphics();
                    }
                });

            //initViewDataSource();

            dataRecoveryWait.release(); //If form is disabled by data waiting then enable it
        }
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
