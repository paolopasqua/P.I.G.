package it.unibs.dii.pajc.pig.client.view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.List;

public interface ChoiceView<T> extends PIGView {

    void showAlert(String title, String message);
    boolean askUser(String title, String message);
    void setDatasource(ListModel<T> datasource);
    List<T> getSelection();
    void addHelpActionListener(ActionListener lst);
    void removeHelpActionListener(ActionListener lst);
    void addMarkFavoriteActionListener(ActionListener lst);
    void removeMarkFavoriteActionListener(ActionListener lst);
    void addDeleteActionListener(ActionListener lst);
    void removeDeleteActionListener(ActionListener lst);
    void addConnectActionListener(ActionListener lst);
    void removeConnectActionListener(ActionListener lst);

}
