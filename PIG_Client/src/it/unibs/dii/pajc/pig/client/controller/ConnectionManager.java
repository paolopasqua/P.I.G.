package it.unibs.dii.pajc.pig.client.controller;

import it.unibs.dii.pajc.pig.client.bean.CommunicationData;
import it.unibs.dii.pajc.pig.client.bean.ServerConnectionData;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;
import it.unibs.dii.pajc.pig.client.bean.generic.Activity;
import it.unibs.dii.pajc.pig.client.bean.generic.Rule;
import it.unibs.dii.pajc.pig.client.model.CommunicationProtocol;
import it.unibs.dii.pajc.pig.client.model.ProtocolV1;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Semaphore;

public class ConnectionManager implements  ConnectionController<CommunicationData> {

    private static final int SERVER_PORT = 2500;

    private ServerConnectionData connectionData;
    private ArrayList<ConnectionObserver<CommunicationData>> observers;
    Socket client;
    BufferedReader listenerStream;
    DataOutputStream writerStream;
    private Thread connectionListener, connectionWriter;
    private Semaphore sendSemaphore, replySemaphore, listenSemaphore;
    private String errorString, sendingData;
    private CommunicationData receivingData;

    public ConnectionManager(ServerConnectionData connectionData) throws IOException {
        this.connectionData = connectionData;
        this.observers = new ArrayList<>();

        this.client = new Socket(connectionData.getAddress(),SERVER_PORT);
        this.listenerStream = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.writerStream = new DataOutputStream(client.getOutputStream());

        this.sendSemaphore = new Semaphore(0);
        this.replySemaphore = new Semaphore(0);
        this.listenSemaphore = new Semaphore(1);

        this.connectionListener = new Thread(() -> {
            while(true) {
                try {
                    listenSemaphore.acquire(); //wait to abilitation

                    if (listenerStream.ready()) {
                        String data = listenerStream.readLine();
                        System.out.println("RECEIVED: " + data);
                        CommunicationProtocol.Package pack = getProtocol().decompilePackage(data);

                        if (pack != null) {
                            //decompile package
                            receivingData = new CommunicationData(pack.getCommand());
                            receivingData.setPackID(pack.getId());
                            for (String param : pack.getParameters()) {
                                Class paramType = getProtocol().getParameterType(param);
                                Object par = getProtocol().decompileParameter(param);

                                if (Activity.class.isAssignableFrom(paramType)) {
                                    receivingData.addActivity((Activity) par);
                                } else if (Rule.class.isAssignableFrom(paramType)) {
                                    receivingData.addRule((Rule) par);
                                } else if (Device.class.isAssignableFrom(paramType)) {
                                    receivingData.addDevice((Device) par);
                                } else if (Sensor.class.isAssignableFrom(paramType)) {
                                    receivingData.addSensor((Sensor) par);
                                } else if (String.class.isAssignableFrom(paramType)) {
                                    receivingData.addString((String) par);
                                }
                            }

                            /*
                            //elaborating the data received
                            if (receivingData.getCommand().equals(ProtocolV1.ProtocolCommands.CONFIRM) ||
                                    receivingData.getCommand().equals(ProtocolV1.ProtocolCommands.ERROR)) {
                                //Reply of a sended message
                                replySemaphore.release(); //release the reply waiting to elaborate data
                                //next data reading released in sending thread
                            } else {
                                informObserver(receivingData);
                                listenSemaphore.release(); //Ready to acquire next data
                            }
                             */


                            informObserver(receivingData);
                        }
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

                listenSemaphore.release(); //Ready to acquire next data
                //next reading now
            }
        });
        this.connectionWriter = new Thread(() -> {
            while(true) {
                try {
                    sendSemaphore.acquire(); //wait to abilitation

                    writerStream.writeBytes(sendingData);
                    System.out.println("SENDED: " + sendingData);

                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }

                //next sending now
            }
        });

        this.errorString = "";
    }

    @Override
    public void start() {
        connectionListener.start();
        connectionWriter.start();
    }

    @Override
    public void attachObserver(ConnectionObserver<CommunicationData> observer) {
        observers.add(observer);
    }

    @Override
    public void detachObserver(ConnectionObserver<CommunicationData> observer) {
        observers.remove(observer);
    }

    private void informObserver(CommunicationData data) {
        for (ConnectionObserver<CommunicationData> observer : observers) {
            observer.elaborateData(data);
        }
    }

    @Override
    public String send(CommunicationData data) {
        ProtocolV1.ProtocolCommands command = ProtocolV1.ProtocolCommands.getFromString(data.getCommand());
        ProtocolV1.ProtocolPackage pack = getProtocol().generateBasePackage(UUID.randomUUID().toString(), command);

        errorString = null; //reset error string

        //adding parameters
        if (data.getStrings() != null)
            data.getStrings().forEach(str -> pack.appendParameter(getProtocol().compileParameter(str)));
        if (data.getDevices() != null)
            data.getDevices().forEach(device -> pack.appendParameter(getProtocol().compileParameter(device)));
        if (data.getSensors() != null)
            data.getSensors().forEach(sensor -> pack.appendParameter(getProtocol().compileParameter(sensor)));
        if (data.getActivities() != null)
            data.getActivities().forEach(activity -> pack.appendParameter(getProtocol().compileParameter(activity)));
        if (data.getRules() != null)
            data.getRules().forEach(rule -> pack.appendParameter(getProtocol().compileParameter(rule)));

        sendingData = getProtocol().compilePackage(pack) + "\n"; //compiling package
        sendSemaphore.release(); //sending data

        /*
        //if command has to wait for a reply
        if (command.isWaitForReply()) {
            boolean replayOk = false;

            //continue until the reply is correct
            while (!replayOk) {
                try {
                    replySemaphore.acquire(); //waiting the reply

                    //check if it's the same package response
                    if (pack.getId().equals(receivingData.getPackID())) {
                        replayOk = true;
                        listenSemaphore.release(); //releasing listening thread

                        if (receivingData.getCommand().equals(ProtocolV1.ProtocolCommands.ERROR)) {
                            if (receivingData.getStrings() != null && !receivingData.getStrings().isEmpty())
                                errorString = receivingData.getStrings().get(0);
                            res = false;
                        } else if (receivingData.getCommand().equals(ProtocolV1.ProtocolCommands.CONFIRM)) {
                            res = true;
                        }
                    } else {
                        informObserver(receivingData); //inform observers (not the correct package)
                        listenSemaphore.release(); //wait for another package
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
         */

        return pack.getId();
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
    public ProtocolV1 getProtocol() {
        return ProtocolV1.getInstance();
    }

    @Override
    public void close() {
        connectionWriter.stop();
        connectionListener.stop();
        try {
            listenerStream.close();
            writerStream.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
