
import java.net.Socket;
import java.io.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EchoThread implements Runnable {
    private Socket client = null;
    private String clientIp = null;
    private Writer writer;
    private BufferedReader reader;
    private ScheduledExecutorService exec1;
    private ScheduledExecutorService exec2;

    public EchoThread(Socket client) {

        this.client = client;
        Writer writer = null;
        clientIp = this.client.getInetAddress().getHostAddress();
        System.out.println("[" + clientIp + "] " + ">> Connessione in ingresso <<");


    }

    public void run() {
        reader = null;
        writer = null;

        try {

            reader = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            writer = new OutputStreamWriter(this.client.getOutputStream());
            exec1 = Executors.newSingleThreadScheduledExecutor();
            exec1.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {

                    try {

                        Utility.sendMessage(Utility.createMessage(Command.STATE, Utility.convertState(GreenHouse.getGreenHmap())), writer);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }, 0, 10, TimeUnit.SECONDS);

            exec2 = Executors.newSingleThreadScheduledExecutor();
            exec2.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {

                    try {
                        Utility.sendMessage(Utility.createMessage(Command.SUMMARY, Utility.convertSummary(GreenHouse.sched.activityList, GreenHouse.rulSched.rulesList)), writer);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }, 0, 1, TimeUnit.MINUTES);

            while(true) {

                ArrayList<String> parsedMessage = new ArrayList<>();
                String prova = reader.readLine();

                if (prova == null){
                    break;
                }

                Parser.parse(parsedMessage, prova);

                String comando = Parser.getAction(parsedMessage);

                if (comando.equals("DISCONNECT")){
                    exec1.shutdownNow();
                    exec2.shutdownNow();
                    break;
                }

                switch (comando) {

                    case "ADD":
                        if (parsedMessage.get(2).equals("010")) {
                            GreenHouse.sched.addActivity(new Activity(parsedMessage), UUID.fromString(parsedMessage.get(3)));
                            Utility.sendACK(UUID.fromString(parsedMessage.get(0)), writer);
                        } else if (parsedMessage.get(2).equals("011")) {
                            GreenHouse.rulSched.addRule(parsedMessage);
                            Utility.sendACK(UUID.fromString(parsedMessage.get(0)), writer);
                        }
                        break;

                    case "REMOVE":
                        if (parsedMessage.get(2).equals("010")) {
                            GreenHouse.sched.removeActivity(UUID.fromString(parsedMessage.get(3)));
                            Utility.sendACK(UUID.fromString(parsedMessage.get(0)), writer);
                        } else if (parsedMessage.get(2).equals(("011"))) {
                            GreenHouse.rulSched.removeRule(UUID.fromString(parsedMessage.get(3)));
                            Utility.sendACK(UUID.fromString(parsedMessage.get(0)), writer);
                        }
                        break;

                    default:
                        break;

                }

            }

        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            // Clean
            try {

                writer.close();
                reader.close();
                exec1.shutdownNow();
                exec2.shutdownNow();
                //this.client.close();
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }

            System.out.println("[" + clientIp + "] " + ">> Connessione terminata <<");
        }
    }

    public Socket getClient() {
        return client;
    }

    public Writer getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }



}