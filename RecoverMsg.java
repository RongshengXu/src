import java.util.*;

public class RecoverMsg{
	public static String NameArrayToString(String [] reserveNames){
		String result = new String("");
		for (int i = 0; i < reserveNames.length; i++){
			result += reserveNames[i] + "$";
		}
		return result;
	}
	public static String StatusArrayToString(boolean [] reserveStatus){
		String result = new String("");
		for (int i = 0; i < reserveStatus.length; i++){
			result += reserveStatus[i] + "$";
		}
		return result;
	}
	public static String [] StringToNameArray(String nameString, int length){
		String [] result = new String[length];
		StringTokenizer st = new StringTokenizer(nameString);
		
		for (int i = 0; i < length; i++){
			result[i] = st.nextToken("$");
		}
		
		return result;
	}
	public static boolean [] StringToStatusArray(String statusString, int length){
		boolean [] result = new boolean[length];
		StringTokenizer st = new StringTokenizer(statusString);
		
		for (int i = 0; i < length; i++){
			result[i] = Boolean.parseBoolean(st.nextToken("$"));
		}
		
		return result;		
	}
	public static void main(String [] args){
		final int testSize = 10;
		String names[] = new String[testSize];
		boolean status[] = new boolean[testSize];
		
		for (int i = 0; i < 10; i++){
			names[i] = "*";
			status[i] = false;
		}
		
		System.out.println("The length is " + names.length);
		
		String namesString = RecoverMsg.NameArrayToString(names);
		String statusString = RecoverMsg.StatusArrayToString(status);
		
		System.out.println("The name array is coverted to: " + namesString);
		System.out.println("The status array is coverted to " + statusString);
		
		String[] namesArray = new String[testSize];
		namesArray = (RecoverMsg.StringToNameArray(namesString, testSize)); 
		
		boolean[] statusArray = new boolean[testSize];
		statusArray = (RecoverMsg.StringToStatusArray(statusString, testSize));

		System.out.println("The name array is: " + namesArray[0]);
		System.out.println("The status array is: " + statusArray[0]);
	}
}