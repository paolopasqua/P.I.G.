import java.util.UUID;

public abstract class Device extends Hardware {

    //I sensori sono attivi, ma possono avere diversi comportamenti;

    private String deviceName;
    protected UUID deviceUUID;
    private int type;

    public String getDeviceName(){
        return deviceName;
    }

    protected void setSensorName(String name){
        this.deviceName = name;
    }

    public UUID getUUID() {
        return deviceUUID;
    }

    public int getType(){
        return type;
    }

    protected void setType(int type) {
        this.type = type;
    }

    public void setValue(int value) {}

    public abstract int getValue();

}
