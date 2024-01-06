package Socket;

import Message.Message;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class SyncServerSocket extends SyncSocket {

    private static int port_number = (int) (Math.random()*100) + 8101;
    private int cur_port;
    private ServerSocket serverSocket;

    private ISocketHandler SocketHandler;

    private Socket clientSocket;

    public SyncServerSocket(String name , ISocketHandler SocketHandler ){
        super(name);
        this.createServerSocket();
        this.SocketHandler = SocketHandler;
    }
    public ServerSocket createServerSocket(){
        try {
            serverSocket = new ServerSocket(port_number);
            this.cur_port = port_number;
            port_number = port_number + 1;
        } catch (BindException e) {
            port_number++;
            createServerSocket();
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        return serverSocket;
    }

    public int getPortNumber(){
        return this.cur_port;
    }

    /**
     * server socket to communicate between the processess and handle peer to peer
     */
    public void serve(){

        while(SocketHandler.handleServer()){
            try {
                this.serverSocket.setSoTimeout(this.SocketHandler.getServerTimeout());
                this.clientSocket = this.serverSocket.accept();

                OutputStream out = this.clientSocket.getOutputStream();
                InputStream in = this.clientSocket.getInputStream();

                ObjectInputStream mapInputStream = new ObjectInputStream(in);
                HashMap<Long , Integer> message = (HashMap<Long, Integer>) mapInputStream.readObject();

                Message responseMessage = this.SocketHandler.handleMessage(message);

//                ObjectOutputStream mapOutputStream = new ObjectOutputStream(out);
//                mapOutputStream.writeObject(responseMessage.getMessage());

            } catch (SocketException e) {
                throw new RuntimeException(e);
            }catch (SocketTimeoutException e){
            }catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
    }


}
