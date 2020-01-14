import java.util.UUID;

public class HeatResistor extends Device {

    private int heatLevel;

    public HeatResistor(String name) {
        this.deviceUUID = UUID.randomUUID();
        this.setType(400);
        this.setSensorName(name);
        heatLevel = 0;  //Spento
    }

    @Override
    public void setValue(int level) {
        updateValue(level);
        this.heatLevel = level;
    }

    public void updateValue(int level) {
        VirtualThing.setDeviceValue(this.getType(), level, deviceUUID);
    }

    @Override
    public int getValue() {
        return heatLevel;
    }

}