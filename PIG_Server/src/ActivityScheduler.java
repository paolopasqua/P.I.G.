import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.*;

public class ActivityScheduler{

    public static final int MAX_ACTIVITY_NUMBER = 1000;

    public ScheduledThreadPoolExecutor service;

    public HashMap<UUID, Activity> activityList;

    public ActivityScheduler() {

        service = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(MAX_ACTIVITY_NUMBER);
        activityList = new HashMap<>();

    }

    public void addActivity(Activity activity, UUID idActivity) {

        service.scheduleAtFixedRate(activity, activity.getInitialDelay(), activity.getRepetition()*60*1000, TimeUnit.MILLISECONDS);
        activityList.put(activity.getActivityUUID(), activity);

    }

    public void removeActivity (UUID idActivity) {

        service.remove(activityList.get(idActivity));
        activityList.remove(idActivity);

    }



}
