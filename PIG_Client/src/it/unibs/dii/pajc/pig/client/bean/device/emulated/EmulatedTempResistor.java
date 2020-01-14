package it.unibs.dii.pajc.pig.client.bean.device.emulated;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Status;
import it.unibs.dii.pajc.pig.client.bean.generic.Action;
import it.unibs.dii.pajc.pig.client.view.renderer.TempResistorRenderer;

import java.util.ResourceBundle;

public class EmulatedTempResistor extends Device {

    public enum TEMP_RESISTOR_STATUS implements Status {
        ON(100, localizationBundle.getString("emulatedtempResistor.status.on.description")),
        OFF(0, localizationBundle.getString("emulatedtempResistor.status.off.description"));

        private Object value;
        private String description;

        private TEMP_RESISTOR_STATUS(Object value, String description) {
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

        public static TEMP_RESISTOR_STATUS fromValue(int value) {
            if (value == (int)ON.getValue())
                return ON;
            else if (value == (int)OFF.getValue())
                return OFF;
            else
                return null;
        }
    }

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/bean/Device");

    private TEMP_RESISTOR_STATUS status;
    private TempResistorRenderer renderer;

    public EmulatedTempResistor(String id) throws IllegalArgumentException {
        this(id, localizationBundle.getString("emulatedtempResistor.description.default"));
    }

    public EmulatedTempResistor(String id, String description) throws IllegalArgumentException {
        super(id, description);

        initActions();
        renderer = new TempResistorRenderer(this);
        drawer = renderer;
    }

    private void initActions() {
        this.actions = new Action[2];

        this.actions[0] = new Action(id, TEMP_RESISTOR_STATUS.ON.getValue(), localizationBundle.getString("emulatedtempResistor.action.1.description"));
        this.actions[1] = new Action(id, TEMP_RESISTOR_STATUS.OFF.getValue(), localizationBundle.getString("emulatedtempResistor.action.2.description"));

        this.actions[0].setTerminationAction(this.actions[1]);
    }

    @Override
    public void setStatus(Status status) {
        if (status instanceof TEMP_RESISTOR_STATUS) {
            this.status = (TEMP_RESISTOR_STATUS) status;
            this.renderer.setStatus(this.status);
            this.renderer.setTooltiptext(this.description + ": " + this.status.description);
        }
        else
            throw new IllegalArgumentException("EmulatedTempResistor.setStatus: status must be TEMP_RESISTOR_STATUS type");
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
