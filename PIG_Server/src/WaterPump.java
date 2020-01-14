import java.util.UUID;

public class WaterPump extends Device {

    private int pumpLevel;

    public WaterPump(String name) {
        this.deviceUUID = UUID.randomUUID();
        this.setType(500);
        this.setSensorName(name);
        pumpLevel = 0; //Spento
    }

    @Override
    public void setValue(int level) {
        updateValue(level);
        this.pumpLevel = level;
    }

    public void updateValue(int level) {
        VirtualThing.setDeviceValue(this.getType(), level, deviceUUID);
    }

    @Override
    public int getValue (){ return pumpLevel; }

}