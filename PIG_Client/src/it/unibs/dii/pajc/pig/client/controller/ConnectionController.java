package it.unibs.dii.pajc.pig.client.controller;

import it.unibs.dii.pajc.pig.client.model.CommunicationProtocol;

public interface ConnectionController<T> extends PIGController {

    @Override
    void start();

    void attachObserver(ConnectionObserver<T> observer);
    void detachObserver(ConnectionObserver<T> observer);
    String send(T data);
    String getServerAddress();
    String getServerInfo();
    CommunicationProtocol getProtocol();
    void close();

}
