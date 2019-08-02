package it.unibs.dii.pajc.pig.client.controller;

public interface ConnectionObserver<T> {

    void elaborateData(T data);

}
