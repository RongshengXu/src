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
    private int Size;
    private List<String>  names = new ArrayList<String>();
    private List<String>  hosts = new ArrayList<String>();
    private List<Integer> ports = new ArrayList<Integer>();
    private int dirsize = 0;
    public ServerTable(String filPath) throws IOException{
        int i = 0;
        Size = 0;
        FileReader fr = new FileReader(filPath);
        BufferedReader bf = new BufferedReader(fr);
        String getline = bf.readLine();
        while (getline != null) {
            StringTokenizer st = new StringTokenizer(getline);
            hosts.add(st.nextToken());
            ports.add(Integer.parseInt(st.nextToken()));
            getline = bf.readLine();
            Size++;
        }
        fr.close();
    }
    public int getSize() {
        return Size;
    }
    int getPort(int index) {
        return ports.get(index);
    }
    String getHostName(int index) {
        return hosts.get(index);
    }
    public static void main(String[] args) {
        try {
            File here = new File(".");
            String path = here.getAbsolutePath().substring(0,here.getAbsolutePath().length()-1);
            ServerTable STable = new ServerTable(path+"src/table.txt");
            for (int i = 0;i < STable.getSize();i++) {
                System.out.println(STable.getHostName(i) + ":" + STable.getPort(i));
            }
        } catch (Exception e) {
            System.err.println("Error:" + e);
        }
    }
}
