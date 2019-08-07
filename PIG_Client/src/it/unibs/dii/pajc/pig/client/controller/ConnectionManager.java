package it.unibs.dii.pajc.pig.client.controller;

import it.unibs.dii.pajc.pig.client.bean.CommunicationData;
import it.unibs.dii.pajc.pig.client.bean.ServerConnectionData;

public class ConnectionManager implements  ConnectionController<CommunicationData> {
    //TODO: all

    private ServerConnectionData connectionData;

    public ConnectionManager(ServerConnectionData connectionData) {
        this.connectionData = connectionData;
    }

    @Override
    public void start() {

    }

    @Override
    public void attachObserver(ConnectionObserver<CommunicationData> observer) {

    }

    @Override
    public void detachObserver(ConnectionObserver<CommunicationData> observer) {

    }

    @Override
    public void send(CommunicationData data) {

    }

    @Override
    public String getServerAddress() {
        return connectionData.getAddress();
    }

    @Override
    public String getServerInfo() {
        return connectionData.getDescription();
    }

    @Override
    public void close() {

    }

}
