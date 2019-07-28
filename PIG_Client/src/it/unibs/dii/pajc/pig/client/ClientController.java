package it.unibs.dii.pajc.pig.client;

import it.unibs.dii.pajc.pig.client.utility.ServerConnectionData;
import it.unibs.dii.pajc.pig.client.view.ConnectionForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Date;

public class ClientController {
    ConnectionForm connectionForm;
    DefaultListModel<ServerConnectionData> serverConnectionsModel;

    public static void main(String[] args) {
        ClientController clientController = new ClientController();
        clientController.openConnectionForm();
    }

    public ClientController() {

        //TODO: remove test
        serverConnectionsModel = new DefaultListModel<>();
        serverConnectionsModel.addElement(new ServerConnectionData("192.168.0.0", "Gateway", new Date(2019, 4, 28)));
        serverConnectionsModel.addElement(new ServerConnectionData("0.0.0.0", "Localhost", new Date(2018, 6, 18)));
        serverConnectionsModel.addElement(new ServerConnectionData("143.92.43.18", "Google", new Date(2019, 7, 20)));
        serverConnectionsModel.addElement(new ServerConnectionData("192.168.0.142", "Test", new Date()));
        ServerConnectionData d = new ServerConnectionData("192.168.0.253", "Printer", new Date());
        d.setFavorite(true);
        serverConnectionsModel.addElement(d);


    }

    public void openConnectionForm() {
        connectionForm = new ConnectionForm();
        connectionForm.setServerModel(serverConnectionsModel);

        connectionForm.addConnectActionListener(actionEvent -> {
            //TODO: connect action
        });
        connectionForm.addHelpActionListener(this::openHelpFormListener);
        connectionForm.addMarkFavoriteActionListener(actionEvent -> {
            //TODO: mark favorite action
        });
        connectionForm.addDeleteActionListener(actionEvent -> {
            //TODO: delete action
        });

        connectionForm.getFrame().setVisible(true);
    }

    public void openHelpFormListener(ActionEvent evt) {
        openHelpForm(evt.getSource());
    }

    public void openHelpForm(Object focus) {
        //TODO
    }
}
