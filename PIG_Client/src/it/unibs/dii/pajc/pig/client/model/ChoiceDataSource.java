package it.unibs.dii.pajc.pig.client.model;

import it.unibs.dii.pajc.pig.client.utility.FileListModel;
import it.unibs.dii.pajc.pig.client.utility.ServerConnectionData;

public class ChoiceDataSource extends FileListModel<ServerConnectionData> implements ChoiceModel<ServerConnectionData> {

    public ChoiceDataSource() {
        super(""); //TODO: set model path
    }

    @Override
    public boolean validateData(ServerConnectionData elem) {
        //TODO: validate an element
        return false;
    }

}
