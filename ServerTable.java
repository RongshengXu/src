/**
 * Created by rongshengxu on 9/19/15.
 */
import java.io.File;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Exchanger;

public class ServerTable {
    final int Size = Theatre.NUM_Server;
    private String [] names = new String[Size];
    private String [] hosts = new String[Size];
    private int [] ports = new int[Size];
    private int dirsize = 0;
    public ServerTable(String filPath) throws IOException{
        int i = 0;
        FileReader fr = new FileReader(filPath);
        BufferedReader bf = new BufferedReader(fr);
        String getline = bf.readLine();
        while (getline != null) {
            StringTokenizer st = new StringTokenizer(getline);
            hosts[i] = st.nextToken();
            ports[i] = Integer.parseInt(st.nextToken());
            i++;
            getline = bf.readLine();
        }
    }
    int getPort(int index) {
        return ports[index];
    }
    String getHostName(int index) {
        return hosts[index];
    }
    public static void main(String[] args) {
        try {
            File here = new File(".");
            String path = here.getAbsolutePath().substring(0,here.getAbsolutePath().length()-1);
            ServerTable STable = new ServerTable(path+"src/table.txt");
        } catch (Exception e) {
            System.err.println("Error:" + e);
        }
    }
}
