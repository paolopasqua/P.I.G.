package it.unibs.dii.pajc.pig.client.bean.device.emulated;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Status;
import it.unibs.dii.pajc.pig.client.bean.generic.Action;
import it.unibs.dii.pajc.pig.client.view.renderer.FanRenderer;

import java.util.ResourceBundle;

public class EmulatedFan extends Device {

    public enum FAN_STATUS implements Status {
        POWER_3(100, localizationBundle.getString("emulatedfan.status.power3.description")),
        POWER_2(66, localizationBundle.getString("emulatedfan.status.power2.description")),
        POWER_1(33, localizationBundle.getString("emulatedfan.status.power1.description")),
        OFF(0, localizationBundle.getString("emulatedfan.status.off.description"));

        private Object value;
        private String description;

        private FAN_STATUS(Object value, String description) {
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

        public static FAN_STATUS fromValue(float value) {
            if (value == (int)POWER_3.getValue())
                return POWER_3;
            else if (value == (int)POWER_2.getValue())
                return POWER_2;
            else if (value == (int)POWER_1.getValue())
                return POWER_1;
            else if (value == (int)OFF.getValue())
                return OFF;
            else
                return null;
        }
    }

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/bean/Device");

    private FAN_STATUS status;
    private FanRenderer renderer;

    public EmulatedFan(String id) throws IllegalArgumentException {
        this(id, localizationBundle.getString("emulatedfan.description.default"));
    }

    public EmulatedFan(String id, String description) throws IllegalArgumentException {
        super(id, description);

        initActions();
        renderer = new FanRenderer(this);
        drawer = renderer;
    }

    private void initActions() {
        this.actions = new Action[4];

        this.actions[0] = new Action(id, FAN_STATUS.POWER_1.getValue(), localizationBundle.getString("emulatedfan.action.1.description"));
        this.actions[1] = new Action(id, FAN_STATUS.POWER_2.getValue(), localizationBundle.getString("emulatedfan.action.2.description"));
        this.actions[2] = new Action(id, FAN_STATUS.POWER_3.getValue(), localizationBundle.getString("emulatedfan.action.3.description"));

        this.actions[3] = new Action(id, FAN_STATUS.OFF.getValue(), localizationBundle.getString("emulatedfan.action.4.description"));

        this.actions[0].setTerminationAction(this.actions[3]);
        this.actions[1].setTerminationAction(this.actions[3]);
        this.actions[2].setTerminationAction(this.actions[3]);
    }

    @Override
    public void setStatus(Status status) {
        if (status instanceof FAN_STATUS) {
            this.status = (FAN_STATUS) status;
            renderer.setStatus(this.status);
            renderer.setTooltiptext(this.description + ": " + this.status.description);
        }
        else
            throw new IllegalArgumentException("EmulatedFan.setStatus: status must be FAN_STATUS type");
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
