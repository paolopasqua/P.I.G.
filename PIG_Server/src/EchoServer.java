
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class EchoServer {

    public static GreenHouse greenHouse;
    public static MyFrame myFrame;

    public static void main(String[] argv) {
        ServerSocket server = null;

        try {

            server = new ServerSocket(2500);

        } catch(IOException ex) {

            ex.printStackTrace();
            System.exit(-1);

        }

        myFrame = new MyFrame();
        greenHouse = new GreenHouse();
        System.out.println("Server avviato...");

        while(true) {

            try {
                Socket client = server.accept();
                EchoThread echoThread = new EchoThread(client);
                Thread worker = new Thread(echoThread);
                worker.start();

            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}