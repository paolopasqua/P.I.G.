import java.util.UUID;

public class Lamp extends Device {

    private int lightLevel;

    public Lamp(String name) {
        this.deviceUUID = UUID.randomUUID();
        this.setType(300);
        this.setSensorName(name);
        lightLevel = 0; //Spento
    }

    @Override
    public void setValue(int level) {
        updateValue(level);
        this.lightLevel = level;
    }

    public void updateValue(int level) {
        VirtualThing.setDeviceValue(this.getType(), level, deviceUUID);
    }

    public int getValue() {
        return lightLevel;
    }

}
