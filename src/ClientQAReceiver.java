

import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Created by Jeppe Vinberg on 11-04-2016.
 */
public class ClientQAReceiver implements Runnable {

    private Socket socket;

    public ClientQAReceiver(Socket socket){
        this.socket = socket;
    }


    @Override
    public void run() {
        try{
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            QA response = null;
            while(true){
                response = (QA) inputStream.readObject();
                if(response.getAnswer() == null) break;
                System.out.println("The answer to question '" + response.getQuestion() + "' is '" + response.getAnswer() + "'.");
            }


        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("ClientQAReceiver terminated");
    }
}
