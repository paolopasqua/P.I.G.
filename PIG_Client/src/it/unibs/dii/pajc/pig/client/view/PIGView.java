package it.unibs.dii.pajc.pig.client.view;

import java.awt.event.WindowListener;

public interface PIGView {

    void show();
    void addWindowListener(WindowListener lst);
    void removeWindowListener(WindowListener lst);

}
