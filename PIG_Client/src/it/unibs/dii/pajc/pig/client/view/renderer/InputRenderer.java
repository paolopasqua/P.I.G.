package it.unibs.dii.pajc.pig.client.view.renderer;

import java.awt.*;

public interface InputRenderer<T, D> {
    Component getInputsComponent(Component container, T source);
    D[] extractData();
}
