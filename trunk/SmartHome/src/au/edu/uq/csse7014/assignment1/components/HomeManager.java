package au.edu.uq.csse7014.assignment1.components;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.avis.client.NotificationEvent;
import org.avis.client.NotificationListener;

import au.edu.uq.csse7014.assignment1.PseudoRPC.HomeManagerRPCClientStub;
import au.edu.uq.csse7014.assignment1.PseudoRPC.HomeManagerRPCServerStub;

public class HomeManager implements NotificationListener{

	private boolean inhabited;
	private boolean aircon;
	private int lastConsumption;
	private String logFile;
	private String username;
	private String currentDay;
	private PrintStream p;
	private HomeManagerRPCServerStub serverStub;
	private HomeManagerRPCClientStub clientStub;
	
	
	public HomeManager(String logFile, String server, String applicationId, String id){
		
		serverStub = new HomeManagerRPCServerStub(this, server, applicationId, id);
		
		this.logFile = logFile;
		FileOutputStream out; // declare a file output object
		// declare a print stream object

		// Create a new file output stream
		// connected to logFile
		try {
			out = new FileOutputStream(logFile);

			// Connect print stream to the output stream
			p = new PrintStream( out );
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void logTemp(int temp)
	{
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
	
	
	public void handleTracking(boolean inhabited){
		if ( inhabited != this.inhabited){
			this.inhabited = inhabited;
		}
	}
	
	public void handleClock(String day){
		this.currentDay = day;
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
	
	public void shutdown(){
		
		System.exit(0);
	}
	@Override
	public void notificationReceived(NotificationEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public Object getProgrammeDescription(String name) {
		// TODO Auto-generated method stub
		return null;
	}


	public Object getProgrammeForPeriod(String stringValue, int intValue1,
			int intValue2) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
