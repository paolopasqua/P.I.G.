import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Activity implements Runnable{

    private UUID activityUUID;
    private int durata;
    private Timestamp timestamp;
    private long initialDelay;
    private int repetition;

    private Action action;

    public Activity(ArrayList<String> parsedMessage){

        action = new Action(UUID.fromString(parsedMessage.get(8)), Integer.parseInt(parsedMessage.get(7)), Integer.parseInt(parsedMessage.get(9)), Integer.parseInt(parsedMessage.get(10)));
        activityUUID = UUID.fromString(parsedMessage.get(3));
        durata = Integer.parseInt(parsedMessage.get(5));
        repetition = Integer.parseInt(parsedMessage.get(6));
        timestamp = Timestamp.valueOf(parsedMessage.get(4));
        initialDelay = timestamp.getTime()-System.currentTimeMillis();

    }

    @Override
    public void run() {

        action.setValueOn();
        try {
            TimeUnit.MINUTES.sleep(durata);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        action.setValueOff();


    }

    public long getInitialDelay() {
        return initialDelay;
    }

    public UUID getActivityUUID() {
        return activityUUID;
    }

    @Override
    public String toString () {

        return "{010,"+activityUUID+","+timestamp+","+durata+","+repetition+","+"{"+"012"+","+action.getDeviceUUID()+","+action.getValueOn()+","+action.getValueOff()+"}"+"}";
    }

    public int getRepetition() {
        return repetition;
    }
}
