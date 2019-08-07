package it.unibs.dii.pajc.pig.client.controller;

import it.unibs.dii.pajc.pig.client.bean.CommunicationData;
import it.unibs.dii.pajc.pig.client.view.HelpView;
import it.unibs.dii.pajc.pig.client.view.ManagementView;

public class ManagementManager implements ManagementController, ConnectionObserver<CommunicationData> {
    //TODO: all

    private HelpView helpView;
    private ManagementView view;
    private ConnectionController<CommunicationData> connectionController;

    @Override
    public void close() {

    }

    @Override
    public void start() {

    }

    @Override
    public void attachObserver(ManagementObserver<ManagementController> observer) {

    }

    @Override
    public void detachObserver(ManagementObserver<ManagementController> observer) {

    }

    @Override
    public void elaborateData(CommunicationData data) {

    }

    public void setView(ManagementView view) {
        this.view = view;
    }

    public void removeView() {
        this.view = null;
    }

    public void setHelpView(HelpView helpView) {
        this.helpView = helpView;
    }

    public void removeHelpView() {
        this.helpView = null;
    }

    public void setConnectionController(ConnectionController<CommunicationData> connectionController) {
        connectionController.attachObserver(this);
        this.connectionController = connectionController;
    }

    public void removeConnectionController() {
        this.connectionController.detachObserver(this);
        this.connectionController = null;
    }
}
