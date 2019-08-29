package it.unibs.dii.pajc.pig.client.bean.sensor;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;

public class EmulatedSensor extends Sensor {

    public EmulatedSensor(String id) throws IllegalArgumentException {
        super(id);
    }

    public EmulatedSensor(String id, String description) throws IllegalArgumentException {
        super(id, description);
    }

}
