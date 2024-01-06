package Process;

import Main.Configuration;
import Message.*;
import Socket.ISocketHandler;
import Socket.SyncClientServer;
import Socket.SyncServerSocket;

import java.util.HashMap;
import java.util.Map;

/**
 * the process in bully algorithm it may be a coordinator or simple process.
 */
public class Process implements ISocketHandler {

    private Long pid;
    private SyncServerSocket serverSocket;
    private Boolean isCoordinator = false;
    private int timeout = 10000;

    private Long lastAliveMessageCoordinator = System.currentTimeMillis() - 1000;

    private boolean canStartElection = false;
    private Configuration configuration;
    public Process(Long pid , int managerPort){
        this.pid = pid;
        this.startServer();

        this.configuration = new Configuration();
        this.configuration.setManagerConfiguration(managerPort);

        this.sendConfigurationsToManager();

        this.sendLogsToManager("Process starts : " + this.pid);
        this.serverSocket.serve();
    }


    /**
     * connect server socket to port and send it to the manager so he can connect this process and send its port to the other processes
     * so they will can communicate with each others
     */
    private void sendConfigurationsToManager(){
        SyncClientServer clientServer = new SyncClientServer( String.valueOf(this.pid) , this.configuration.getMangerConfiguration());
        HashMap<Long, Integer > config = new HashMap<Long ,Integer>();
        config.put(this.pid , this.serverSocket.getPortNumber());

        clientServer.sendMessage(new ConfigurationMessage(config));
    }

    private void startServer() {
        this.serverSocket = new SyncServerSocket(String.valueOf(this.pid) , this );
    }

    /**
     * this is the server interval and different between coordinator and normal process
     * the coordinator timeout is less than the other process so he can send alive message before they assume it is down
     * @return
     */
    @Override
    public int getServerTimeout() {
        return this.timeout;
    }

    /**
     * used in the server socket to handle the coming messages from the other processes
     * it is related to iSocketHandler
     * @param message comming from the other processes ( to make some action )
     */
    @Override
    public Message handleMessage(HashMap<Long, Integer> message) {

//        if(!message.containsKey(Long.valueOf(0))) new Message(MessageTypes.OK);

        int type = message.get(Long.valueOf(0));

        if(MessageTypes.CONFIGURATION.ordinal() == type){

            message.remove(Long.valueOf(0));

            this.canStartElection = true;
            this.configuration.setConfigurations(message);

        }
        else if (MessageTypes.ALIVE.ordinal() == type){
            this.lastAliveMessageCoordinator = System.currentTimeMillis();
        }else if(MessageTypes.ELECTION.ordinal() == type){
            this.election();
        }else if (MessageTypes.COORDINATOR.ordinal() == type){
            this.canStartElection = false;
        }

        return new Message(MessageTypes.OK);
    }

    /**
     * it will work between socket timeout
     * so we can check if the coordinator send the alive message or it is down and start the election
     *
     * this related to IsocketHandler and used in the server socket
     */
    @Override
    public boolean handleServer() {
        if( this.isCoordinator ){
            this.sendAliveToAllProcesses();
        }
        else {
            if(System.currentTimeMillis() - this.lastAliveMessageCoordinator > this.timeout){
//                this.canStartElection = true;
                this.election();
            }
        }

        return true;
    }

    private void sendAliveToAllProcesses(){

        if(System.currentTimeMillis() - this.lastAliveMessageCoordinator - 100 < this.timeout) return;
        this.lastAliveMessageCoordinator = System.currentTimeMillis();

        this.sendLogsToManager("coordinator is sending alive message to all processes : " + this.pid);

        for (Map.Entry<Long, Integer> config : configuration.getConfigurations().entrySet()) {
            // if not the manager and not this process
            if( config.getValue() != this.serverSocket.getPortNumber() && config.getKey() != Long.valueOf(1)){
                this.sendAliveMessage(config.getKey() ,config.getValue());
            }
        }
    }

    private void sendAliveMessage(Long sendPid , int port){
        SyncClientServer syncClientServer = new SyncClientServer(this.pid + " to " + sendPid , port);
        syncClientServer.sendMessage(new Message(MessageTypes.ALIVE));
    }
    private void election(){
        if(!this.canStartElection) return;
        this.canStartElection = false;

        this.sendLogsToManager("start election : " + this.pid);
        this.setIsCoordinator(false);

        if(this.sendElectionMessageToAllHigherProcesses()){
            this.setIsCoordinator(true);
            this.sendCoordinatorMessageToAllProcesses();
        }

    }

    private boolean sendElectionMessageToAllHigherProcesses(){
        boolean greatest = true;
        for (Map.Entry<Long, Integer> config : configuration.getConfigurations().entrySet()) {
            if(this.pid < config.getKey()){
                greatest = false;
                this.sendElectionMessage(config.getValue());
            }
        }

        return greatest;
    }

    private void sendCoordinatorMessageToAllProcesses(){
        if(!this.isCoordinator) return ;
        this.sendLogsToManager("i am the coordinator : " + this.pid);
        for (Map.Entry<Long, Integer> config : configuration.getConfigurations().entrySet()) {
            if(config.getKey() != this.pid && config.getKey() != Long.valueOf(1)){
                this.sendCoordinatorMessage(config.getValue());
            }
        }
    }

    private void sendCoordinatorMessage(int port){
        SyncClientServer syncClientServer = new SyncClientServer(this.pid + " to " + port , port);
        syncClientServer.sendMessage(new Message(MessageTypes.COORDINATOR));
    }

    private void sendElectionMessage(int port){
        SyncClientServer syncClientServer = new SyncClientServer(this.pid + " to " + port , port);
        syncClientServer.sendMessage(new Message(MessageTypes.ELECTION));
    }

    private void setIsCoordinator(boolean value){
        if(value == true){
            this.isCoordinator = true;
            this.timeout = 9000;
        }else {
            this.isCoordinator = false;
            this.timeout = 10000;
        }
    }

    /**
     * send the logs to manager because we in another process and can't access the UI
     * @param message
     */
    private void sendLogsToManager(String message){
        SyncClientServer managerSo = new SyncClientServer("Manager Log" , configuration.getMangerConfiguration());
        managerSo.sendMessage(new LogMessage(message));
    }

}
