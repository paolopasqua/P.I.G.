package it.unibs.dii.pajc.pig.client.bean.device.emulated;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Status;
import it.unibs.dii.pajc.pig.client.bean.generic.Action;
import it.unibs.dii.pajc.pig.client.bean.generic.ActionParameter;
import it.unibs.dii.pajc.pig.client.bean.generic.ActionParameterData;
import it.unibs.dii.pajc.pig.client.view.renderer.InputRenderer;
import it.unibs.dii.pajc.pig.client.view.renderer.SliderParameterRenderer;

import java.util.ResourceBundle;

public class EmulatedTempResistor extends Device {

    private static final int MIN_TEMP = 0;
    private static final int MAX_TEMP = 40;

    public enum TEMP_RESISTOR_STATUS implements Status {
        ON(1, localizationBundle.getString("tempResistor.status.on.description")),
        OFF(0, localizationBundle.getString("tempResistor.status.off.description"));

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
    }

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/bean/Device");

    private TEMP_RESISTOR_STATUS status;

    public EmulatedTempResistor(String id) throws IllegalArgumentException {
        this(id, null);
    }

    public EmulatedTempResistor(String id, String description) throws IllegalArgumentException {
        super(id, description);

        initActions();
    }

    private void initActions() {
        this.actions = new Action[2];

        this.actions[0] = new Action("1", localizationBundle.getString("tempResistor.action.1.description"));
        ActionParameter[] parameters = new ActionParameter[1];
        parameters[0] = new ActionParameter("1", localizationBundle.getString("tempResistor.action.1.parameter.1.description"), Integer.class);
        this.actions[0].setParameters(parameters);

        InputRenderer<Action, ActionParameterData> renderer = new SliderParameterRenderer(MIN_TEMP, MAX_TEMP, MIN_TEMP);
        this.actions[0].setParameterRenderer(renderer);

        this.actions[1] = new Action("2", localizationBundle.getString("tempResistor.action.2.description"));

        this.actions[0].setTerminationAction(this.actions[1]);
    }

    @Override
    public void setStatus(Status status) {
        if (status instanceof TEMP_RESISTOR_STATUS)
            this.status = (TEMP_RESISTOR_STATUS) status;
        else
            throw new IllegalArgumentException("EmulatedTempResistor.setStatus: status must be TEMP_RESISTOR_STATUS type");
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
