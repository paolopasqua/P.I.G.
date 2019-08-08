package it.unibs.dii.pajc.pig.client.bean;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;
import it.unibs.dii.pajc.pig.client.bean.generic.Activity;
import it.unibs.dii.pajc.pig.client.bean.generic.Rule;
import it.unibs.dii.pajc.pig.client.utility.MultiTypeModel;

import java.util.List;

public class CommunicationData {

    private String command;
    private MultiTypeModel data;

    public CommunicationData(String command) {
        data = new MultiTypeModel();
        setCommand(command);
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void addSensor(Sensor sensor) {
        data.addElement(sensor);
    }

    public void removeSensor(Sensor sensor) {
        data.removeElement(sensor);
    }

    public List<Sensor> getSensors() {
        return data.getElements(Sensor.class);
    }

    public void addDevice(Device device) {
        data.addElement(device);
    }

    public void removeDevice(Device device) {
        data.removeElement(device);
    }

    public List<Device> getDevices() {
        return data.getElements(Device.class);
    }

    public void addActivity(Activity activity) {
        data.addElement(activity);
    }

    public void removeActivity(Activity activity) {
        data.removeElement(activity);
    }

    public List<Activity> getActivities() {
        return data.getElements(Activity.class);
    }

    public void addRule(Rule rule) {
        data.addElement(rule);
    }

    public void removeRule(Rule rule) {
        data.removeElement(rule);
    }

    public List<Rule> getRules() {
        return data.getElements(Rule.class);
    }
}
