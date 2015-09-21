import sun.util.BuddhistCalendar;

import java.net.Socket;
import java.util.*;
import java.io.*;
public class Linker {
    PrintWriter[] dataOut;
    BufferedReader[] dataIn;
    BufferedReader dIn;
    int myId, N;
    private Socket[] link;
    private void connect(ServerTable t, int Id, int numProc, BufferedReader[] dataIn, PrintWriter[] dataOut)
            throws Exception{
        for (int i = 0;i < Id;i++) {
            link[i] = new Socket(t.getHostName(i),t.getPort(i));
            dataIn[i] = new BufferedReader(
                    new InputStreamReader(link[i].getInputStream()));
            dataOut[i] = new PrintWriter(link[i].getOutputStream());
            dataOut[i].println(Id + " " + i + " " + "hello" + " " + "null");
            dataOut[i].flush();
        }
    }
    public Linker(ServerTable t, int id, int numProc) throws Exception {
        link = new Socket[numProc];
        myId = id;
        N = numProc;
        dataIn = new BufferedReader[numProc];
        dataOut = new PrintWriter[numProc];
        connect(t, id, numProc, dataIn, dataOut);
    }
    public void setLink(int Id, Socket s) throws Exception {
        System.out.println("Set link for ID:" + Id);
        link[Id] = s;
        dataIn[Id] = new BufferedReader(
                        new InputStreamReader(link[Id].getInputStream()));
        dataOut[Id] = new PrintWriter(link[Id].getOutputStream());
    }
    public void sendMsg(int destId, String tag, String msg) {     
        dataOut[destId].println(myId + " " + destId + " " + 
				      tag + " " + msg + "#");
        dataOut[destId].flush();
    }
    public void sendMsg(int destId, String tag) {
        sendMsg(destId, tag, " 0 ");
    }
    public void multicast(IntLinkedList destIds, String tag, String msg){
        for (int i=0; i<destIds.size(); i++) {
            System.out.println(tag+ " " + msg);
            sendMsg(destIds.getEntry(i), tag, msg);
        }
    }
    public Msg receiveMsg(int fromId) throws IOException  {        
        String getline = dataIn[fromId].readLine();
        Util.println(" received message " + getline);
        StringTokenizer st = new StringTokenizer(getline);
        int srcId = Integer.parseInt(st.nextToken());
        int destId = Integer.parseInt(st.nextToken());
        String tag = st.nextToken();
        String msg = st.nextToken("#");
        return new Msg(srcId, destId, tag, msg, true);
    }
    public int getMyId() { return myId; }
    public int getNumProc() { return N; }
}
