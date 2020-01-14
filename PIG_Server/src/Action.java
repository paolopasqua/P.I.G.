import java.util.UUID;

public class Action {

    private UUID deviceUUID;
    private int deviceType;
    private int valueOn;
    private int valueOff;

    public Action (UUID devUUID, int devType, int valOn, int valOff) {

        this.deviceType = devType;
        this.deviceUUID = devUUID;
        this.valueOn = valOn;
        this.valueOff = valOff;

    }

    public int getDeviceType() {
        return deviceType;
    }

    public int getValueOff() {
        return valueOff;
    }

    public int getValueOn() {
        return valueOn;
    }

    public UUID getDeviceUUID() {
        return deviceUUID;
    }

    public void setValueOn () {

        ((Device) GreenHouse.getGreenHmap().get(deviceUUID)).setValue(valueOn);

    }

    public void setValueOff () {

        ((Device) GreenHouse.getGreenHmap().get(deviceUUID)).setValue(valueOff);

    }

}
