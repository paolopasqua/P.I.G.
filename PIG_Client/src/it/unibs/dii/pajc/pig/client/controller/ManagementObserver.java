package it.unibs.dii.pajc.pig.client.controller;

public interface ManagementObserver<T> {

    void closingAlert(T Source);

}
