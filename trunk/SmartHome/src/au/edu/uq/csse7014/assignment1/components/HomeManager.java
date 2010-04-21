package au.edu.uq.csse7014.assignment1.components;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.avis.client.NotificationEvent;
import org.avis.client.NotificationListener;

public class HomeManager implements NotificationListener{

	private boolean inhabited;
	private boolean aircon;
	private int lastConsumption;
	private String logFile;
	private String username;
	private String currentDay;
	private PrintStream p;
	
	
	public HomeManager(String logFile){
		
		this.logFile = logFile;
		FileOutputStream out; // declare a file output object
		// declare a print stream object

		// Create a new file output stream
		// connected to "myfile.txt"
		try {
			out = new FileOutputStream(logFile);

			// Connect print stream to the output stream
			p = new PrintStream( out );
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void logTemp(int temp){
		p.println ("This is written to a file");
		p.println(currentDay + ": Air-conditioning adjusted.");
		p.println("Temperature: at " + temp + "degrees");
		if(inhabited){
			p.println("At Home: " + username);
		} else {
			p.println("At Home: nobody");
		}
	}
	
	
	public void handleEnergyUsage(int consumption){
		if(lastConsumption != consumption && consumption> 4){
			//TODO send warning
		}
	}
	
	public void handleTemp(int temp){
		TempHandlerThread thread = new TempHandlerThread();
		thread.init(temp, this);
		thread.run();
	}
	
	public class TempHandlerThread extends Thread{
		
		HomeManager manager;
		int temp;
		
		public void init(int temp, HomeManager manager){
			this.temp = temp;
			this.manager = manager;
		}
		
		public void run(){
			try {
				if( manager.inhabited ){
					if (temp != 22){
						manager.aircon = true;
						Thread.sleep(5000);
						manager.aircon = false;
					}
				} else {
					manager.aircon = true;
					Thread.sleep(5000);
					manager.aircon = false;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public String viewLog(){
		 FileReader fr;
		 BufferedReader br;
		 StringBuilder builder = new StringBuilder();
		 String line;
		 
		try {
			fr = new FileReader(logFile);
			br = new BufferedReader(fr);
			
			while ( (line=br.readLine()) != null ) { 
				builder.append(line);
			}
			fr.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.toString();
	}
	
	

	@Override
	public void notificationReceived(NotificationEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
