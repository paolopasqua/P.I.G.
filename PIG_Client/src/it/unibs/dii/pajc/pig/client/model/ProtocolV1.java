package it.unibs.dii.pajc.pig.client.model;

import it.unibs.dii.pajc.pig.client.bean.abstraction.Device;
import it.unibs.dii.pajc.pig.client.bean.abstraction.Sensor;
import it.unibs.dii.pajc.pig.client.bean.device.emulated.EmulatedFan;
import it.unibs.dii.pajc.pig.client.bean.device.emulated.EmulatedLamp;
import it.unibs.dii.pajc.pig.client.bean.device.emulated.EmulatedPump;
import it.unibs.dii.pajc.pig.client.bean.device.emulated.EmulatedTempResistor;
import it.unibs.dii.pajc.pig.client.bean.generic.Activity;
import it.unibs.dii.pajc.pig.client.bean.generic.Rule;
import it.unibs.dii.pajc.pig.client.bean.sensor.EmulatedTempSensor;
import it.unibs.dii.pajc.pig.client.bean.sensor.EmulatedWaterSensor;

import java.util.ArrayList;
import java.util.Date;

public class ProtocolV1 extends CommunicationProtocolAdapter {

    private static final char PARAMETER_BEGIN = '{';
    private static final char PARAMETER_END = '}';
    private static final char DATA_SEPARATOR = ',';
    private static final char PACKAGE_ID_BEGIN = '{';
    private static final char PACKAGE_ID_END = '}';
    private static final char PACKAGE_HEAD_SEPARATOR = ':';

    private static final String EMULATED_TEMP_SENSOR_REGEX = "(a-zA-Z0-9)+" + DATA_SEPARATOR + "(0-9)+";
    private static final String EMULATED_WATER_SENSOR_REGEX = EMULATED_TEMP_SENSOR_REGEX;

    private static final String EMULATED_LAMP_REGEX = EMULATED_TEMP_SENSOR_REGEX;
    private static final String EMULATED_FAN_REGEX = "(a-zA-Z0-9)+" + DATA_SEPARATOR + "(0-9\\.)+";;
    private static final String EMULATED_TEMP_RESISTOR_REGEX = EMULATED_TEMP_SENSOR_REGEX;
    private static final String EMULATED_PUMP_REGEX = EMULATED_FAN_REGEX;

    private static final String PARAMETER_REGEX = "(" + PARAMETER_BEGIN + ")(a-zA-Z0-9)+(" + DATA_SEPARATOR + "(.)+)+(" + PARAMETER_END + ")"; //{id,data,...}
    private static final String PACKAGE_REGEX = "(" + PACKAGE_ID_BEGIN + ")(a-zA-Z0-9)+(" + PACKAGE_ID_BEGIN + ")(A-Z)+(" + PACKAGE_HEAD_SEPARATOR + PARAMETER_REGEX + "+)?"; //{id}command:parameters

    public enum ProtocoloTiming implements Timing {
        TIMEOUT(600000); //10 minutes

        private long timing;
        private ProtocoloTiming(long timing) { this.timing = timing; }
        @Override
        public long getTime() { return timing; }
    }

    public enum ProtocolCommands implements Command {
        CONNECTION(CMD_CONNECTION),
        DATA_REQUEST(CMD_DATA_REQUEST),
        ADD_ENTITY(CMD_ADD_ENTITY),
        REMOVE_ENTITY(CMD_REMOVE_ENTITY),
        NEWS_DIFFUSION(CMD_NEWS_DIFFUSION),
        SYNCHRONIZATION(CMD_SYNCHRONIZATION),
        STATE_UPDATE(CMD_STATE_UPDATE),
        DISCONNECTION(CMD_DISCONNECTION),
        CONFIRM(CMD_CONFIRM),
        ERROR(CMD_ERROR);

        private Command stdCommand;
        private String cmd;

        private ProtocolCommands(Command stdCommand) { this(stdCommand, null); }
        private ProtocolCommands(Command stdCommand, String overwriteCmd) {
            this.stdCommand = stdCommand;
            this.cmd = overwriteCmd == null ? stdCommand.getCommandString() : overwriteCmd;
        }
        @Override
        public String getCommandString() { return cmd; }
        public Command getStandardCommand() {return stdCommand; }

        public static ProtocolCommands getFromString(String command) {
            for (ProtocolCommands c : ProtocolCommands.values()) {
                if (c.getCommandString().equals(command))
                    return c;
            }
            return null;
        }
    }

    public enum ProtocolParameterTypes {
        STRING("000", data -> data), //{000,value}
        ACTIVITY("010", data -> { //{010,id-attività,id-dispositivo,data-esecuzione,durata,ripetizione,unità-ripetizione,azione}
            String[] dataSplit = ProtocolV1.getInstance().splitParameterData(data);
            String activityID = dataSplit[0];
            String deviceID = dataSplit[1];
            String actionID = dataSplit[6];
            Date execution = new Date(dataSplit[2]);
            int duration = Integer.parseInt(dataSplit[3]);
            int repetition = Integer.parseInt(dataSplit[4]);
            Activity.REPETITION unit = Activity.REPETITION.getByCode(dataSplit[5]);

            Activity act = new Activity(activityID, deviceID, actionID, execution);
            act.setDuration(duration);
            act.setRepetitionValue(repetition);
            act.setRepetitionUnits(unit);

            return act;
        }),
        RULE("011", data -> { //{011,id-regola,id-sensore,comparatore,dato-comparato,attività}
            String[] dataSplit = ProtocolV1.getInstance().splitParameterData(data);
            String ruleID = dataSplit[0];
            String sensorID = dataSplit[1];
            Rule.COMPARATOR c = Rule.COMPARATOR.getBySymbol(dataSplit[2]);
            String value = dataSplit[3];
            Activity act = (Activity)ProtocolV1.getInstance().decompileParameter(dataSplit[4]);

            Rule rule = new Rule(ruleID, sensorID, c, value, act);
            return rule;
        }),
        EMULATED_TEMP_SENSOR("100", data -> { //{100,id-sensore,valore}
            if (data.matches(EMULATED_TEMP_SENSOR_REGEX)) {
                String[] dataSplit = ProtocolV1.getInstance().splitParameterData(data);
                EmulatedTempSensor s = new EmulatedTempSensor(dataSplit[0]);
                s.setData(Integer.parseInt(dataSplit[1]));
                return s;
            }
            else
                return null;
        }),
        EMULATED_WATER_SENSOR("200", data -> { //{200,id-sensore,valore}
            if (data.matches(EMULATED_WATER_SENSOR_REGEX)) {
                String[] dataSplit = ProtocolV1.getInstance().splitParameterData(data);
                EmulatedWaterSensor s = new EmulatedWaterSensor(dataSplit[0]);
                s.setData(Integer.parseInt(dataSplit[1]));
                return s;
            }
            else
                return null;
        }),
        EMULATED_LAMP("300", data -> { //{300,id-dispositivo,stato}
            if (data.matches(EMULATED_LAMP_REGEX)) {
                String[] dataSplit = ProtocolV1.getInstance().splitParameterData(data);
                EmulatedLamp d = new EmulatedLamp(dataSplit[0]);
                d.setStatus(EmulatedLamp.LAMP_STATUS.fromValue(Integer.parseInt(dataSplit[1])));
                return d;
            }
            else
                return null;
        }),
        EMULATED_FAN("350", data -> { //{350,id-dispositivo,stato}
            if (data.matches(EMULATED_FAN_REGEX)) {
                String[] dataSplit = ProtocolV1.getInstance().splitParameterData(data);
                EmulatedFan d = new EmulatedFan(dataSplit[0]);
                d.setStatus(EmulatedFan.FAN_STATUS.fromValue(Float.parseFloat(dataSplit[1])));
                return d;
            }
            else
                return null;
        }),
        EMULATED_TEMP_RESISTOR("400", data -> { //{400,id-dispositivo,stato}
            if (data.matches(EMULATED_TEMP_RESISTOR_REGEX)) {
                String[] dataSplit = ProtocolV1.getInstance().splitParameterData(data);
                EmulatedTempResistor d = new EmulatedTempResistor(dataSplit[0]);
                d.setStatus(EmulatedTempResistor.TEMP_RESISTOR_STATUS.fromValue(Integer.parseInt(dataSplit[1])));
                return d;
            }
            else
                return null;
        }),
        EMULATED_PUMP("500", data -> { //{500,id-dispositivo,stato}
            if (data.matches(EMULATED_PUMP_REGEX)) {
                String[] dataSplit = ProtocolV1.getInstance().splitParameterData(data);
                EmulatedPump d = new EmulatedPump(dataSplit[0]);
                d.setStatus(EmulatedPump.PUMP_STATUS.fromValue(Float.parseFloat(dataSplit[1])));
                return d;
            }
            else
                return null;
        });

        private String typeId;
        private ParameterInstantiator instantiator;
        private ProtocolParameterTypes(String typeId, ParameterInstantiator instantiator) {
            this.typeId = typeId;
            this.instantiator = instantiator;
        }
        public String getTypeId() {
            return typeId;
        }
        public Object getInstance(String data) {
            return instantiator.getInstance(data);
        }
    }

    public class ProtocolPackage implements Package {
        private String id;
        private ProtocolCommands command;
        private ArrayList<String> parameters;

        public ProtocolPackage(String id, ProtocolCommands command) {
            this.id = id;
            this.command = command;
            this.parameters = new ArrayList<>();
        }
        public void appendParameter(String param) {
            this.parameters.add(param);
        }
        public String removeParameter(int index) {
            return this.parameters.remove(index);
        }
        @Override
        public String getId() {
            return id;
        }
        @Override
        public String getCommand() {
            return command.getCommandString();
        }
        @Override
        public String[] getParameters() {
            return (String[])parameters.toArray();
        }
    }

    private static ProtocolV1 instance = null;

    private ProtocolV1() {
    }

    public static ProtocolV1 getInstance() {
        if (instance == null)
            instance = new ProtocolV1();
        return instance;
    }

    public String[] splitParameterData(String data) {
        ArrayList<String> splitted = new ArrayList<>();
        int currPos = 0;

        while (currPos < data.length()) {
            int nextPos = currPos;

            if (data.charAt(currPos) == PARAMETER_BEGIN) { //data is another package
                //if there's a PARAMETER_BEGIN on data, gets the index and set counter to 1. Otherwise nextPos is already ok
                int nextParamBegin = data.indexOf(PARAMETER_BEGIN,currPos);
                nextPos = data.indexOf(PARAMETER_END, currPos);

                //if there's a parameter inside, must be included in the string
                while(nextParamBegin < nextPos && nextParamBegin != -1) {
                    nextParamBegin = data.indexOf(PARAMETER_BEGIN, nextPos);
                    nextPos = data.indexOf(PARAMETER_END, nextPos);
                }

                splitted.add(data.substring(currPos, nextPos));
            }
            else { //data is a value
                nextPos = data.indexOf(DATA_SEPARATOR, currPos);
            }
            currPos = nextPos+1;
        }

        return (String[])splitted.toArray();
    }

    @Override
    public String compilePackage(Package pack) {
        //{id}command:parameters
        String ret = PACKAGE_ID_BEGIN+"";

        ret += pack.getId() + PACKAGE_ID_END;
        ret += pack.getCommand();

        if (pack.getParameters().length > 0) {
            ret += PACKAGE_HEAD_SEPARATOR;
            for (String p : pack.getParameters()) {
                ret += compileParameter(p);
            }
        }

        return ret;
    }

    @Override
    public String compileParameter(String data) {
        //{id,data}
        String ret = PARAMETER_BEGIN + ProtocolParameterTypes.STRING.getTypeId() + DATA_SEPARATOR + data + PARAMETER_END;
        return ret;
    }

    @Override
    public String compileParameter(Activity data) {
        //{010,id-attività,id-dispositivo,data-esecuzione,durata,ripetizione,unità-ripetizione,azione}
        String ret = PARAMETER_BEGIN + ProtocolParameterTypes.ACTIVITY.getTypeId() + DATA_SEPARATOR;
        ret += data.getID() + DATA_SEPARATOR;
        ret += data.getDeviceId() + DATA_SEPARATOR;
        ret += data.getExecution().toString() + DATA_SEPARATOR;
        ret += data.getDuration() + DATA_SEPARATOR;
        ret += data.getRepetitionValue() + DATA_SEPARATOR;
        ret += data.getRepetitionUnits().getCode() + DATA_SEPARATOR;
        ret += data.getActionId() + PARAMETER_END;
        return ret;
    }

    @Override
    public String compileParameter(Rule data) {
        //{011,id-regola,id-sensore,comparatore,dato-comparato,attività}
        String ret = PARAMETER_BEGIN + ProtocolParameterTypes.RULE.getTypeId() + DATA_SEPARATOR;
        ret += data.getID() + DATA_SEPARATOR;
        ret += data.getSensorId() + DATA_SEPARATOR;
        ret += data.getComparator().getSymbol() + DATA_SEPARATOR;
        ret += compileParameter(data.getActivity()) + PARAMETER_END;
        return ret;
    }

    @Override
    public String compileParameter(Sensor data) {
        //{100,id-sensore,valore}
        String ret = PARAMETER_BEGIN + ProtocolParameterTypes.EMULATED_TEMP_SENSOR.getTypeId() + DATA_SEPARATOR;
        ret += data.getID() + DATA_SEPARATOR;
        ret += data.getData() + PARAMETER_END;
        return ret;
    }

    @Override
    public String compileParameter(Device data) {
        //{300,id-sensore,valore}
        String ret = PARAMETER_BEGIN + ProtocolParameterTypes.EMULATED_LAMP.getTypeId() + DATA_SEPARATOR;
        ret += data.getID() + DATA_SEPARATOR;
        ret += data.getStatus().getValue() + "" + PARAMETER_END;
        return ret;
    }

    @Override
    public Package decompilePackage(String pack) {
        //{id}command:parameters

        if (pack.matches(PACKAGE_REGEX)) {
            int idEnd = pack.indexOf(PACKAGE_ID_END);
            int sep = pack.indexOf(PACKAGE_HEAD_SEPARATOR, idEnd);
            String packID = pack.substring(1, idEnd); //gets content between PACKAGE_ID_BEGIN and PACKAGE_ID_END
            ProtocolCommands command = ProtocolCommands.getFromString(pack.substring(idEnd+1, sep)); //gets content between PACKAGE_ID_END and PACKAGE_HEAD_SEPARATOR
            if (command == null)
                throw new IllegalArgumentException("ProtocolV1.decompilePackage: unrecognized command string");

            String parameters = pack.substring(sep+1); //gets parameters after PACKAGE_HEAD_SEPARATOR

            ProtocolPackage p =  new ProtocolPackage(packID, command);
            //TODO append parameters

            return p;
        }

        return null;
    }

    @Override
    public Class getParameterType(String parameter) {
        if (parameter.matches(PARAMETER_REGEX)) {
            String type = extractParameterType(parameter);

            if (type.equals(ProtocolParameterTypes.STRING.getTypeId())) {
                return String.class;
            }
            else if (type.equals(ProtocolParameterTypes.ACTIVITY.getTypeId())) {
                return Activity.class;
            }
            else if (type.equals(ProtocolParameterTypes.RULE.getTypeId())) {
                return Rule.class;
            }
            else if (type.equals(ProtocolParameterTypes.EMULATED_TEMP_SENSOR.getTypeId())) {
                return EmulatedTempSensor.class;
            }
            else if (type.equals(ProtocolParameterTypes.EMULATED_WATER_SENSOR.getTypeId())) {
                return EmulatedWaterSensor.class;
            }
            else if (type.equals(ProtocolParameterTypes.EMULATED_LAMP.getTypeId())) {
                return EmulatedLamp.class;
            }
            else if (type.equals(ProtocolParameterTypes.EMULATED_FAN.getTypeId())) {
                return EmulatedFan.class;
            }
            else if (type.equals(ProtocolParameterTypes.EMULATED_TEMP_RESISTOR.getTypeId())) {
                return EmulatedTempResistor.class;
            }
            else if (type.equals(ProtocolParameterTypes.EMULATED_PUMP.getTypeId())) {
                return EmulatedPump.class;
            }
        }

        return null;
    }

    @Override
    public Object decompileParameter(String parameter) {
        //{id,data,...}

        if (parameter.matches(PARAMETER_REGEX)) {
            String type = extractParameterType(parameter);
            String data = extractParameterData(parameter);

            if (type.equals(ProtocolParameterTypes.STRING.getTypeId())) {
                return ProtocolParameterTypes.STRING.getInstance(data);
            }
            else if (type.equals(ProtocolParameterTypes.ACTIVITY.getTypeId())) {
                return ProtocolParameterTypes.ACTIVITY.getInstance(data);
            }
            else if (type.equals(ProtocolParameterTypes.RULE.getTypeId())) {
                return ProtocolParameterTypes.RULE.getInstance(data);
            }
            else if (type.equals(ProtocolParameterTypes.EMULATED_TEMP_SENSOR.getTypeId())) {
                return ProtocolParameterTypes.EMULATED_TEMP_SENSOR.getInstance(data);
            }
            else if (type.equals(ProtocolParameterTypes.EMULATED_WATER_SENSOR.getTypeId())) {
                return ProtocolParameterTypes.EMULATED_WATER_SENSOR.getInstance(data);
            }
            else if (type.equals(ProtocolParameterTypes.EMULATED_LAMP.getTypeId())) {
                return ProtocolParameterTypes.EMULATED_LAMP.getInstance(data);
            }
            else if (type.equals(ProtocolParameterTypes.EMULATED_FAN.getTypeId())) {
                return ProtocolParameterTypes.EMULATED_FAN.getInstance(data);
            }
            else if (type.equals(ProtocolParameterTypes.EMULATED_TEMP_RESISTOR.getTypeId())) {
                return ProtocolParameterTypes.EMULATED_TEMP_RESISTOR.getInstance(data);
            }
            else if (type.equals(ProtocolParameterTypes.EMULATED_PUMP.getTypeId())) {
                return ProtocolParameterTypes.EMULATED_PUMP.getInstance(data);
            }
        }

        return null;
    }

    private String extractParameterType(String parameter) {
        int dataSep = parameter.indexOf(DATA_SEPARATOR);
        return parameter.substring(1, dataSep); //remove { and after DATA_SEPARATOR
    }

    private String extractParameterData(String parameter) {
        int dataSep = parameter.indexOf(DATA_SEPARATOR);
        return parameter.substring(dataSep+1, parameter.length()-1); //get data and remove }
    }
}
