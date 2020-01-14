import java.util.UUID;

public class WaterSensor extends Sensor {

    private int waterLevel;

    public WaterSensor(String name) {

        this.sensorUUID = UUID.randomUUID();
        this.setSensorName(name);
        waterLevel = getValue();
        this.type = 200;

    }

    @Override
    public int getValue() {
        updateWaterLevel();
        return waterLevel;
    }

    @Override
    public int getType() {return this.type; }


    public void updateWaterLevel() {
        //Updating del valore per mezzo del sensore
        waterLevel = VirtualThing.getWaterLevel();
    }

}
