package it.unibs.dii.pajc.pig.client;

import it.unibs.dii.pajc.pig.client.controller.ChoiceManager;
import it.unibs.dii.pajc.pig.client.model.ChoiceDataSource;
import it.unibs.dii.pajc.pig.client.view.ConnectionForm;
import it.unibs.dii.pajc.pig.client.view.HelpForm;

import javax.swing.*;
import java.io.IOException;
import java.util.ResourceBundle;

public class PIG {

    private static ResourceBundle localizationBundle = ResourceBundle.getBundle("localization/PIG");

    public static void main(String[] args) {
        HelpForm helpForm = new HelpForm(); //TODO: init help form
        ConnectionForm choiceView = new ConnectionForm();
        ChoiceManager choiceController = new ChoiceManager();
        ChoiceDataSource choiceModel = null;
        try {
            choiceModel = new ChoiceDataSource();
            choiceModel.load(); //TODO: manage exceptions
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            //TODO: localize
            JOptionPane.showMessageDialog(null, localizationBundle.getString("advice.error.loading.message"), localizationBundle.getString("advice.error.loading.title"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        ChoiceDataSource modelFinalRef = choiceModel;

        //choiceController.attachHelpView(helpForm);
        choiceController.setView(choiceView);
        choiceController.setModel(choiceModel);
        choiceController.addClosingActionListener(actionEvent -> {
            try {
                modelFinalRef.store();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        choiceController.start();
    }
}
