package it.unibs.dii.pajc.pig.client.bean.device.emulated;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Status;
import it.unibs.dii.pajc.pig.client.bean.generic.Action;
import it.unibs.dii.pajc.pig.client.bean.generic.ActionParameter;
import it.unibs.dii.pajc.pig.client.bean.generic.ActionParameterData;
import it.unibs.dii.pajc.pig.client.view.renderer.InputRenderer;
import it.unibs.dii.pajc.pig.client.view.renderer.ThreeLevelParameterRenderer;

import java.util.ResourceBundle;

public class EmulatedPump extends Device {

    public enum PUMP_STATUS implements Status {
        POWER_3(1f, localizationBundle.getString("pump.status.power3.description")),
        POWER_2(0.5f, localizationBundle.getString("pump.status.power2.description")),
        POWER_1(0.25f, localizationBundle.getString("pump.status.power1.description")),
        OFF(0f, localizationBundle.getString("pump.status.off.description"));

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
    }

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/bean/Device");

    private PUMP_STATUS status;

    public EmulatedPump(String id) throws IllegalArgumentException {
        this(id, null);
    }

    public EmulatedPump(String id, String description) throws IllegalArgumentException {
        super(id, description);

        initActions();
    }

    private void initActions() {
        this.actions = new Action[2];

        this.actions[0] = new Action("1", localizationBundle.getString("pump.action.1.description"));
        ActionParameter[] parameters = new ActionParameter[1];
        parameters[0] = new ActionParameter("1", localizationBundle.getString("pump.action.1.parameter.1.description"), Float.class);
        this.actions[0].setParameters(parameters);

        InputRenderer<Action, ActionParameterData> renderer = new ThreeLevelParameterRenderer((Float)PUMP_STATUS.POWER_1.getValue(), (Float)PUMP_STATUS.POWER_2.getValue(), (Float)PUMP_STATUS.POWER_3.getValue());
        this.actions[0].setParameterRenderer(renderer);

        this.actions[1] = new Action("2", localizationBundle.getString("pump.action.2.description"));

        this.actions[0].setTerminationAction(this.actions[1]);
    }

    @Override
    public void setStatus(Status status) {
        if (status instanceof PUMP_STATUS)
            this.status = (PUMP_STATUS) status;
        else
            throw new IllegalArgumentException("EmulatedPump.setStatus: status must be PUMP_STATUS type");
    }

    @Override
    public Status getStatus() {
        return status;
    }
}
