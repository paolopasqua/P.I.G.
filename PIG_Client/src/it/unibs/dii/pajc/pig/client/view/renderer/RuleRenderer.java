package it.unibs.dii.pajc.pig.client.view.renderer;

import it.unibs.dii.pajc.pig.client.bean.generic.Activity;
import it.unibs.dii.pajc.pig.client.bean.generic.Rule;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class RuleRenderer extends JPanel implements ListCellRenderer<Rule> {
    String ruleLabel = "";
    String actionLabel = "";
    String deviceLabel = "";
    private JLabel line1;
    private JLabel line2;
    private JLabel line3;

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/view/renderer/RuleRenderer");

    public RuleRenderer() {

        ruleLabel = localizationBundle.getString("label.rule");
        actionLabel = localizationBundle.getString("label.action");
        deviceLabel = localizationBundle.getString("label.device");

        line1 = new JLabel();
        line2 = new JLabel();
        line3 = new JLabel();

        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK),
                        BorderFactory.createEmptyBorder(0, 5, 0, 5)
                )
        );
        setLayout(new GridLayout(2, 0));

        //add(line1);
        add(line2);
        add(line3);
        //add(line4);

    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Rule> jList, Rule rule, int index, boolean isSelected, boolean cellHasFocus) {
        String tmp = "";

        line1.setText(rule.getID());

        tmp = ruleLabel;
        if (rule.getSensorDescription() == null || rule.getSensorDescription().isEmpty())
            tmp += rule.getSensorId();
        else
            tmp += rule.getSensorDescription();
        tmp += " " + rule.getComparator().getSymbol() + " " + rule.getData().toString();
        line2.setText(tmp);

        if (rule.getAction().getDescription() == null || rule.getAction().getDescription().isEmpty())
            tmp = actionLabel + rule.getAction().getOnStatusValue().toString();
        else
            tmp = rule.getAction().getDescription();

        if (rule.getDeviceDescription() == null || rule.getDeviceDescription().isEmpty())
            tmp += " " + deviceLabel + rule.getAction().getIdDevice();
        else
            tmp += " " + rule.getDeviceDescription();
        line3.setText(tmp);

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
