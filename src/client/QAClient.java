package client;

import res.QA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Jeppe Vinberg on 11-04-2016.
 */
public class QAClient {

    private int portNumber = 40103;

    public static void main(String[] args){
        String serverName = args[0];
    }

    /**
     *
     * Connects to the server on IP address serverName and port number portNumber.
     */
    protected Socket connectToServer(String serverName) {
        Socket res = null;
        try {
            res = new Socket(serverName,portNumber);
        } catch (IOException e) {
            // in case of IOException, we return null
        }
        return res;
    }

    public void run(String serverName){

        Socket socket = connectToServer(serverName);

        if(socket != null){
            ArrayBlockingQueue<QA> senderQueue = new ArrayBlockingQueue<QA>(100);
            ClientQASender sender = new ClientQASender(socket, senderQueue);
            ClientQAReceiver receiver = new ClientQAReceiver(socket);
            Thread t1 = new Thread(sender);
            Thread t2 = new Thread(receiver);
            t1.run();
            t2.run();;
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            String question;
            try{
                while((question = stdin.readLine()) != null){
                    QA qa = new QA();
                    qa.setQuestion(question);
                    senderQueue.put(qa);
                }
                senderQueue.put(null);
            }catch (Exception e){}


        }
    }
}
