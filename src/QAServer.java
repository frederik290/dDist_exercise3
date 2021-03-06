import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Jeppe Vinberg on 11-04-2016.
 */
public class QAServer {

    private int portNumber = 40103;
    private ServerSocket serverSocket;

    protected void printLocalHostAddress() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            String localhostAddress = localhost.getHostAddress();
            System.out.println("Contact this server on the IP address " + localhostAddress);
        } catch (UnknownHostException e) {
            System.err.println("Cannot resolve the Internet address of the local host.");
            System.err.println(e);
            System.exit(-1);
        }
    }

    protected void registerOnPort() {
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            serverSocket = null;
            System.err.println("Cannot open server socket on port number" + portNumber);
            System.err.println(e);
            System.exit(-1);
        }
    }

    public void deregisterOnPort() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    protected Socket waitForConnectionFromClient() {
        Socket res = null;
        try {
            res = serverSocket.accept();
        } catch (IOException e) {
            // We return null on IOExceptions
        }
        return res;
    }

    public void run(){
        printLocalHostAddress();
        registerOnPort();

        while(true) {
            Socket clientSocket = waitForConnectionFromClient();
            if(clientSocket != null) {
                System.out.println("New client connected: " + clientSocket);
                ArrayBlockingQueue<QA> queue = new ArrayBlockingQueue<>(100);
                ServerQAReceiver receiver = new ServerQAReceiver(clientSocket, queue);
                ServerQASender sender = new ServerQASender(clientSocket, queue);
                Thread t1 = new Thread(receiver);
                Thread t2 = new Thread(sender);
                t1.start();
                t2.start();

            }

        }
    }

    public static void main(String[] args){
        QAServer server = new QAServer();
        server.run();
    }

}
