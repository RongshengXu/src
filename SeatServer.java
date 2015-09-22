import com.sun.corba.se.spi.activation.Server;

import javax.swing.plaf.synth.SynthEditorPaneUI;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.Semaphore;
import java.util.zip.InflaterOutputStream;

public class SeatServer{
	private SeatTable table;
    private int Server_ID;
    private ServerTable t;
	private DirectClock DC;
	private int[] q;
	private int done_counter;
	private boolean[] down;
	private boolean sycned;
	static Semaphore semaphore;
	private int health_counter;
    private boolean sycned_once;
	public class newRun implements Runnable {
		private Socket s;

		public newRun(Socket so) {
			s = so;
		}
		public void run() {
			//System.out.println("thread");
			handleclient(s);
		}
	}

	Thread check = new Thread(){
		public void run() {
			health_check();
		}
	};
	public void config(Socket s) {
		Thread th = new Thread(new newRun(s));
		th.start();
	}

	public IntLinkedList neighbors = new IntLinkedList();

	public SeatServer(int ID, String filePath) throws Exception{
		semaphore = new Semaphore(1);
        //semaphore.acquire();
        t = new ServerTable(filePath);
        DC = new DirectClock(t.getSize(), ID);
        q = new int[t.getSize()];
		down = new boolean[t.getSize()];
        Server_ID = ID;
		table = new SeatTable();
		sycned = false;
        sycned_once = false;
		for (int i = 0;i < q.length;i++) {
			q[i] = Integer.MAX_VALUE;
			down[i] = false;
		}
		done_counter = 0;
		health_counter = 0;
        //semaphore.release();
	}
    public void sync() {
        sendFirstAvail("recover", "0", false);
    }
    private void sendFirstAvail(String tag, String msg, boolean lamport) {
        boolean found =false;
        for (int i = 0;i < t.getSize()&&!found;i++) {
            if (i != Server_ID) {
                try {
                    Msg m = new Msg(Server_ID, i, tag, msg, lamport);
                    Socket s = new Socket(t.getHostName(i), t.getPort(i));
                    s.setSoTimeout(5000);
                    PrintWriter dataOut = new PrintWriter(s.getOutputStream());
                    dataOut.println(m.toString());
                    dataOut.flush();
                    BufferedReader din = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String str = din.readLine();
                    found = true;
                    //s.close();
                } catch (Exception e) {
                }
            }
        }
    }
	// Lamport algorithm method functions
	private void multicast(String tag, String msg, boolean lamport) {
		for (int i=0;i < t.getSize();i++) {
			if (i != Server_ID)
				sendMsg(i, tag, msg, lamport);
		}
		//System.out.println("multi done!");
	}
	private void sendMsg(int dest, String tag, String msg, boolean lamport) {
		Msg m;
		m = new Msg(Server_ID, dest, tag, msg, lamport);
		try {
			Socket s = new Socket(t.getHostName(dest), t.getPort(dest));
			s.setSoTimeout(5000);
			PrintWriter dataOut = new PrintWriter(s.getOutputStream());
            System.out.println("Send message:" + tag);
			dataOut.println(m.toString());
			dataOut.flush();
			BufferedReader din = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String str = din.readLine();
			s.close();
		} catch (Exception e) {
            System.err.println("Send message error: "+e);
            System.out.println(dest + " " + tag);
			down[dest] = true;
			if (tag.equals("delete")&&lamport) {
				done_counter++;
			} else if (tag.equals("reserve")&&lamport) {
                done_counter++;
            }
			else if (tag.equals("recover")) {
				health_counter++;
			}
			//System.err.println("Send message: " + e);
		}
	}
	private void Lamport_request() throws Exception{
		semaphore.acquire();
		q[Server_ID] = DC.getValue(Server_ID);
		DC.sendAction();
		System.out.println("TimeStamp: " + q[Server_ID]);
		multicast("request", Integer.toString(q[Server_ID]), true);
		semaphore.release();
	}
	private void Lamport_release() throws Exception{
		semaphore.acquire();
		q[Server_ID] = Integer.MAX_VALUE;
		DC.sendAction();
		multicast("release", Integer.toString(q[Server_ID]), true);
		semaphore.release();
	}
	private boolean less(int a1, int b1, int a2, int b2) {
		if (a1 < a2) {
			return true;
		} else if (a1 > a2) {
			return false;
		} else {
			return (b1 < b2);
		}
	}
	private void Lamport_wait() throws Exception{
		boolean OK = false;
		while (!OK) {
			semaphore.acquire();
			//System.out.println("Wait...");
			//System.out.println(down[0]);
			OK = true;
			for (int i = 0; i < t.getSize(); i++) {
				if (i != Server_ID && !down[i]) {
					if (!less(q[Server_ID], Server_ID, DC.getValue(i), i))
						OK = false;
					if (!less(q[Server_ID], Server_ID, q[i], i))
						OK = false;
				}
			}
			semaphore.release();
		}
	}
	private void done_check() {
		while (done_counter < t.getSize() - 1) {
		}
	}
	private void health_check() {
		while (health_counter < t.getSize()-1) {
            System.out.println("health wait...");
		}
        //sycned = true;
	}
	// Public user methods
    public int get_port() {
        return t.getPort(Server_ID);
    }

	public String get_host() {
		return t.getHostName(Server_ID);
	}
	private void heartbeat(Socket sock) throws Exception{
		PrintWriter p = new PrintWriter(sock.getOutputStream());
		p.println("receiver");
		p.flush();
	}
	void handleclient(Socket theClient){
		try{
			BufferedReader din = new BufferedReader(new InputStreamReader(theClient.getInputStream()));
			PrintStream pout = new PrintStream(theClient.getOutputStream());
			String getline = din.readLine();
			//System.out.println(getline);
			StringTokenizer st = new StringTokenizer(getline);
			//System.out.println(getline);
			Msg m = Msg.parseMsg(st);
			int source = m.getSrcId();
			int dest = m.getDestId();
			String tag = m.getTag();
			String mbuff = m.getMessage();
			boolean lamport = m.getFromUser();
			//System.out.println(tag);
			if (source < 10086) {
				heartbeat(theClient);
			}
            //System.out.println("handle");
			//System.out.println("In");
			if (tag.equals("search")) {
				if (lamport) {
					Lamport_request();
					Lamport_wait();
				}
				semaphore.acquire();
				StringTokenizer s1 = new StringTokenizer(mbuff);
				Vector<Integer> reservedSeats = table.search(s1.nextToken());
				semaphore.release();
				if (lamport) {
					if (reservedSeats.isEmpty()) {
						pout.println("Failed: no reservation is made by " + mbuff + ".");
					} else {
						pout.println("There are " + reservedSeats.size() + " reserved for " + mbuff + ".");
					}
					//done_check();
					Lamport_release();
				}
			} else if (tag.equals("reserve")) {
				if (lamport) {
					Lamport_request();
					Lamport_wait();
				}
				semaphore.acquire();
				StringTokenizer s2 = new StringTokenizer(mbuff);
				String name = s2.nextToken();
				int count = Integer.parseInt(s2.nextToken());
				String message = table.reserve(name, count);
				semaphore.release();
				if (lamport) {
					pout.println(message);
					multicast(tag, mbuff, false);
                    done_check();
					Lamport_release();
				} else {
					sendMsg(source, "done", "0", false);
				}
			} else if (tag.equals("delete")) {
				if (lamport) {
					Lamport_request();
					Lamport_wait();
				}
				semaphore.acquire();
				StringTokenizer s3 = new StringTokenizer(mbuff);
				String message = table.delete(s3.nextToken());
				semaphore.release();
				if (lamport) {
					pout.println(message);
					multicast(tag, mbuff, false);
                    done_check();
					Lamport_release();
				} else {
					sendMsg(source, "done", "0", false);
				}
			} else if (tag.equals("request")) {
				//System.out.println(mbuff);
				semaphore.acquire();
				StringTokenizer s4 = new StringTokenizer(mbuff);
				int time = Integer.parseInt(s4.nextToken());
				DC.receiveAction(source, time);
				q[source] = time;
				sendMsg(source, "ack", Integer.toString(DC.getValue(Server_ID)), true);
				System.out.println("Send ACK");
				DC.sendAction();
				semaphore.release();
			} else if (tag.equals("release")) {
				semaphore.acquire();
				StringTokenizer s5 = new StringTokenizer(mbuff);
				int time = Integer.parseInt(s5.nextToken());
				DC.receiveAction(source, time);
				q[source] = time;
				semaphore.release();
			} else if (tag.equals("ack")) {
				semaphore.acquire();
				System.out.println("Got ACK");
				StringTokenizer s6 = new StringTokenizer(mbuff);
				int time = Integer.parseInt(s6.nextToken());
				DC.receiveAction(source, time);
				semaphore.release();
			} else if (tag.equals("done")) {
				semaphore.acquire();
				done_counter++;
				semaphore.release();
			}
			else if (tag.equals("recover")) {
                //semaphore.acquire();
                System.out.println("data sent!");
                String[] nameArray = table.getNamesArray();
                boolean[] statusArray = table.getStatusArray();
                String seat_message = RecoverMsg.NameArrayToString(nameArray) + " "
                        + RecoverMsg.StatusArrayToString(statusArray);
                sendMsg(source, "data", seat_message, false);
                //semaphore.release();
			}
			else if (tag.equals("data")) {
                StringTokenizer data = new StringTokenizer(mbuff);
                System.out.println(mbuff);
                System.out.println("Syncronizing...");
                if (!data.hasMoreTokens()){
                    System.out.println("There is no further data for NameArray!!!");
                }
                String str1 = data.nextToken();
                if (!data.hasMoreTokens()){
                    System.out.println("There is no further data for StatusArray!!!");
                }
                String str2 = data.nextToken();
                String[] temp1 = RecoverMsg.StringToNameArray(str1, table.getNumTotal());
                boolean[] temp2 = RecoverMsg.StringToStatusArray(str2, table.getNumTotal());
                System.out.print(str1 + " " + str2);
                System.out.println(temp1[0]);
                System.out.println(temp2[0]);
                //System.out.println("Begin");
                for (int i = 0;i < table.getNumTotal();i++) {
                    table.setNamesArray(temp1[i], i);
                    if (i%10 == 0 && i != 0) {
                        System.out.println(" ");
                    }
                    System.out.print(temp1[i] + " ");
                }
                System.out.println(" ");
                for (int i = 0;i < table.getNumTotal();i++) {
                    table.setStatusArray(temp2[i],i);
                    if (i%10 == 0 && i != 0) {
                        System.out.println(" ");
                    }
                    System.out.print(temp2[i] + " ");
                }
                System.out.println(" ");
				sycned = true;
			}
           // theClient.close();
		}
		catch (Exception e){
			System.out.println("Handleclient exception: " + e);
			semaphore.release();
		}
	}

	public static void main(String [] args){
		System.out.println("SeatServer started: ");
		try{
            String PATH = "./table.txt";
            SeatServer ss = new SeatServer(0, PATH);
			ServerSocket listener = new ServerSocket(ss.get_port());
			while (true){
				System.out.println("*****Waiting for a client:*****");
				Socket aClient = listener.accept();
				aClient.close();
			}
		}
		catch (Exception e){
            File f = new File(".");
            System.out.println(f.getAbsolutePath());
			System.out.println("SeatServer aborted:" + e);
		}
	}
}