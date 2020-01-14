import java.util.HashMap;
import java.util.UUID;

public class GreenHouse {
    //This class is used to manage all sensor and device, and the emulator

    //Sensors
    private TempSensor tempSensor;
    private WaterSensor waterSensor;

    //Devices
    private Fan fan;
    private WaterPump waterPump;
    private HeatResistor heatResistor;
    private Lamp lamp;
    private static HashMap greenHmap;
    public static ActivityScheduler sched = new ActivityScheduler();
    public static RuleScheduler rulSched = new RuleScheduler();

    public GreenHouse() {

        greenHmap = new HashMap<UUID, Hardware>();

        //Inizializzazione sensori
        tempSensor = new TempSensor("FirstTemperatureSensor");
        waterSensor = new WaterSensor("FirstWaterSensor");

        //Inizializzazione devices
        fan = new Fan("FirstFan");
        waterPump = new WaterPump("FirstWaterPump");
        heatResistor = new HeatResistor("FirstHeatResistor");
        lamp = new Lamp("FirstLamp");

        greenHmap.put(tempSensor.getUUID(), tempSensor);
        greenHmap.put(waterSensor.getUUID(), waterSensor);

        greenHmap.put(fan.getUUID(), fan);
        greenHmap.put(waterPump.getUUID(), waterPump);
        greenHmap.put(heatResistor.getUUID(), heatResistor);
        greenHmap.put(lamp.getUUID(), lamp);

    }

    public boolean getValueSensor(){
        return true;
    }

    public static HashMap getGreenHmap() {
        return greenHmap;
    }
}
