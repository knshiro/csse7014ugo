package au.edu.uq.csse7014.assignment1.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import au.edu.uq.csse7014.assignment1.model.Programme;

public class SmartHomeUI {

	
	public SmartHomeUI() {
		BufferedReader stdin = new BufferedReader (new InputStreamReader(System.in));
	    String message;
	    Programme prog;
	    int start,end;
		
	    try {
		System.out.println("Welcome to the Smart Home Monitoring System");
		System.out.println("Please enter your user name:");
		System.out.flush(); // empties buffer, before you input text

		message = stdin.readLine();

		System.out.println("Welcome to the Smart Home Monitoring System");
		System.out.println("Please select an option:");
		System.out.println("1. View Log");
		System.out.println("2. Get Programme Description");
		System.out.println("3. Get Today's Listings for Period");
		System.out.println("E. Exit");		
		System.out.flush(); // empties buffer, before you input text

		message = stdin.readLine();
		
		switch(message.charAt(0)){
		case '1':
			if((message = viewLog()).equals("")) {
				System.out.println("Log is empty");
			} else {
				System.out.println(message);
			}
			break;
			
		case '2':
			System.out.println("Please enter programme name:");
			System.out.flush(); // empties buffer, before you input text
			message = stdin.readLine();
			
			if((prog = getProgrammeDescription(message)) == null) {
				System.out.println("Programme Good News Week not found in guide listings");
			} else {
				System.out.println("Programme synopsis for " + prog.getName());
				System.out.println(prog.getDescription());
			}
			break;
			
		case '3':
			System.out.println("Please enter start time:");
			System.out.flush(); // empties buffer, before you input text
			message = stdin.readLine();
			start = Integer.parseInt(message);
			
			System.out.println("Please enter end time:");
			System.out.flush(); // empties buffer, before you input text
			message = stdin.readLine();
			end = Integer.parseInt(message);
			
			if((message = getTodayListForPeriod(start,end)) == null) {
				System.out.println("No listings available");
			} else {
				System.out.println(message);
			}
			break;
			
		case 'E':
			shutdown();
			break;
			
		default:
			System.out.println("Invalid command");

		}

		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	public String viewLog(){
		return null;
	}
	
	public Programme getProgrammeDescription(String name){
		return null;
	}
	
	public String getTodayListForPeriod(int start, int end){
		return null;
	}
	
	public void shutdown(){
		System.exit(0);
	}
}
