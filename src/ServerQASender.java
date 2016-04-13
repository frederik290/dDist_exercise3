import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
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
        Scanner scanner;
        ObjectOutputStream outputStream;
        QA qa = null;
        String answer;
        try{
            //stdin = new BufferedReader(new InputStreamReader(System.in));
            scanner = new Scanner(System.in);
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            while(true) {
                qa = queue.take();
                if(qa.getQuestion() != null){
                    System.out.println("\nAnswer this question: '" + qa.getQuestion() + "':");
                    answer = scanner.nextLine();
                    qa.setAnswer(answer);
                    outputStream.writeObject(qa);
                }else{
                    qa.setAnswer(null);
                    outputStream.writeObject(qa);
                    break;
                }

            }
            scanner.close();
            outputStream.close();
            clientSocket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("ServerQASender terminated");





    }
}

