package it.unibs.dii.pajc.pig.client.bean.sensor;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;

public class DHT11_TemperatureSensor extends Sensor {

    private int dataGPIO;

    public DHT11_TemperatureSensor(String id) throws IllegalArgumentException {
        super(id);
    }

    public DHT11_TemperatureSensor(String id, String description) throws IllegalArgumentException {
        super(id, description);
    }

    public int getDataGPIO() {
        return dataGPIO;
    }

    public void setDataGPIO(int dataGPIO) {
        this.dataGPIO = dataGPIO;
    }
}
