package it.unibs.dii.pajc.pig.client.model;

import javax.swing.event.ListDataListener;
import java.util.List;

public interface ChoiceModel<T> {

    int getIndex(T elem);
    int getSize();
    T getElementAt(int index);
    List<T> getElements();
    void addElement(T elem);
    void removeElementAt(int index);
    void updateElementAt(T elem, int index);

    void addListDataListener(ListDataListener lst);
    void removeListDataListener(ListDataListener lst);

    boolean validateData(T elem);

}
