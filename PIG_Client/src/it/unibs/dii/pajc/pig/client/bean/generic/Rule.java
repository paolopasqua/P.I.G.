package it.unibs.dii.pajc.pig.client.bean.generic;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;

public class Rule {

    public enum COMPARATOR {
        EQUAL_MAJORITY(">="),
        MAJORITY(">"),
        EQUALITY("="),
        MINORITY("<"),
        EQUAL_MINORITY("<=");

        private String symbol;

        private COMPARATOR(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }

        public static COMPARATOR getBySymbol(String symbol) {
            for (COMPARATOR c : COMPARATOR.values())
                if (c.getSymbol().equals(symbol))
                    return c;
            return null;
        }
    }

    private String id;
    private String sensorId;
    private COMPARATOR comparator;
    private Object data;
    private Action action;
    private String sensorDescription, deviceDescription;

    public Rule(String id, Sensor sensor, COMPARATOR comparator, Object data, Action action) {
        this(id, sensor.getID(), comparator, data, action);
    }
    public Rule(String id, String sensor, COMPARATOR comparator, Object data, Action action) {
        this.id = id;
        this.sensorId = sensor;
        this.comparator = comparator;
        this.data = data;
        this.action = action;
        this.sensorDescription = null;
        this.deviceDescription = null;

        if (this.id == null)
            throw  new IllegalArgumentException("Rule(): id can't be null");
        if (this.sensorId == null)
            throw  new IllegalArgumentException("Rule(): sensor can't be null");
        if (this.comparator == null)
            throw  new IllegalArgumentException("Rule(): comparator can't be null");
        if (this.data == null)
            throw  new IllegalArgumentException("Rule(): data can't be null");
        if (this.action == null)
            throw  new IllegalArgumentException("Rule(): action can't be null");
    }

    public String getID() {
        return id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public String getSensorDescription() {
        return sensorDescription;
    }

    public void setSensorDescription(String sensorDescription) {
        this.sensorDescription = sensorDescription;
    }

    public String getDeviceDescription() {
        return deviceDescription;
    }

    public void setDeviceDescription(String deviceDescription) {
        this.deviceDescription = deviceDescription;
    }

    public COMPARATOR getComparator() {
        return comparator;
    }

    public Object getData() {
        return data;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rule)
            return id.equals(((Rule)obj).getID());

        return super.equals(obj);
    }
}
