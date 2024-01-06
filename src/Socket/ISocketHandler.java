package Socket;

import Message.Message;

import java.util.HashMap;

/**
 * handle operation between sockets and classes that run the servers.
 * todo : it should handled with better way
 */
public interface ISocketHandler {

    int getServerTimeout();
    Message handleMessage(HashMap<Long , Integer > message);

    boolean handleServer();

}
