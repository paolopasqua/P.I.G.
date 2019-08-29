package it.unibs.dii.pajc.pig.client.view.renderer;

import it.unibs.dii.pajc.pig.client.bean.generic.Action;
import it.unibs.dii.pajc.pig.client.bean.generic.ActionParameter;
import it.unibs.dii.pajc.pig.client.bean.generic.ActionParameterData;
import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.LabeledComponent;
import it.unibs.dii.pajc.pig.client.view.component.generalpurpouse.RadioButtonGroupPanel;

import javax.swing.*;
import java.awt.*;

public class ThreeLevelParameterRenderer implements InputRenderer<Action, ActionParameterData> {

    public static final String LEVEL_1 = "1";
    public static final String LEVEL_2 = "2";
    public static final String LEVEL_3 = "3";

    private ActionParameter paramReference;
    private LabeledComponent paramComponent;
    private RadioButtonGroupPanel radioPanel;
    private float lev1Value, lev2Value, lev3Value;

    public ThreeLevelParameterRenderer(float lev1Value, float lev2Value, float lev3Value) {
        radioPanel = new RadioButtonGroupPanel();
        radioPanel.add(new JRadioButton(LEVEL_1, true));
        radioPanel.add(new JRadioButton(LEVEL_2, false));
        radioPanel.add(new JRadioButton(LEVEL_3, false));

        paramComponent = new LabeledComponent(radioPanel);

        this.lev1Value = lev1Value;
        this.lev2Value = lev2Value;
        this.lev3Value = lev3Value;
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
            float value = 0;

            if (radioPanel.getSelected().getText().equals(LEVEL_1))
                value = lev1Value;
            else if (radioPanel.getSelected().getText().equals(LEVEL_2))
                value = lev2Value;
            else if (radioPanel.getSelected().getText().equals(LEVEL_3))
                value = lev3Value;

            data[0] = new ActionParameterData(paramReference, value);
            return data;
        }

        return new ActionParameterData[0];
    }
}
