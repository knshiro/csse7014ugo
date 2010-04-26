package au.edu.uq.csse7014.assignment1.PseudoRPC;

import java.io.IOException;
import java.net.ConnectException;

import org.avis.client.Elvin;
import org.avis.client.InvalidSubscriptionException;
import org.avis.client.Notification;
import org.avis.client.NotificationEvent;
import org.avis.client.NotificationListener;
import org.avis.client.Subscription;
import org.avis.common.InvalidURIException;

import au.edu.uq.csse7014.assignment1.components.HomeManager;

public class HomeManagerRPCServerStub implements NotificationListener{
	
	private static enum Queries {ProgDesc,ProgPeriod,Temperature,Tracking,Energy,Clock,ViewLog,Shutdown};
	private Elvin elvin;
	private String id;
	private String applicationId;
	private HomeManager controller;
	
	public HomeManagerRPCServerStub(HomeManager controller, String server, String applicationId, String id){
		
		this.controller = controller;
		this.id = id + "SERVER";
		this.applicationId = applicationId;
		try {
			elvin = new Elvin(server);
			System.out.println("connected");
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
	
	public void listen(){

			Subscription sub;
			try {
				sub = elvin.subscribe("APPLICATIONID == '"+ applicationId +"' && DESTID == '"+ id +"'");
				sub.addListener(this);
			} catch (InvalidSubscriptionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO elvin.send(not); //send thAuto-generated catch block
				e.printStackTrace();
			} 
	
	}

	@Override
	public void notificationReceived(NotificationEvent call) {
		Object result = null;
		Notification answer;
		String stringValue;
		int intValue1, intValue2;
		   
		String fromId = call.notification.getString("FROMID");    
		String query = (String) call.notification.get("QUERY");
		
		switch (Queries.valueOf( query )) {
		case ProgDesc:
			stringValue = call.notification.getString("ARG1");
			result = controller.getProgrammeDescription(stringValue);
			break;
		case ProgPeriod:
			stringValue = call.notification.getString("ARG1");
			intValue1 = call.notification.getInt("ARG2");
			intValue2 = call.notification.getInt("ARG3");
			result = controller.getProgrammeForPeriod(stringValue, intValue1, intValue2);
			break;
		case Clock:
			stringValue = call.notification.getString("VALUE");
			controller.handleClock(stringValue);
			break;
		case Tracking:
			stringValue = call.notification.getString("VALUE");
			controller.handleTracking(stringValue.compareTo("home") == 0);
			break;
		case Temperature:
			stringValue = call.notification.getString("VALUE");
			int temp = Integer.parseInt(stringValue);
			controller.handleTemp(temp);
			break;
		case Energy:
			stringValue = call.notification.getString("VALUE");
			int consumption = Integer.parseInt(stringValue);
			controller.handleEnergyUsage(consumption);
			break;
		case ViewLog:
			result = controller.viewLog();
			break;
		case Shutdown:
			elvin.close();
			controller.shutdown();
		default:
			break;
		}
		
		if(result != null)
		try {
			

			answer = new Notification();
			answer.set("APPLICATIONID", applicationId);
			answer.set("FROMID", id);  
			answer.set("DESTID", fromId);
			answer.set("RESULT", result);
	
			
			elvin.send(answer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
			
	}
	


}
