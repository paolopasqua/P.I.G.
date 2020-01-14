package it.unibs.dii.pajc.pig.client.bean.device.emulated;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Status;
import it.unibs.dii.pajc.pig.client.bean.generic.Action;
import it.unibs.dii.pajc.pig.client.view.renderer.LampRenderer;

import java.util.ResourceBundle;

public class EmulatedLamp extends Device {

    public enum LAMP_STATUS implements Status {
        ON(100, localizationBundle.getString("emulatedlamp.status.on.description")),
        OFF(0, localizationBundle.getString("emulatedlamp.status.off.description"));

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

        public static LAMP_STATUS fromValue(int value) {
            if (value == (int)ON.getValue())
                return ON;
            else if (value == (int)OFF.getValue())
                return OFF;
            else
                return null;
        }
    }

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/bean/Device");

    protected LAMP_STATUS status;
    private LampRenderer renderer;

    public EmulatedLamp(String id) throws IllegalArgumentException {
        this(id, localizationBundle.getString("emulatedlamp.description.default"));
    }

    public EmulatedLamp(String id, String description) throws IllegalArgumentException {
        super(id, description);

        initActions();
        renderer = new LampRenderer(this);
        drawer = renderer;
    }

    private void initActions() {
        this.actions = new Action[2];

        this.actions[0] = new Action(id, LAMP_STATUS.ON.getValue(), localizationBundle.getString("emulatedlamp.action.1.description"));
        this.actions[1] = new Action(id, LAMP_STATUS.OFF.getValue(), localizationBundle.getString("emulatedlamp.action.2.description"));

        this.actions[0].setTerminationAction(this.actions[1]);
    }

    @Override
    public void setStatus(Status status) throws IllegalArgumentException {
        if (status instanceof LAMP_STATUS) {
            this.status = (LAMP_STATUS) status;
            renderer.setStatus(this.status);
            renderer.setTooltiptext(this.description + ": " + this.status.description);
        } else
            throw new IllegalArgumentException("EmulatedLamp.setStatus: status must be LAMP_STATUS type");
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
