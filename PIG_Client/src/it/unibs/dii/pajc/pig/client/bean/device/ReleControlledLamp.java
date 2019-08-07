package it.unibs.dii.pajc.pig.client.bean.device;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Status;

import java.util.ResourceBundle;

public class ReleControlledLamp extends Device {

    public enum LAMP_STATUS implements Status {
        ON(1, localizationBundle.getString("relecontrolledlamp.status.on.description")),
        OFF(0, localizationBundle.getString("relecontrolledlamp.status.off.description"));

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

    private LAMP_STATUS status;
    private int controlGPIO;
    private boolean NC_NO; //Normally closed (false) or normally opened (true)

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/bean/Device");

    public ReleControlledLamp(String id) throws IllegalArgumentException {
        super(id);
    }

    public ReleControlledLamp(String id, String description) throws IllegalArgumentException {
        super(id, description);
    }

    @Override
    public void setStatus(Status status) throws IllegalArgumentException {
        if (status instanceof LAMP_STATUS)
            this.status = (LAMP_STATUS) status;

        throw new IllegalArgumentException("ReleControlledLamp.setStatus: status must be LAMP_STATUS type");
    }

    @Override
    public Status getStatus() {
        return status;
    }

    public int getControlGPIO() {
        return controlGPIO;
    }

    public void setControlGPIO(int controlGPIO) {
        this.controlGPIO = controlGPIO;
    }

    public boolean isNC_NO() {
        return NC_NO;
    }

    public void setNC_NO(boolean NC_NO) {
        this.NC_NO = NC_NO;
    }
}
