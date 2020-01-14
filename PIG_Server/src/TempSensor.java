import java.util.UUID;

public class TempSensor extends Sensor {

    private int temperature; //In Celsius

    public TempSensor(String name) {

        this.sensorUUID = UUID.randomUUID();
        this.setSensorName(name);
        temperature = this.getValue();
        this.type = 100;

    }

    @Override
    public int getValue() {

        updateTemperature();
        return temperature;
    }

    @Override
    public int getType() {return this.type; }

    public void updateTemperature() {

        //Updating del valore per mezzo del sensore
        temperature = VirtualThing.getTemperature();

    }

}
