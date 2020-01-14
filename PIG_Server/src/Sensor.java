import java.util.UUID;

public abstract class Sensor extends Hardware {

    //I sensori sono passivi, non hanno bisogno di metodi setter;

    private String sensorName;
    protected UUID sensorUUID;
    protected int type;
    protected int value;

    public String getSensorName(){
        return sensorName;
    }

    public UUID getUUID(){
        return sensorUUID;
    }

    protected void setSensorName(String name){
        this.sensorName = name;
    }

    public abstract int getValue ();

    public abstract int getType();

}
