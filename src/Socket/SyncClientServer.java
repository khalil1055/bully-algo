package Socket;

import Message.Message;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * handle all client servers in one place with messages.
 */
public class SyncClientServer extends SyncSocket {

    private Socket echoSocket;
    private int port;
    private String server_name = "127.0.0.1";

    public SyncClientServer(String name, int port) {
        super(name);
        this.port = port;
    }

    public boolean sendMessage(Message message) {
        boolean success = false;
        try {
            this.echoSocket = new Socket(this.server_name, this.port);

            OutputStream out = echoSocket.getOutputStream();
            InputStream in = echoSocket.getInputStream();

            ObjectOutputStream mapOutputStream = new ObjectOutputStream(out);
            mapOutputStream.writeObject(message.getMessage());

//            ObjectInputStream mapInputStream = new ObjectInputStream(in);
//            HashMap<Long, Integer> response = (HashMap<Long, Integer>) mapInputStream.readObject();

            success = true;
            in.close();
            out.close();
            this.echoSocket.close();

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return success;
    }

}
