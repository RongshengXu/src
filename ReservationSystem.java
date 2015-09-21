import java.lang.*;
import java.util.*;
import java.net.*;
import java.io.*;

public class ReservationSystem{
	BufferedReader din;
	PrintStream pout;
	private ServerTable Table;
    private int RSID;

    public ReservationSystem(String filePath) throws Exception{
        RSID = 0;
        Table = new ServerTable(filePath);
    }
	public void getSocket() throws IOException{
        boolean found = false;
        for (int i = 0;i < Theatre.NUM_Server&&!found;i ++) {
            try {
                Socket server = new Socket(Table.getHostName(i), Table.getPort(i));
                din = new BufferedReader(new InputStreamReader(server.getInputStream()));
                pout = new PrintStream(server.getOutputStream());
                RSID = i;
                found = true;
            }catch (Exception e) {
              //  System.out.println("Wrong!");
            }
        }
	}
	
	public void search(String name) throws IOException{
		getSocket();
		boolean found = false;
		Msg message = new Msg(10086, RSID, "search", name, true);
//		pout.println(Integer.toString(10086) + " " + Integer.toString(RSID) + " " + "search " + name);
		pout.println(message.toString());
		pout.flush();
        String s = din.readLine();
        while (s == null) {
            s =din.readLine();
        }
        System.out.println(s);
	}
	
	public void reserve(String name, int count) throws IOException{
		getSocket();
		Msg message = new Msg(10086, RSID, "reserve", name + " " + count, true);
//		pout.println(Integer.toString(10086) + " " + Integer.toString(RSID) + " " + "reserve " + name + " " + count);
		pout.println(message.toString());
		pout.flush();
        String s = din.readLine();
        while (s == null) {
            s = din.readLine();
        }
        System.out.println(s);
	}
	
	public void delete(String name) throws IOException {
        getSocket();
        Msg message = new Msg(10086, RSID, "delete", name, true);
//      pout.println(Integer.toString(10086) + " " + Integer.toString(RSID) + " " + "delete" + name);
        pout.println(message.toString());
        pout.flush();
        String s = din.readLine();
        while (s == null) {
            s =din.readLine();
        }
        System.out.println(s);
    }
	
	public static void main(String [] args){
        File f = new File(".");
        String path = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-1);
		try {
            ReservationSystem rs = new ReservationSystem(path + "/src/table.txt");
            System.out.println("A new user started: ");
            rs.search("Xuan");
            rs.reserve("Xuan", 5);
            rs.search("Xuan");
            rs.reserve("Yang", 6);
            rs.search("Xuan");
            rs.search("Yang");
            rs.reserve("Xuan", 5);
           // rs.delete("Xuan");
            rs.reserve("Wang", 6);
        }
		catch (Exception e){
			System.out.println("Reservation system aborted: " + e);
		}
	}
}