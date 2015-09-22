import java.io.*;

/**
 * Created by rongshengxu on 9/21/15.
 */
import java.net.*;
import java.lang.*;
import java.util.*;
public class Client1 {
    public static void main(String[] args) {
        File f = new File(".");
        String path = f.getAbsolutePath().substring(0, f.getAbsolutePath().length() - 1);
        try {
            while (true) {
                ReservationSystem rs = new ReservationSystem(path + "/table.txt");
                System.out.print("Command-> ");
                Scanner in = new Scanner(System.in);
                String command = in.nextLine();

                StringTokenizer st = new StringTokenizer(command);
                String tag = st.nextToken();
                if (tag.equals("delete")) {
                	if (st.hasMoreTokens()){
                		String name = st.nextToken();
                        rs.delete(name);
                	}
                	else{
                		System.out.println("Please specify a name for the delete command!");
                	}
                }
                else if (tag.equals("reserve")) {
                	if (!st.hasMoreTokens()){
                		System.out.println("Please specify a name and count for the reserve command!");
                	}
                	else{
                		String name = st.nextToken();
                		if (!st.hasMoreTokens()){
                			System.out.println("Please specify a count for the reserve command!");
                		}
                		else{
		                    int count = Integer.parseInt(st.nextToken());
		                    rs.reserve(name, count);
                		}
                	}
                }
                else if (tag.equals("search")) {
                	if (st.hasMoreTokens()){
                        String name = st.nextToken();
                        rs.search(name);	
                	}
                	else{
                		System.out.println("Please specify a name for the search command!");
                	}
                }
                else{
                	System.out.println("Not a valid command!");
                }
            }
        } catch (Exception e) {
            System.out.println("Reservation system aborted: " + e);
        }
    }
}
