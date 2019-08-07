package it.unibs.dii.pajc.pig.client.bean.abstraction;

public abstract class Sensor {

    protected String id;
    protected String description;
    protected int data;

    public Sensor(String id) throws IllegalArgumentException {
        this.id = id;

        if (this.id == null)
            throw  new IllegalArgumentException("Sensor(): id can't be null");
    }

    public Sensor(String id, String description) throws IllegalArgumentException {
        this(id);
        setDescription(description);
    }

    public String getID() {
        return id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    @Override
    public boolean equals(Object obj) {
        if (Sensor.class.isAssignableFrom(obj.getClass()))
            return id.equals(((Sensor)obj).getID());

        return super.equals(obj);
    }
}
