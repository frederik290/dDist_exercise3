import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Jeppe Vinberg on 11-04-2016.
 */
public class ServerQAReceiver implements Runnable {

    private Socket clientSocket;
    private ArrayBlockingQueue<QA> queue;

    public ServerQAReceiver(Socket clientSocket, ArrayBlockingQueue<QA> queue){
        this.clientSocket = clientSocket;
        this.queue = queue;
    }


    @Override
    public void run() {
        QA qa = null;
        try{
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            while(true){
                qa = (QA) inputStream.readObject();
                queue.put(qa);
                if(qa.getQuestion() == null) break;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("ServerQAReceiver terminated");

    }
}
