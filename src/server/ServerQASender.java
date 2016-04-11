package server;

import res.QA;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Jeppe Vinberg on 11-04-2016.
 */
public class ServerQASender implements Runnable {

    private Socket clientSocket;
    private ArrayBlockingQueue<QA> queue;

    public ServerQASender(Socket clientSocket, ArrayBlockingQueue<QA> queue){
        this.clientSocket = clientSocket;
        this.queue = queue;
    }

    @Override
    public void run() {
        BufferedReader stdin;
        ObjectOutputStream outputStream;
        QA qa = null;
        String answer;
        try{
            stdin = new BufferedReader(new InputStreamReader(System.in));
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            while(true) {
                qa = queue.take();
                if(qa != null){
                    System.out.println("Answer this question: '" + qa.getQuestion() + "':");
                    answer = stdin.readLine();
                    qa.setAnswer(answer);
                    outputStream.writeObject(qa);
                }else{
                    outputStream.writeObject(null);
                    break;
                }

            }
        } catch (Exception e){}





    }
}

