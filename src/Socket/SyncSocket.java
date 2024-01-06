package Socket;

public class SyncSocket {
    protected String name;

    /**
     * handle all processes sockets in one place with our standards
     * @param name
     */
    protected SyncSocket(String name){
        this.name = name;
    }
}
