package it.unibs.dii.pajc.pig.client.bean.sensor;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;
import it.unibs.dii.pajc.pig.client.view.renderer.TempSensorRenderer;

import java.util.ResourceBundle;

public class EmulatedTempSensor extends Sensor {

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/bean/Sensor");

    private TempSensorRenderer renderer;

    public EmulatedTempSensor(String id) throws IllegalArgumentException {
        this(id, localizationBundle.getString("emulatedtempSensor.description.default"));
    }

    public EmulatedTempSensor(String id, String description) throws IllegalArgumentException {
        super(id, description);

        this.renderer = new TempSensorRenderer(this);
        this.drawer = renderer;
    }

    @Override
    public void setData(int data) {
        super.setData(data);
        renderer.setData(data);
        renderer.setTooltiptext(this.description + ": " + data);
    }
}
