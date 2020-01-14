import java.util.UUID;

public class Fan extends Device {

    private int fanValue;

    public Fan(String name) {
        this.deviceUUID = UUID.randomUUID();
        this.setType(350);
        this.setSensorName(name);
        fanValue = 0; //Spento
    }

    @Override
    public void setValue(int value) {
        updateValue(value);
        fanValue = value;
    }

    public void updateValue(int value) {
        VirtualThing.setDeviceValue(this.getType(), value, deviceUUID);
    }

    @Override
    public int getValue() {
        return fanValue;
    }

}
