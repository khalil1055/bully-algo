package Message;

import java.util.HashMap;

// TODO: it is a big fault to make the message handled with HashMap<Long,Integer> so it is make some restrication on the messages
//  it is better to send objects between the process so it will be more dynamic
public class Message {

    protected MessageTypes type;

    /**
     * messages between the processes so there is a standard between them
     *
     * @param type
     */
    public Message(MessageTypes type){
        this.type = type;
    }

    public HashMap<Long,Integer> getMessage(){
        HashMap<Long,Integer> mp = new HashMap<Long,Integer>();
        mp.put(Long.valueOf(0) , type.ordinal());

        return mp;
    }


}