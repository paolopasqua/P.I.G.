package it.unibs.dii.pajc.pig.client.model;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;
import it.unibs.dii.pajc.pig.client.bean.generic.Activity;
import it.unibs.dii.pajc.pig.client.bean.generic.Rule;

public abstract class CommunicationProtocolAdapter implements CommunicationProtocol {

    @Override
    public String compilePackage(Package pack) {
        return null;
    }

    @Override
    public String compileParameter(String data) {
        return null;
    }

    @Override
    public Package decompilePackage(String pack) {
        return null;
    }

    @Override
    public String compileParameter(Activity data) {
        return null;
    }

    @Override
    public String compileParameter(Rule data) {
        return null;
    }

    @Override
    public String compileParameter(Sensor data) {
        return null;
    }

    @Override
    public String compileParameter(Device data) {
        return null;
    }

    @Override
    public Object decompileParameter(String parameter) {
        return null;
    }
}
