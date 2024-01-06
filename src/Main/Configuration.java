package Main;

import java.util.HashMap;

/**
 * configuration standard between the processes
 */
public class Configuration {

    private static Configuration instance;

    private HashMap< Long , Integer > configuration;
//
//    public static Configuration getInstance(){
//        if(instance != null){
//            return instance;
//        }
//        instance = new Configuration();
//        return instance;
//    }
    public Configuration(){
        this.configuration = new HashMap<Long , Integer>();
    }

    public void addConfiguration(Long pid , Integer port){
        if(pid == Long.valueOf(0)) return;
        this.configuration.put(pid ,port);
    }

    public Integer getConfiguration(Long pid){
        return this.configuration.get(pid);
    }

    public void setManagerConfiguration(Integer port){
        this.addConfiguration(1L, port);
    }

    public Integer getMangerConfiguration(){
        return this.getConfiguration(1L);
    }

    public HashMap<Long , Integer> getConfigurations(){
        return this.configuration;
    }

    public void setConfigurations(HashMap<Long , Integer> configuration){
        this.configuration = configuration;
    }

    public void removeConfiguration(Long pid){
        this.configuration.remove(pid);
    }

}
