package it.unibs.dii.pajc.pig.client.view.component.generalpurpouse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RadioButtonGroupPanel extends JPanel {

    private JPanel background;
    private ActionListener radioActionListener;

    public RadioButtonGroupPanel() {
        background = this;

        radioActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getSource() instanceof  JRadioButton) {
                    JRadioButton radio = (JRadioButton)actionEvent.getSource();

                    radio.setSelected(true);

                    for (Component cmp : background.getComponents()) {
                        if (cmp instanceof JRadioButton)
                            if (!cmp.equals(radio))
                                ((JRadioButton)cmp).setSelected(false);
                    }
                }
            }
        };

        initComponent();
    }
    
    private void initComponent() {
        super.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
    }

    public JRadioButton getSelected() {
        for (Component cmp : background.getComponents()) {
            if (cmp instanceof JRadioButton)
                if (((JRadioButton)cmp).isSelected())
                    return ((JRadioButton)cmp);
        }

        return null;
    }

    public Component add(JRadioButton radio) {
        radio.addActionListener(radioActionListener);
        return super.add(radio);
    }

    @Override
    @Deprecated
    public Component add(Component comp) {
        return null;
    }

    @Override
    @Deprecated
    public Component add(String name, Component comp) {
        return null;
    }

    @Override
    @Deprecated
    public Component add(Component comp, int index) {
        return null;
    }

    @Override
    @Deprecated
    public void setLayout(LayoutManager mgr) {
        //Do Nothing
    }
}
