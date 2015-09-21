import java.util.*;
public class Msg {
    int srcId, destId;
    String tag;
    String msgBuf;
    boolean fromUser;
    
    public Msg(int s, int t, String msgType, String buf, boolean isUser) {
        this.srcId = s;
        destId = t;
        tag = msgType;
        msgBuf = buf;
        fromUser = isUser;
    }
    public Msg(Msg newMsg){
    	this.srcId = newMsg.srcId;
    	destId = newMsg.destId;
    	tag = newMsg.tag;
    	msgBuf = newMsg.msgBuf;
    	fromUser = newMsg.fromUser;
    }
    public int getSrcId() {
        return srcId;
    }
    public int getDestId() {
        return destId;
    }
    public String getTag() {
        return tag;
    }
    public String getMessage() {
        return msgBuf;
    }
    public boolean getFromUser(){
    	return fromUser;
    }
    public int getMessageInt() {
        StringTokenizer st = new StringTokenizer(msgBuf);
        return Integer.parseInt(st.nextToken());
    }
    public static Msg parseMsg(StringTokenizer st){
        int srcId = Integer.parseInt(st.nextToken());
        int destId = Integer.parseInt(st.nextToken());
        String tag = st.nextToken(" ");
        String buf = st.nextToken("#");
        boolean isUser = Boolean.parseBoolean(st.nextToken("#"));
        return new Msg(srcId, destId, tag, buf, isUser);
    }
    public String toString(){
        String s = String.valueOf(srcId)+" " +
                    String.valueOf(destId)+ " " +
                    tag + " " + msgBuf + "#" + fromUser + "#";
        return s;
    }
}
