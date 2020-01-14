import java.util.ArrayList;
import java.util.UUID;

public class VirtualThing {

    //Classe fittizia che gestisce e recupera il valore dei vari sensori
    private static int temp;
    private static int watLev;

    //Per i devices si hanno 4 tipi di device = {0:fan; 1:water pump; 2:lamp; 3:heat resistor};

    public static int getTemperature() {

        return EchoServer.myFrame.sensorsArrayList.get(1).getSlider().getValue();

    }

    public static int getWaterLevel() {

        return EchoServer.myFrame.sensorsArrayList.get(0).getSlider().getValue();

    }

    public static void setDeviceValue(int typeOfDevice, int value, UUID idDev) {

        typeOfDevice = ((Device) GreenHouse.getGreenHmap().get(idDev)).getType();

        switch (typeOfDevice) {

            case 300:
                EchoServer.myFrame.devicesArrayList.get(0).setValue(value);
                break;

            case 350:
                EchoServer.myFrame.devicesArrayList.get(1).setValue(value);
                break;

            case 400:
                EchoServer.myFrame.devicesArrayList.get(2).setValue(value);
                break;

            case 500:
                EchoServer.myFrame.devicesArrayList.get(3).setValue(value);
                break;

            default:
                break;

        }

    }

}
