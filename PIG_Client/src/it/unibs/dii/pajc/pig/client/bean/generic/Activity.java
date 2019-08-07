package it.unibs.dii.pajc.pig.client.bean.generic;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;

public class Activity {

    public enum REPETITION {
        DAYS("dd", "days"),
        HOURS("hh", "hours"),
        MINUTES("mm", "minutes");

        private String shortRepresentation, longRepresentation;

        private REPETITION(String shortRepresentation, String longRepresentation) {
            this.shortRepresentation = shortRepresentation;
            this.longRepresentation = longRepresentation;
        }

        public String getShortRepresentation() {
            return shortRepresentation;
        }

        public String getLongRepresentation() {
            return longRepresentation;
        }
    }

    private String id;
    private Device device;
    private Action action;
    private ArrayList<ActionParameterData> parametersData;
    private Date execution;
    private Duration duration;
    private int repetitionValue;
    private REPETITION repetitionUnits;

    public Activity(String id, Device device, Action action, Date execution) throws IllegalArgumentException {
        this.id = id;
        this.device = device;
        this.action = action;
        this.execution = execution;

        if (this.id == null)
            throw  new IllegalArgumentException("Activity(): id can't be null");
        if (this.device == null)
            throw  new IllegalArgumentException("Activity(): device can't be null");
        if (this.action == null)
            throw  new IllegalArgumentException("Activity(): action can't be null");
        if (this.execution == null)
            throw  new IllegalArgumentException("Activity(): execution date can't be null");
    }

    public String getID() {
        return id;
    }

    public Device getDevice() {
        return device;
    }

    public Action getAction() {
        return action;
    }

    public ArrayList<ActionParameterData> getParametersData() {
        return parametersData;
    }

    public Date getExecution() {
        return execution;
    }

    public Duration getDuration() {
        return duration;
    }

    public int getRepetitionValue() {
        return repetitionValue;
    }

    public REPETITION getRepetitionUnits() {
        return repetitionUnits;
    }

    public void addParameterData(ActionParameterData parameterData) {
        if (parameterData != null) {
            if (this.parametersData == null)
                this.parametersData = new ArrayList<>();

            this.parametersData.add(parameterData);
        }
    }

    public void setParametersData(ArrayList<ActionParameterData> parametersData) {
        this.parametersData = parametersData;
    }

    public void setDuration(Duration duration) {
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
