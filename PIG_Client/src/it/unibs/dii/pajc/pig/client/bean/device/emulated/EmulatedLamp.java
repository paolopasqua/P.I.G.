package it.unibs.dii.pajc.pig.client.bean.device.emulated;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Status;
import it.unibs.dii.pajc.pig.client.bean.generic.Action;

import java.util.ResourceBundle;

public class EmulatedLamp extends Device {

    public enum LAMP_STATUS implements Status {
        ON(1, localizationBundle.getString("lamp.status.on.description")),
        OFF(0, localizationBundle.getString("lamp.status.off.description"));

        private Object value;
        private String description;

        private LAMP_STATUS(Object value, String description) {
            this.value = value;
            this.description = description;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/bean/Device");

    protected LAMP_STATUS status;

    public EmulatedLamp(String id) throws IllegalArgumentException {
        this(id, null);
    }

    public EmulatedLamp(String id, String description) throws IllegalArgumentException {
        super(id, description);

        initActions();
    }

    private void initActions() {
        this.actions = new Action[2];

        this.actions[0] = new Action("1", localizationBundle.getString("lamp.action.1.description"));
        this.actions[1] = new Action("2", localizationBundle.getString("lamp.action.2.description"));

        this.actions[0].setTerminationAction(this.actions[1]);
    }

    @Override
    public void setStatus(Status status) throws IllegalArgumentException {
        if (status instanceof LAMP_STATUS)
            this.status = (LAMP_STATUS) status;
        else
            throw new IllegalArgumentException("EmulatedLamp.setStatus: status must be LAMP_STATUS type");
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
