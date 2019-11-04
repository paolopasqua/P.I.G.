package it.unibs.dii.pajc.pig.client.bean.generic;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;

import java.util.Date;
import java.util.ResourceBundle;

public class Activity {

    public enum REPETITION {
        DAYS("dd", localizationBundle.getString("repetition.days.short"), localizationBundle.getString("repetition.days.long")),
        HOURS("hh", localizationBundle.getString("repetition.hours.short"), localizationBundle.getString("repetition.hours.long")),
        MINUTES("mm", localizationBundle.getString("repetition.minutes.short"), localizationBundle.getString("repetition.minutes.long"));

        private String code, shortRepresentation, longRepresentation;

        private REPETITION(String code, String shortRepresentation, String longRepresentation) {
            this.code = code;
            this.shortRepresentation = shortRepresentation;
            this.longRepresentation = longRepresentation;
        }

        public String getCode() { return code; }

        public String getShortRepresentation() {
            return shortRepresentation;
        }

        public String getLongRepresentation() {
            return longRepresentation;
        }

        public static REPETITION getByCode(String code) {
            for (REPETITION r : REPETITION.values())
                if (r.getCode().equals(code))
                    return r;
            return null;
        }

        public static REPETITION getByDescription(String desc, boolean shortLong) {
            for(REPETITION r : REPETITION.values()) {
                if (shortLong) {
                    if (desc.equals(r.longRepresentation))
                        return r;
                } else {
                    if (desc.equals(r.shortRepresentation))
                        return r;
                }
            }
            return null;
        }
    }

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/bean/Activity");

    private String id;
    private String deviceId;
    private String actionId;
    private Date execution;
    private int duration;
    private int repetitionValue;
    private REPETITION repetitionUnits;

    public Activity(String id, Device device, Action action, Date execution) throws IllegalArgumentException {
        this(id, device.getID(), action.getID(), execution);
    }

    public Activity(String id, String device, String action, Date execution) throws IllegalArgumentException {
        this.id = id;
        this.deviceId = device;
        this.actionId = action;
        this.execution = execution;

        if (this.id == null)
            throw  new IllegalArgumentException("Activity(): id can't be null");
        if (this.deviceId == null)
            throw  new IllegalArgumentException("Activity(): device can't be null");
        if (this.actionId == null)
            throw  new IllegalArgumentException("Activity(): action can't be null");
        if (this.execution == null)
            throw  new IllegalArgumentException("Activity(): execution date can't be null");
    }

    public String getID() {
        return id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getActionId() {
        return actionId;
    }

    public Date getExecution() {
        return execution;
    }

    public int getDuration() {
        return duration;
    }

    public int getRepetitionValue() {
        return repetitionValue;
    }

    public REPETITION getRepetitionUnits() {
        return repetitionUnits;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setRepetitionValue(int repetitionValue) {
        this.repetitionValue = repetitionValue;
    }

    public void setRepetitionUnits(REPETITION repetitionUnits) {
        this.repetitionUnits = repetitionUnits;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Activity)
            return id.equals(((Activity)obj).getID());

        return super.equals(obj);
    }
}
