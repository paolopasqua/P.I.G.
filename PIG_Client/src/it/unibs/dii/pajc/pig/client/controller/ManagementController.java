package it.unibs.dii.pajc.pig.client.controller;

public interface ManagementController extends PIGController {

    void attachObserver(ManagementObserver<ManagementController> observer);
    void detachObserver(ManagementObserver<ManagementController> observer);
    void close();

}
