package it.unibs.dii.pajc.pig.client.view.renderer;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Status;
import it.unibs.dii.pajc.pig.client.bean.generic.Activity;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class ActivityRenderer extends JPanel implements ListCellRenderer<Activity> {

    String actionLabel = "";
    String deviceLabel = "";
    String durationLabel = "";
    String repetitionLabel = "";
    private JLabel line1;
    private JLabel line2;
    private JLabel line3;
    private JLabel line4;

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/view/renderer/ActivityRenderer");

    public ActivityRenderer() {

        actionLabel = localizationBundle.getString("label.action");
        deviceLabel = localizationBundle.getString("label.device");
        durationLabel = localizationBundle.getString("label.duration");
        repetitionLabel = localizationBundle.getString("label.repetition");

        line1 = new JLabel();
        line2 = new JLabel();
        line3 = new JLabel();
        line4 = new JLabel();

        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK),
                        BorderFactory.createEmptyBorder(0, 5, 0, 5)
                )
        );
        setLayout(new GridLayout(3, 0));

        //add(line1);
        add(line2);
        add(line3);
        add(line4);

    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Activity> jList, Activity activity, int index, boolean isSelected, boolean cellHasFocus) {
        String tmp = "";

        line1.setText(activity.getID());

        if (activity.getAction().getDescription() == null || activity.getAction().getDescription().isEmpty())
            tmp = actionLabel + activity.getAction().getOnStatusValue().toString();
        else
            tmp = activity.getAction().getDescription();

        if (activity.getDeviceDescription() == null || activity.getDeviceDescription().isEmpty())
            tmp += " " + deviceLabel + activity.getDeviceId();
        else
            tmp += " " + activity.getDeviceDescription();
        line2.setText(tmp);

        line3.setText(durationLabel + activity.getDuration() + " " + Activity.REPETITION.MINUTES.getLongRepresentation());

        line4.setText(repetitionLabel + activity.getRepetitionValue() + " " + activity.getRepetitionUnits().getLongRepresentation());

        if (isSelected) {
            setBackground(jList.getSelectionBackground());
            setForeground(jList.getSelectionForeground());
        }
        else {
            setBackground(jList.getBackground());
            setForeground(jList.getForeground());
        }

        //this.setMaximumSize(jList.getPreferredSize());

        return this;
    }
}
