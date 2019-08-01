package it.unibs.dii.pajc.pig.client;

import it.unibs.dii.pajc.pig.client.controller.ChoiceManager;
import it.unibs.dii.pajc.pig.client.model.ChoiceDataSource;
import it.unibs.dii.pajc.pig.client.utility.ServerConnectionData;
import it.unibs.dii.pajc.pig.client.view.ConnectionForm;
import it.unibs.dii.pajc.pig.client.view.HelpForm;

import java.util.Date;

public class PIG {
    public static void main(String[] args) {
        HelpForm helpForm = new HelpForm(); //TODO: init help form
        ConnectionForm choiceView = new ConnectionForm();
        ChoiceDataSource choiceModel = new ChoiceDataSource();
        ChoiceManager choiceController = new ChoiceManager();

        choiceModel.load(); //TODO: manage exceptions

        //TODO: remove test
        ServerConnectionData data = null;

        data = new ServerConnectionData();
        data.setAddress("192.168.0.99");
        data.setDescription("Diskstation");
        data.setLastConnection(new Date(19, 1, 13));
        data.setFavorite(true);
        choiceModel.addElement(data);

        data = new ServerConnectionData();
        data.setAddress("192.168.0.43");
        data.setDescription(null);
        data.setLastConnection(new Date());
        data.setFavorite(false);
        choiceModel.addElement(data);

        data = new ServerConnectionData();
        data.setAddress("192.168.0.74");
        data.setDescription("Portatile");
        data.setLastConnection(new Date(19, 4, 22));
        data.setFavorite(false);
        choiceModel.addElement(data);

        data = new ServerConnectionData();
        data.setAddress("192.168.0.23");
        data.setDescription(null);
        data.setLastConnection(new Date(17, 12, 31));
        data.setFavorite(false);
        choiceModel.addElement(data);

        data = new ServerConnectionData();
        data.setAddress("192.168.0.1");
        data.setDescription("Gateway");
        data.setLastConnection(new Date());
        choiceModel.addElement(data);

        data = new ServerConnectionData();
        data.setAddress("0.0.0.0");
        data.setDescription("Localhost");
        data.setLastConnection(new Date(19, 8, 1));
        data.setFavorite(true);
        choiceModel.addElement(data);
        //end test

        //choiceController.attachHelpView(helpForm);
        choiceController.setView(choiceView);
        choiceController.setModel(choiceModel);
        choiceController.addClosingActionListener(actionEvent -> {
            choiceModel.store();
        });

        choiceController.start();
    }
}
