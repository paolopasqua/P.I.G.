package it.unibs.dii.pajc.pig.client.view.renderer;

import it.unibs.dii.pajc.pig.client.bean.generic.Action;
import it.unibs.dii.pajc.pig.client.bean.generic.ActionParameter;
import it.unibs.dii.pajc.pig.client.bean.generic.ActionParameterData;
import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.LabeledComponent;

import javax.swing.*;
import java.awt.*;

public class SliderParameterRenderer implements InputRenderer<Action, ActionParameterData> {

    private ActionParameter paramReference;
    private JSlider field;
    private LabeledComponent paramComponent;

    private int minValue, maxValue, currentValue;

    public SliderParameterRenderer(int minValue, int maxValue, int currentValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = currentValue;

        JLabel valueText = new JLabel(""+currentValue);
        valueText.setHorizontalAlignment(JLabel.CENTER);

        field = new JSlider(minValue, maxValue, currentValue);
        field.addChangeListener(changeEvent -> valueText.setText(""+field.getValue()));

        JLabel min = new JLabel(""+minValue);
        JLabel max = new JLabel(""+maxValue);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
        panel.add(min, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        panel.add(max, BorderLayout.EAST);
        panel.add(valueText, BorderLayout.SOUTH);

        paramComponent = new LabeledComponent(panel);
    }

    @Override
    public Component getInputsComponent(Component container, Action source) {
        if (source.hasParameters()) {
            paramReference = source.getParameters()[0];

            paramComponent.setLabel(paramReference.getDescription(), LabeledComponent.LABEL_ORIENTATION.WEST);
            return paramComponent;
        }

        paramReference = null;
        return null;
    }

    @Override
    public ActionParameterData[] extractData() {
        if (paramReference != null) {
            ActionParameterData[] data = new ActionParameterData[1];
            data[0] = new ActionParameterData(paramReference, field.getValue());

            return data;
        }

        return new ActionParameterData[0];
    }
}
