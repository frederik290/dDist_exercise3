package client;

import res.QA;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Jeppe Vinberg on 11-04-2016.
 */
public class ClientQASender implements Runnable {

    private Socket socket;
    private ArrayBlockingQueue<QA> queue;

    public ClientQASender(Socket socket, ArrayBlockingQueue<QA> queue){
        this.socket = socket;
        this.queue = queue;
    }


    @Override
    public void run() {
        QA current = null;
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            while (true) {
                current = queue.take();
                outputStream.writeObject(current);
                if (current == null) break;


            }



        } catch (Exception e){}


        }
}

