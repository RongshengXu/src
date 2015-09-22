import java.net.ServerSocket;
import java.net.Socket;
public class Server {
        public static void main(String[] args) {
        	if (args.length < 1){
        		System.out.println("Please specify the server id!");
        		return;
        	}
            System.out.println("SeatServer " + args[0] +" started: ");
            try{
                String PATH = "/Users/rongshengxu/Documents/Distributed Systems/Programming Assignment" +
                        "/HW2_Programming_Part/src/table.txt";
                SeatServer ss = new SeatServer(Integer.parseInt(args[0]), PATH);
                ServerSocket listener = new ServerSocket(ss.get_port());
                ss.sync();
                while (true){
                    System.out.println("*****Waiting for a client:*****");
                    Socket aClient = listener.accept();
                    ss.config(aClient);
                    //aClient.close();
                }
            }
            catch (Exception e){
                System.out.println("SeatServer " + args[0] +" aborted:" + e);
            }
        }
}
