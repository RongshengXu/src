import java.util.*;

public class SeatTable{
	final int seatNumber = 100;
	private String [] reserveNames = new String[seatNumber];
	private boolean [] reserveStatus = new boolean[seatNumber];
	private int num_seat_resreved = 0;
	
	public SeatTable(){
		for (int i = 0; i < seatNumber; i++){
			reserveNames[i] = "";
			reserveStatus[i] = false;
			num_seat_resreved = 0;
		}
	}
	
	int getNumLeft(){
		return seatNumber - num_seat_resreved;
	}
	
	Vector<Integer> search(String name){
		Vector<Integer> reservedSeats = new Vector<Integer>(0);
		
		for (int i = 0; i < seatNumber; i++){
			if (reserveNames[i].equals(name)){
				reservedSeats.add(i);
			}
		}
		
		System.out.println("The size of reservedSeats for " + name + " is: " + reservedSeats.size()); // ***********************
		return reservedSeats;
	}
	
	String reserve(String name, int count){
		String message;
		int num_seat_Left = getNumLeft() - count;
		Vector<Integer> newReservedSeats = new Vector<Integer>(0);
		
		if (num_seat_Left < 0){		// Fail type 1: no enough seats			 
			message = "Failed: only " + getNumLeft() + " seats left but " + count + " seats are requested.\n";
			return message;
		}
		
		Vector<Integer> reservedSeats = search(name);			
		if (!reservedSeats.isEmpty()){					// Fail type 2: no enough seats
			message = "Failed: " + name + " has booked the following seats: ";
			for (int i = 0; i < reservedSeats.size() - 1; i++){
				message += (reservedSeats.get(i) + ", ");
			}
			message += (reservedSeats.get(reservedSeats.size() - 1) + ".\n");
			
			return message;
		}
		
		for (int i = 0; i < seatNumber; i++){
			if (!reserveStatus[i]){
				newReservedSeats.add(i);
				reserveStatus[i] = true;
				reserveNames[i] = name;
				num_seat_resreved++;
				count--;
				if (count == 0){
					break;
				}
			}
		}
		
		message = ("The seats have been reserved for " + name + ": ");
		for (int i = 0; i < newReservedSeats.size() - 1; i++){
			message += (newReservedSeats.get(i) + ", ");
		}
		message += (newReservedSeats.get(newReservedSeats.size() - 1) + ".\n");		
		
		return message;	
	}
	
	String delete(String name){
		String message;
		Vector<Integer> reservedSeats = search(name);
		if (reservedSeats.isEmpty()){
			message = "Failed: no reservation is made by " + name + ".\n";
			return message;
		}
		
		for (int i =0; i < reservedSeats.size(); i++){
			reserveStatus[reservedSeats.get(i)] = false;
			reserveNames[reservedSeats.get(i)] = "";
			num_seat_resreved--;
		}
		int num_seats_released = reservedSeats.size();
		int num_seat_left = seatNumber - num_seat_resreved;
		
		message = (num_seats_released + " seats have been released. " + num_seat_left +
							" seats are now available.\n");
		return message;
	}

	public static void main(String [] args){
		SeatTable table = new SeatTable();
		table.search("Xuan");
		table.reserve("Xuan", 5);
		table.reserve("Yang", 6);
		table.search("Xuan");
//		table.reserve("Xuan", 5);
//		table.delete("Xuan");
//		table.reserve("Wang", 6);
	}
}