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
                ReservationSystem rs = new ReservationSystem(path + "/src/table.txt");
                System.out.println("Command:");
                Scanner in = new Scanner(System.in);
                String command = in.nextLine();
                System.out.println(command);
                StringTokenizer st = new StringTokenizer(command);
                String tag = st.nextToken();
                if (tag.equals("delete")) {
                    String name = st.nextToken();
                    rs.delete(name);
                }
                else if (tag.equals("reserve")) {
                    String name = st.nextToken();
                    int count = Integer.parseInt(st.nextToken());
                    rs.reserve(name, count);
                }
                else if (tag.equals("search")) {
                    String name = st.nextToken();
                    rs.search(name);
                }
            }
        } catch (Exception e) {
            System.out.println("Reservation system aborted: " + e);
        }
    }
}
