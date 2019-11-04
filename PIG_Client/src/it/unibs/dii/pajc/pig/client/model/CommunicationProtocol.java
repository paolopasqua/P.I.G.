package it.unibs.dii.pajc.pig.client.model;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;
import it.unibs.dii.pajc.pig.client.bean.generic.Activity;
import it.unibs.dii.pajc.pig.client.bean.generic.Rule;

public interface CommunicationProtocol {

    Command CMD_CONNECTION = () -> "CONNECT";
    Command CMD_DATA_REQUEST = () -> "SUMMARY";
    Command CMD_ADD_ENTITY = () -> "ADD";
    Command CMD_REMOVE_ENTITY = () -> "REMOVE";
    Command CMD_NEWS_DIFFUSION = () -> "NEWS";
    Command CMD_SYNCHRONIZATION = () -> "SYNC";
    Command CMD_STATE_UPDATE = () -> "STATE";
    Command CMD_DISCONNECTION = () -> "DISCONNECT";
    Command CMD_CONFIRM = () -> "ACK";
    Command CMD_ERROR = () -> "ERROR";

    interface Timing {
        long getTime();
    }

    interface Command {
        String getCommandString();
    }

    interface Package {
        String getId();
        String getCommand();
        String[] getParameters();
    }

    interface ParameterInstantiator {
        Object getInstance(String data);
    }

    String compilePackage(Package pack);
    Package decompilePackage(String pack);
    String compileParameter(String data);
    String compileParameter(Activity data);
    String compileParameter(Rule data);
    String compileParameter(Sensor data);
    String compileParameter(Device data);

    Class getParameterType(String parameter);
    Object decompileParameter(String parameter);

}
