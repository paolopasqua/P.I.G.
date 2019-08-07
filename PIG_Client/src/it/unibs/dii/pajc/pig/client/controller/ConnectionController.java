package it.unibs.dii.pajc.pig.client.controller;

public interface ConnectionController<T> extends PIGController {

    void attachObserver(ConnectionObserver<T> observer);
    void detachObserver(ConnectionObserver<T> observer);
    void send(T data);
    void close();

}
