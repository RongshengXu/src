/**
 * Created by rongshengxu on 9/20/15.
 */
import java.net.*;
public class Server4 {
    public static void main(String[] args) {
        System.out.println("SeatServer started: ");
        try{
            SeatServer ss = new SeatServer(4);
            ServerSocket listener = new ServerSocket(ss.get_port());

            while (true){
                System.out.println("*****Waiting for a client:*****");
                Socket aClient = listener.accept();
                ss.config(aClient);
                //aClient.close();
            }
        }
        catch (Exception e){
            System.out.println("SeatServer aborted:" + e);
        }
    }
}
