package it.unibs.dii.pajc.pig.client.bean.generic;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Status;

public class Action {

    protected String idDevice;
    protected String description;
    protected Action terminationAction;
    protected Object onStatusValue;

    public Action(String idDevice, Object onStatusValue) throws IllegalArgumentException {
        this.idDevice = idDevice;
        this.onStatusValue = onStatusValue;

        if (this.onStatusValue == null)
            throw  new IllegalArgumentException("Action(): on status can't be null");

        this.description = null;
        this.terminationAction = null;
    }

    public Action(String idDevice, Object onStatusValue, String description) throws IllegalArgumentException {
        this(idDevice, onStatusValue);
        setDescription(description);
    }

    public String getIdDevice() {
        return idDevice;
    }

    public Object getOnStatusValue() {
        return onStatusValue;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Action getTerminationAction() {
        return terminationAction;
    }

    public void setTerminationAction(Action action) {
        this.terminationAction = action;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Action)
            return onStatusValue.equals(((Action)obj).getOnStatusValue()) && idDevice.equals(((Action)obj).getIdDevice());

        return super.equals(obj);
    }
}
