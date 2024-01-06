package Main;

import Message.ConfigurationMessage;
import Socket.ISocketHandler;
import Message.Message;
import Message.MessageTypes;
import Socket.SyncClientServer;
import Socket.SyncServerSocket;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Message.LogMessage;

/**
 * used to hande UI frame & create process & handle and send configuration between the processes
 */
public class Manager implements ISocketHandler {


    private static MyFrame frame;
    private static Manager instance = null;
    private List<Process> processesList = new ArrayList<Process>();

    private SyncServerSocket serverSc;

    private static Configuration configuration;

    public static Manager getInstance() {
        if (instance != null) {
            return instance;
        }

        instance = new Manager();
        frame = new MyFrame();
        configuration = new Configuration();

        return instance;
    }

    private Manager() {
    }

    private Manager(int processCount) {
        this.createProcesses(processCount);
    }

    public void createProcesses(int processCount) {
        for (int i = 0; i < processCount; i++) {
            addProcess();
        }
    }


    /**
     * create the command to run new process.
     * @return
     */
    private List<String> getCommand() {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = "Process.StartProcess";

        List<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-cp");
        command.add(classpath);
        command.add(className);
        command.add(String.valueOf(this.serverSc.getPortNumber()));

        return command;
    }


    public Long addProcess() {
        try {
            List<String> command = this.getCommand();

            ProcessBuilder builder = new ProcessBuilder(command);
            Process process = builder.inheritIO().start();
            this.processesList.add(process);

            return process.pid();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void waitForProcesses() {

        for (int i = 0; i < this.processesList.size(); i++) {
            try {
                Process process = this.processesList.get(i);
                process.waitFor();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    /**
     * serve manager server socket to get the configuration from the processes
     * and send the configuration updates when made
     */
    public void socketServe() {
        this.serverSc = new SyncServerSocket("Manager", this);
        configuration.setManagerConfiguration(this.serverSc.getPortNumber());
        this.serverSc.serve();
    }

    /**
     * get process names to view it in the UI
     * @return
     */
    public List<String> getProcessNamesList() {
        List<String> processNames = new ArrayList<String>();

        for (int i = 0; i < this.processesList.size(); i++) {

            Process process = this.processesList.get(i);
            processNames.add(String.valueOf(process.pid()));

        }

        return processNames;
    }

    /**
     * when get all process configuration from each process
     * so will send this updates to all process to communicate between
     */
    private void sendConfigurationToAllProcesses() {


        HashMap<Long, Integer> configurations = configuration.getConfigurations();

        for (Map.Entry<Long, Integer> m : configurations.entrySet()) {
            if (m.getKey() != Long.valueOf(0) && m.getValue() != this.serverSc.getPortNumber()) {
                this.sendConfigurationsToProcess(m.getKey(), m.getValue(), configurations);
            }
        }

    }

    private void sendElectionMessage(Long processId, int portNumber, HashMap<Long, Integer> configurations) {
        SyncClientServer clientServer2 = new SyncClientServer("Manager send To " + processId, portNumber);
        clientServer2.sendMessage(new Message(MessageTypes.ELECTION));

    }

    private void sendConfigurationsToProcess(Long processId, int portNumber, HashMap<Long, Integer> configurations) {

        SyncClientServer clientServer2 = new SyncClientServer("Manager To " + processId, portNumber);
        clientServer2.sendMessage(new ConfigurationMessage(configurations));
    }

    @Override
    public int getServerTimeout() {
        return 1000;
    }

    /**
     * handle messages coming form the processes
     *
     * @param message
     */
    @Override
    public Message handleMessage(HashMap<Long, Integer> message) {

        int type = message.get(Long.valueOf(0));
        message.remove(Long.valueOf(0));

        if (MessageTypes.CONFIGURATION.ordinal() == type) {
            for (Map.Entry<Long, Integer> m : message.entrySet()) {
                configuration.addConfiguration(m.getKey(), m.getValue());
            }

            if (configuration.getConfigurations().size() == this.processesList.size() + 1) {
                this.sendConfigurationToAllProcesses();
            }
        }else if(MessageTypes.LOG.ordinal() == type){
            this.addLog(LogMessage.messageDecoding(message));
        }

        return new Message(MessageTypes.OK);
    }

    /**
     * handle work between server timeout.
     * @return
     */
    @Override
    public boolean handleServer() {
        return true;
    }

    public boolean destroyProcess(Long pid) {
        int ndx = -1;
        for (int i = 0; i < this.processesList.size(); i++) {
            if (this.processesList.get(i).pid() == pid) {
                ndx = i;
                break;
            }
        }

        if (ndx == -1) {
            return false;
        }

        this.destroyProcess(this.processesList.get(ndx), false);
        this.processesList.remove(ndx);

        return true;
    }

    private void destroyProcess(Process process, boolean removeAll) {

        configuration.removeConfiguration(process.pid());
        //ToDo stop & remove sockets
        if (!removeAll) {
            this.sendConfigurationToAllProcesses();
        }
        this.addLog("kill process : " + process.pid());
        process.destroy();
    }

    public void destroyAllProcesses() {
        for (Process process : this.processesList) {
            destroyProcess(process, true);
        }
    }


    public void addLog(String message){
        frame.addLog(message);
    }
}
