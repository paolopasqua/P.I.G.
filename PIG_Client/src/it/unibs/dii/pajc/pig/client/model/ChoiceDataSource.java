package it.unibs.dii.pajc.pig.client.model;

import it.unibs.dii.pajc.pig.client.utility.FileListModel;
import it.unibs.dii.pajc.pig.client.utility.ServerConnectionData;
import it.unibs.dii.pajc.pig.client.utility.UtilityConstant;

import java.io.IOException;

public class ChoiceDataSource extends FileListModel<ServerConnectionData> implements ChoiceModel<ServerConnectionData> {

    public ChoiceDataSource() throws IOException {
        super(UtilityConstant.SERVER_SELECTION_DATA_PATH);
    }

    @Override
    public boolean validateData(ServerConnectionData elem) {
        //TODO: validate an element
        return false;
    }

}
