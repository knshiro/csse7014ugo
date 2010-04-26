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

	private String id;
	private String applicationId;
	private ISensor sensor;
	private LoopThread loop;
	private Elvin elvin;
	private boolean active;
	
	Sensor(String type, String filename, String server, String applicationId, String id){
		
		this.applicationId  = applicationId;
		this.id = id;
		
		Subscription sub;
		if(type == "temperature"){
			sensor = new TempSensor();
		} else {
			sensor = new GenericSensor();
		}
		sensor.loadDataFile(filename);
		
		try {
			elvin = new Elvin(server);
			sub = elvin.subscribe("APPLICATIONID == '"+ applicationId +"' && DESTID='"+ id + "'");
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
		active = true;
		loop = new LoopThread();
		loop.setSensor(sensor);
		loop.start();
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
			    not.set("APPLICATIONID", applicationId);
			    not.set("FROMID", id);
			    not.set("DESTID","HomeManager"); //TODO really ugly hard coded id
			    not.set("QUERY", id);
			    not.set("VALUE", reading);
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
			active=false;
		}
		
	
	}
	
	public void main(String [] args){
		String applicationid = "42207313";
		String id = args[0];  //TODO Put an unique value
		Sensor sensor = new Sensor(args[0],args[1],args[2],applicationid,id);
		sensor.startLoop();
		while(sensor.active){
		}
	}

	@Override
	public void notificationReceived(NotificationEvent event) {
		if(event.notification.get("NAME").equals("shutdown")) {
			elvin.close();
			loop.stopLoop();
		}
	}
	
	
}
