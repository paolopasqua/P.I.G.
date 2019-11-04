package it.unibs.dii.pajc.pig.client.bean.sensor;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;
import it.unibs.dii.pajc.pig.client.view.renderer.WaterSensorRenderer;

import java.util.ResourceBundle;

public class EmulatedWaterSensor extends Sensor {

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/bean/Sensor");

    private WaterSensorRenderer renderer;

    public EmulatedWaterSensor(String id) throws IllegalArgumentException {
        this(id, localizationBundle.getString("emulatedwatersensor.description.default"));
    }

    public EmulatedWaterSensor(String id, String description) throws IllegalArgumentException {
        super(id, description);

        this.renderer = new WaterSensorRenderer(this);
        this.drawer = renderer;
    }

    @Override
    public void setData(int data) {
        super.setData(data);
        renderer.setData(data);
        renderer.setTooltiptext(this.description + ": " + data);
    }
}
