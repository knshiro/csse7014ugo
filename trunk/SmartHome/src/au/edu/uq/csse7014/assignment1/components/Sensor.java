package au.edu.uq.csse7014.assignment1.components;

import java.io.IOException;
import java.net.ConnectException;

import org.avis.client.Elvin;
import org.avis.client.Notification;
import org.avis.client.NotificationEvent;
import org.avis.client.NotificationListener;
import org.avis.client.Subscription;
import org.avis.common.InvalidURIException;

import au.edu.uq.csse7014.assignment1.components.sensors.GenericSensor;
import au.edu.uq.csse7014.assignment1.components.sensors.TempSensor;

public class Sensor implements NotificationListener {

	private ISensor sensor;
	private LoopThread loop;
	private Elvin elvin;
	
	Sensor(String type, String filename, String server){
		Subscription sub;
		if(type == "temperature"){
			sensor = new TempSensor();
		} else {
			sensor = new GenericSensor();
		}
		sensor.loadDataFile(filename);
		
		try {
			elvin = new Elvin(server);
			sub = elvin.subscribe("NAME == 'test'");
			sub.addListener(this);
			
			
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidURIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void startLoop(){
		
		loop = new LoopThread();
		loop.setSensor(sensor);
		loop.run();
	}
	
	class LoopThread extends Thread {
		
		private volatile Thread loop;
		ISensor sensor;
		public void setSensor(ISensor sensor){
			this.sensor = sensor;
		}
		
		public void start(){
			loop = new Thread(this);
			loop.start();
		}
		
		public void run(){
			Notification not;
			String reading;
			Thread thisThread = Thread.currentThread();
			while(loop == thisThread){
				not = new Notification();
				reading = sensor.sense();
			    not.set("NAME", reading);     
			    try {
					elvin.send(not);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} //send the notification
			}
		}
		
		public void stopLoop(){
			Thread temp = loop ;
			loop = null;
			temp.interrupt();
			sensor.shutDown();
		}
		
	
	}
	
	public void main(String [] args){
		
	}

	@Override
	public void notificationReceived(NotificationEvent event) {
		if(event.notification.get("NAME").equals("shutdown")) {
			loop.stopLoop();
			elvin.close();
			System.exit(0);
		}
	}
	
	
}
