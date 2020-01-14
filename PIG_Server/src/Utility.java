import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Utility {

    public static String convertState (HashMap state) {

        String convertedMessage = "";

        for (Object obj : state.values()) {

            convertedMessage = convertedMessage + "{" + ((Hardware) obj).getType() + "," + ((Hardware) obj).getUUID() + "," + ((Hardware) obj).getValue() + "}" ;

        }

        return convertedMessage;
    }

    public static String convertSummary (HashMap<UUID, Activity> activity, HashMap<UUID, Rule> rules) {

        String convertedMessage = "";

        for (Activity obj : activity.values()) {
            convertedMessage += obj.toString();
        }

        for (Rule obj : rules.values()) {
            convertedMessage += obj.toString();
        }

        return convertedMessage;

    }

    //{identificativo}commandName:{parameter1}{parameter2}...
    public static String createMessage (String commandName, String parameters) {

        String message;
        message = "{" + UUID.randomUUID() + "}" + commandName;
        if (!parameters.equals("")) {
            message += ":" + parameters;
        }

        message += "\n";

        return message;

    }

    public static String createMessage (UUID uuid, String commandName) {

        String message;
        message = "{" + uuid + "}" + commandName + "\n";

        return message;

    }

    public static void sendMessage (String messageToBeSent, Writer writer) throws IOException {

        writer.write(messageToBeSent);
        writer.flush();

    }

    public static void sendACK (UUID uuid, Writer writer) throws IOException {

        sendMessage(createMessage(uuid, Command.ACK), writer);

    }

    public static void sendError (UUID uuid, Writer writer) throws IOException {

        sendMessage(createMessage(uuid, Command.ERROR), writer);

    }


}


