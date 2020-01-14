package it.unibs.dii.pajc.pig.client.bean;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;
import it.unibs.dii.pajc.pig.client.bean.device.emulated.EmulatedFan;
import it.unibs.dii.pajc.pig.client.bean.device.emulated.EmulatedLamp;
import it.unibs.dii.pajc.pig.client.bean.device.emulated.EmulatedPump;
import it.unibs.dii.pajc.pig.client.bean.device.emulated.EmulatedTempResistor;
import it.unibs.dii.pajc.pig.client.bean.generic.Activity;
import it.unibs.dii.pajc.pig.client.bean.generic.Rule;
import it.unibs.dii.pajc.pig.client.bean.sensor.DHT11_TemperatureSensor;
import it.unibs.dii.pajc.pig.client.bean.sensor.EmulatedTempSensor;
import it.unibs.dii.pajc.pig.client.bean.sensor.EmulatedWaterSensor;
import it.unibs.dii.pajc.pig.client.utility.MultiTypeModel;

import java.util.ArrayList;
import java.util.List;

public class CommunicationData {

    private String packID;
    private String command;
    private MultiTypeModel data;

    public CommunicationData(String command) {
        setPackID(null);
        data = new MultiTypeModel();
        setCommand(command);
    }

    public String getPackID() {
        return packID;
    }

    public void setPackID(String packID) {
        this.packID = packID;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void addString(String str) {
        data.addElement(str);
    }

    public void removeString(String str) {
        data.removeElement(str);
    }

    public List<String> getStrings() {
        return data.getElements(String.class);
    }

    public void addSensor(Sensor sensor) {
        data.addElement(sensor);
    }

    public void removeSensor(Sensor sensor) {
        data.removeElement(sensor);
    }

    public List<Sensor> getSensors() {
        List<Sensor> res = new ArrayList<>();

        if (data.getElements(Sensor.class) != null)
            res.addAll(data.getElements(Sensor.class));

        if (data.getElements(EmulatedTempSensor.class) != null)
            res.addAll(data.getElements(EmulatedTempSensor.class));

        if (data.getElements(EmulatedWaterSensor.class) != null)
            res.addAll(data.getElements(EmulatedWaterSensor.class));

        if (data.getElements(DHT11_TemperatureSensor.class) != null)
            res.addAll(data.getElements(DHT11_TemperatureSensor.class));

        return res;
    }

    public void addDevice(Device device) {
        data.addElement(device);
    }

    public void removeDevice(Device device) {
        data.removeElement(device);
    }

    public List<Device> getDevices() {
        List<Device> res = new ArrayList<>();

        if (data.getElements(Device.class) != null)
            res.addAll(data.getElements(Device.class));

        if (data.getElements(EmulatedFan.class) != null)
            res.addAll(data.getElements(EmulatedFan.class));

        if (data.getElements(EmulatedPump.class) != null)
            res.addAll(data.getElements(EmulatedPump.class));

        if (data.getElements(EmulatedLamp.class) != null)
            res.addAll(data.getElements(EmulatedLamp.class));

        if (data.getElements(EmulatedTempResistor.class) != null)
            res.addAll(data.getElements(EmulatedTempResistor.class));

        return res;
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
