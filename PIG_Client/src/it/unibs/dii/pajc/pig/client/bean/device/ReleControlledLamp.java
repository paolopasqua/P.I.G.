package it.unibs.dii.pajc.pig.client.bean.device;

import it.unibs.dii.pajc.pig.client.bean.device.emulated.EmulatedLamp;

public class ReleControlledLamp extends EmulatedLamp {

    private int controlGPIO;
    private boolean NC_NO; //Normally closed (false) or normally opened (true)

    public ReleControlledLamp(String id) throws IllegalArgumentException {
        super(id);
    }

    public ReleControlledLamp(String id, String description) throws IllegalArgumentException {
        super(id, description);
    }

    public int getControlGPIO() {
        return controlGPIO;
    }

    public void setControlGPIO(int controlGPIO) {
        this.controlGPIO = controlGPIO;
    }

    public boolean isNC_NO() {
        return NC_NO;
    }

    public void setNC_NO(boolean NC_NO) {
        this.NC_NO = NC_NO;
    }
}
