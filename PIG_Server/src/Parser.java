import java.util.ArrayList;
import java.util.Scanner;

public class Parser {

    public static final char SEPARATOR = ',';
    public static final char OPEN_CURLY_BRACKET = '{';
    public static final char CLOSE_CURLY_BRACKET = '}';
    public static final char COLON = ':';

    public static String message;
    public static ArrayList<String> parsedMessage;

    public Parser () {

    }

    public static void parse (ArrayList<String> parsedMessage, String message) {

        String temporaryWord = "";

        int i = 0;

        for (int cont=0; cont<message.length(); cont++){

            switch (message.charAt(cont)) {

                case OPEN_CURLY_BRACKET:
                    if (!temporaryWord.equals("")){
                        parsedMessage.add(temporaryWord);
                    }
                    //parsedMessage.add(Character.toString(OPEN_CURLY_BRACKET));
                    temporaryWord = "";
                    break;

                case CLOSE_CURLY_BRACKET:
                    if (!temporaryWord.equals("")){
                        parsedMessage.add(temporaryWord);
                    }
                    //parsedMessage.add(Character.toString(CLOSE_CURLY_BRACKET));
                    temporaryWord = "";
                    break;

                case COLON:
                    if (!temporaryWord.equals("")){
                        if (i == 0) {
                            parsedMessage.add(temporaryWord);
                            temporaryWord = "";
                            i++;
                        } else {
                            temporaryWord += message.charAt(cont);
                        }
                    }
                    break;

                case SEPARATOR:
                    if (!temporaryWord.equals("")){
                        parsedMessage.add(temporaryWord);
                    }
                    temporaryWord = "";
                    break;

                default:
                    temporaryWord += message.charAt(cont);
                    break;

            }

        }

    }

    public static String getAction (ArrayList<String> parsedMessage) {

        return parsedMessage.get(1);

    }

}
