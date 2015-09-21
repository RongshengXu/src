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
            ReservationSystem rs = new ReservationSystem(path + "/src/table.txt");
            System.out.println("A new user started: ");
            rs.search("1");
            rs.reserve("1", 5);
            rs.search("1");
            rs.reserve("2", 6);
            rs.search("2");
            rs.search("3");
            rs.reserve("3", 5);
            rs.delete("Xuan");
            rs.reserve("3", 6);
        } catch (Exception e) {
            System.out.println("Reservation system aborted: " + e);
        }
    }
}
