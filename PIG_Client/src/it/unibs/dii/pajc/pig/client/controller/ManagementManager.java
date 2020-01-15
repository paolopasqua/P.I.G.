package it.unibs.dii.pajc.pig.client.controller;

import it.unibs.dii.pajc.pig.client.bean.CommunicationData;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;
import it.unibs.dii.pajc.pig.client.bean.generic.Action;
import it.unibs.dii.pajc.pig.client.bean.generic.Activity;
import it.unibs.dii.pajc.pig.client.bean.generic.Rule;
import it.unibs.dii.pajc.pig.client.model.CommunicationProtocol;
import it.unibs.dii.pajc.pig.client.utility.LogicActionAdapter;
import it.unibs.dii.pajc.pig.client.utility.LogicActionEvent;
import it.unibs.dii.pajc.pig.client.view.HelpView;
import it.unibs.dii.pajc.pig.client.view.ManagementView;

import javax.swing.*;
import java.util.*;
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
                    if (a.getDuration() == 0) {
                        //duration is zero --> exec only the turnOn action
                        Action newAction = new Action(a.getAction().getIdDevice(), a.getAction().getOnStatusValue(), a.getAction().getDescription());
                        newAction.setTerminationAction(null); //no termination action
                        a.setAction(newAction);
                    }

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
                    error = localizationBundle.getString("alert.connection.error.messagetimeout");
                }

            } catch (InterruptedException e) {
                error = e.getStackTrace().toString() + "\n" + "Please retry";
                response = false;
            }

            view.setWaitForData(false);

            if (!response) {
                view.showAlert(localizationBundle.getString("alert.connection.error.title"), error);
            }
        }
        else
            response = true;

        return response;
    }

    private void stateDeviceUpdate(Device device, List<Device> devicesReceived) {
        //Possible commands: STATE

        int index = devicesReceived.indexOf(device);

        //if there's not into received devices then remove from datasource
        if (index < 0) {
            deviceDataSource.removeElement(device);
        } else {//else update data
            device.setStatus(devicesReceived.get(index).getStatus());
            view.refreshGraphics();

            devicesReceived.remove(index); //remove because elaborated
        }
    }

    private void stateSensorUpdate(Sensor sensor, List<Sensor> sensorsReceived) {
        //Possible commands: STATE

        int index = sensorsReceived.indexOf(sensor);

        //if there's not into received devices then remove from datasource
        if (index < 0) {
            sensorDataSource.removeElement(sensor);
        } else {//else update data
            sensor.setData(sensorsReceived.get(index).getData());
            view.refreshGraphics();

            sensorsReceived.remove(index); //remove because elaborated
        }
    }

    private void summaryActivityUpdate(Activity activity, List<Activity> activitiesReceived) {
        //Possible commands: SUMMARY

        int index = activitiesReceived.indexOf(activity);

        //if there's not into received devices then remove from datasource
        if (index < 0) {
            activityDataSource.removeElement(activity);
        }
        else {
            //activity can't change

            activitiesReceived.remove(index); //remove because elaborated
        }
    }

    private void summaryRuleUpdate(Rule rule, List<Rule> rulesReceived) {
        //Possible commands: SUMMARY

        int index = rulesReceived.indexOf(rule);

        //if there's not into received devices then remove from datasource
        if (index < 0) {
            ruleDataSource.removeElement(rule);
        }
        else {
            //rule can't change

            rulesReceived.remove(index); //remove because elaborated
        }
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
        //view.setWaitForData(true);
        view.setWaitForDataStatus(localizationBundle.getString("dialog.waitfordata.status.component.label.loading"));

        //Starting connection controller
        view.setWaitForDataStatus(localizationBundle.getString("dialog.waitfordata.status.component.label.opening"));
        connectionController.start();

        view.setWaitForDataStatus(localizationBundle.getString("dialog.waitfordata.status.component.label.requesting"));
        //Server send data without request
        //Wait for data recovery
        try {
            dataRecoveryWait.drainPermits(); //make sure data recovery is future.
            boolean response = dataRecoveryWait.tryAcquire(2, CommunicationProtocol.TIMEOUT_RESPONSE.getTime(), TimeUnit.MILLISECONDS);

            if (!response) {
                view.showAlert(localizationBundle.getString("alert.connection.error.title"), localizationBundle.getString("alert.connection.error.messagetimeout"));

                this.close();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Enable form
        //view.setWaitForData(false);
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
            //ACK: release the sending routine
            replySemaphores.get(data.getPackID()).release();
        }
        else if (command.equals(CommunicationProtocol.CMD_ERROR.getCommandString())) {
            //ERROR: set the error and release the sending routine
            String error = "";
            if (!data.getStrings().isEmpty())
                error = data.getStrings().get(0);
            replyErrors.put(data.getPackID(), error);
            replySemaphores.get(data.getPackID()).release();
        }
        else if (command.equals(CommunicationProtocol.CMD_DATA_REQUEST.getCommandString())) {
            //SUMMARY
            List<Activity> activities = data.getActivities();
            if (activities != null && !activities.isEmpty()) {
                activityDataSource.elements().asIterator().forEachRemaining(activity -> summaryActivityUpdate(activity, activities));
                //adding the remaining activities
                activities.forEach(activity -> {
                    //Setting descriptions
                    for(int i = 0; i < deviceDataSource.size(); i++) {
                        if (deviceDataSource.get(i).getID().equals(activity.getDeviceId())) {
                            activity.setDeviceDescription(deviceDataSource.get(i).getDescription());
                            Action[] actions = deviceDataSource.get(i).getActions();
                            for (int j = 0; j < actions.length; j++) {
                                if (actions[j].equals(activity.getAction())) {
                                    activity.setActionDescription(actions[j].getDescription());
                                    break;
                                }
                            }
                            break;
                        }
                    }

                    activityDataSource.addElement(activity);
                });
            }
            List<Rule> rules = data.getRules();
            if (rules != null && !rules.isEmpty()) {
                ruleDataSource.elements().asIterator().forEachRemaining(rule -> summaryRuleUpdate(rule, rules));
                //adding the remaining rules
                rules.forEach(rule -> {
                    //Setting descriptions
                    for(int i = 0; i < sensorDataSource.size(); i++) {
                        if (sensorDataSource.get(i).getID().equals(rule.getSensorId())) {
                            rule.setSensorDescription(sensorDataSource.get(i).getDescription());
                            break;
                        }
                    }
                    for(int i = 0; i < deviceDataSource.size(); i++) {
                        if (deviceDataSource.get(i).getID().equals(rule.getAction().getIdDevice())) {
                            rule.setDeviceDescription(deviceDataSource.get(i).getDescription());
                            Action[] actions = deviceDataSource.get(i).getActions();
                            for (int j = 0; j < actions.length; j++) {
                                if (actions[j].equals(rule.getAction())) {
                                    rule.getAction().setDescription(actions[j].getDescription());
                                    break;
                                }
                            }
                            break;
                        }
                    }

                    ruleDataSource.addElement(rule);
                });
            }

            initViewDataSource();

            dataRecoveryWait.release(); //If form is disabled by data waiting then enable it
        }
        else if (command.equals(CommunicationProtocol.CMD_STATE_UPDATE.getCommandString())) {
            //STATE

            List<Device> devices = data.getDevices();
            if (devices != null && !devices.isEmpty()) {
                deviceDataSource.elements().asIterator().forEachRemaining(device -> stateDeviceUpdate(device, devices));
                devices.forEach(device -> deviceDataSource.addElement(device)); //adding the remaining devices
            }
            List<Sensor> sensors = data.getSensors();
            if (sensors != null && !sensors.isEmpty()) {
                sensorDataSource.elements().asIterator().forEachRemaining(sensor -> stateSensorUpdate(sensor, sensors));
                sensors.forEach(sensor -> sensorDataSource.addElement(sensor)); //adding the remaining sensors
            }

            initViewDataSource();

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
