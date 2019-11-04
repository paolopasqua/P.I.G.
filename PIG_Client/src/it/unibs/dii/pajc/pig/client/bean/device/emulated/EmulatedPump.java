package it.unibs.dii.pajc.pig.client.bean.device.emulated;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Status;
import it.unibs.dii.pajc.pig.client.bean.generic.Action;
import it.unibs.dii.pajc.pig.client.view.renderer.PumpRenderer;

import java.util.ResourceBundle;

public class EmulatedPump extends Device {

    public enum PUMP_STATUS implements Status {
        POWER_3(1f, localizationBundle.getString("emulatedpump.status.power3.description")),
        POWER_2(0.5f, localizationBundle.getString("emulatedpump.status.power2.description")),
        POWER_1(0.25f, localizationBundle.getString("emulatedpump.status.power1.description")),
        OFF(0f, localizationBundle.getString("emulatedpump.status.off.description"));

        private Object value;
        private String description;

        private PUMP_STATUS(Object value, String description) {
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

        public static PUMP_STATUS fromValue(float value) {
            if (value == (float)POWER_3.getValue())
                return POWER_3;
            else if (value == (float)POWER_2.getValue())
                return POWER_2;
            else if (value == (float)POWER_1.getValue())
                return POWER_1;
            else if (value == (float)OFF.getValue())
                return OFF;
            else
                return null;
        }
    }

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/bean/Device");

    private PUMP_STATUS status;
    private PumpRenderer renderer;

    public EmulatedPump(String id) throws IllegalArgumentException {
        this(id, localizationBundle.getString("emulatedpump.description.default"));
    }

    public EmulatedPump(String id, String description) throws IllegalArgumentException {
        super(id, description);

        initActions();
        renderer = new PumpRenderer(this);
        drawer = renderer;
    }

    private void initActions() {
        this.actions = new Action[4];

        this.actions[0] = new Action("1", localizationBundle.getString("emulatedpump.action.1.description"));
        this.actions[1] = new Action("2", localizationBundle.getString("emulatedpump.action.2.description"));
        this.actions[2] = new Action("3", localizationBundle.getString("emulatedpump.action.3.description"));

        this.actions[3] = new Action("4", localizationBundle.getString("emulatedpump.action.4.description"));

        this.actions[0].setTerminationAction(this.actions[3]);
        this.actions[1].setTerminationAction(this.actions[3]);
        this.actions[2].setTerminationAction(this.actions[3]);
    }

    @Override
    public void setStatus(Status status) {
        if (status instanceof PUMP_STATUS) {
            this.status = (PUMP_STATUS) status;
            renderer.setStatus(this.status);
            renderer.setTooltiptext(this.description + ": " + this.status.description);
        } else
            throw new IllegalArgumentException("EmulatedPump.setStatus: status must be PUMP_STATUS type");
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
