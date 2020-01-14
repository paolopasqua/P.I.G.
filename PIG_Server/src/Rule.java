import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Rule {

    private UUID idRegola;
    private UUID idSensor;
    private String comparator;
    private int comparedValue;
    private Action action;
    private Multi t1;
    private int sensorValue;


    public Rule (ArrayList<String> parsedMessage) {

        idRegola = UUID.fromString(parsedMessage.get(3));
        idSensor = UUID.fromString(parsedMessage.get(4));
        comparator = parsedMessage.get(5);
        comparedValue = Integer.parseInt(parsedMessage.get(6));
        action = new Action(UUID.fromString(parsedMessage.get(8)), Integer.parseInt(parsedMessage.get(7)), Integer.parseInt(parsedMessage.get(9)), Integer.parseInt(parsedMessage.get(10)));
        t1=new Multi();
        t1.start();

    }

    class Multi extends Thread{
        public void run() {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (verifyCondition(comparator, comparedValue)){
                    action.setValueOff();
                } else {
                    action.setValueOn();
                }
            }
        }
    }

    public void stopRule() {

        this.t1.stop();

    }

    public UUID getIdRegola() {
        return idRegola;
    }

    public boolean verifyCondition (String comparatore, int datoComparato){

        sensorValue = ((Sensor) GreenHouse.getGreenHmap().get(idSensor)).getValue();

        switch (comparatore) {

            case ">":
                if (datoComparato>sensorValue) { return true; }
                break;

            case "<":
                if (datoComparato<sensorValue) { return true; }
                break;

            case ">=":
                if (datoComparato>=sensorValue) { return true; }
                break;

            case "<=":
                if (datoComparato<=sensorValue) { return true; }
                break;

            case "=":
                if (datoComparato==sensorValue) { return true; }
                break;

            default:
                break;

        }
        return false;
    }

    @Override
    public String toString() {

        return "{011,"+idRegola+","+idSensor+","+comparator+","+comparedValue+","+"{"+"012"+","+action.getDeviceUUID()+","+action.getValueOn()+","+action.getValueOff()+"}"+"}";
    }


}