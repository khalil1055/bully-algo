package Message;

import java.util.HashMap;
import java.util.Map;

// Todo : this is bad chouse to use encode & decode like this but it will take time to change the message system and make it more dynamic
public class LogMessage extends Message {

    private String message;

    /**
     * encode and decode the strings to fit integer HashMap to send it between processes
     * @param message
     */
    public LogMessage(String message) {
        super(MessageTypes.LOG);
        this.message = message;
    }


    @Override
    public HashMap<Long, Integer> getMessage() {
        return messageEncoding();
    }

    public HashMap<Long, Integer> messageEncoding() {
        HashMap<Long, Integer> encoded = new HashMap<Long, Integer>();
        encoded.put(Long.valueOf(0), this.type.ordinal());

        for (int i = 0; i < this.message.length(); i++) {
            encoded.put(Long.valueOf(i + 1), Integer.valueOf(this.message.charAt(i)));
        }

        return encoded;
    }

    public static String messageDecoding(HashMap<Long ,Integer> encoded){
        String message ="";
        for (Map.Entry<Long, Integer> m : encoded.entrySet()) {
            if(m.getKey() > 0){
                int c = m.getValue();
                message += (char)(c) ;
            }
        }
        return message;
    }
}
