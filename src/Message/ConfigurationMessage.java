package Message;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationMessage extends Message{

    private HashMap<Long , Integer> config;

    /**
     * send configuration between procesess
     * @param config
     */
    public ConfigurationMessage(HashMap<Long , Integer> config) {
        super(MessageTypes.CONFIGURATION);
        this.config = config;
    }


    @Override
    public HashMap<Long , Integer> getMessage() {
        HashMap<Long , Integer> mp = new HashMap<>();
        for (Map.Entry<Long, Integer> m : this.config.entrySet()) {
            mp.put(m.getKey() , m.getValue());
        }
        mp.put( Long.valueOf(0) , this.type.ordinal());
        return mp;
    }
}
