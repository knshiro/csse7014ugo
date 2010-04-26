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

import au.edu.uq.csse7014.assignment1.components.EPG;


public class EPGPseudoRPCServerStub implements NotificationListener{

	private static enum Queries {ProgDesc,ProgPeriod,Shutdown};
	private Elvin elvin;
	private String id;
	private String applicationId;
	private EPG controller;
	
	public EPGPseudoRPCServerStub(EPG controller, String server, String applicationId, String id){
		
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
				controller.setActive(true);
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
		   
	    
		String query = (String) call.notification.get("QUERY");
		
		switch (Queries.valueOf( query )) {
		case ProgDesc:
			String arg1 = call.notification.getString("ARG1");
			result = controller.getProgrammeDescription(arg1);
			break;
		case ProgPeriod:
			arg1 = call.notification.getString("ARG1");
			int arg2 = call.notification.getInt("ARG2");
			int arg3 = call.notification.getInt("ARG3");
			result = controller.getProgrammeForPeriod(arg1, arg2, arg3);
			break;
		case Shutdown:
			elvin.close();
			controller.setActive(false);
			System.exit(0);
		default:
			break;
		}
		
		if (result != null) {
			try {
				String fromId = call.notification.getString("FROMID");

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
	
}
