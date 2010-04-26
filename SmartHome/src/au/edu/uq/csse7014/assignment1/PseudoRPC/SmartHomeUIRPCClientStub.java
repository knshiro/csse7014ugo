package au.edu.uq.csse7014.assignment1.PseudoRPC;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.avis.client.Elvin;
import org.avis.client.Notification;
import org.avis.client.NotificationEvent;
import org.avis.client.NotificationListener;
import org.avis.client.Subscription;
import org.avis.common.InvalidURIException;

public class SmartHomeUIRPCClientStub implements NotificationListener{
	private Object result = null;
	private boolean done;
	private boolean timeout = false;
	private String applicationId;
	private String id;
	private Elvin elvin;
	
	public SmartHomeUIRPCClientStub(String server, String applicationId, String id){
		this.applicationId = applicationId;
		this.id = id + "CLIENT";
		Subscription sub;
		try {
			elvin = new Elvin(server);
			System.out.println("connected");
			
			sub = elvin.subscribe("APPLICATIONID == '"+ applicationId +"' && DESTID == '"+ id +"'");
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
	
	
	public void trackingNotify(){
	}
	
	public Object processCall(String callName,String destId, List<Object> args){
		while(done){
		}
		
		Notification call = new Notification();
		call.set("APPLICATIONID", applicationId);
		call.set("FROMID", id);  
		call.set("DESTID", destId);  //TODO ugly hard coded
		call.set("QUERY", callName);
		for(int i=0; i<args.size(); i++){
			call.set("ARG"+i, args.get(i));
		}
		try {
			elvin.send(call);
		} catch (IOException e) {
			// TODO Auto-generated catch blockHomeManagerRPCClientStub
			e.printStackTrace();
		}
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				//
			}
		};
		/*while(!done && !timeout){ //TODO pas beau
		}
		
		if(timeout){
			done = false;
			//errorvoid
		}*/
		try {
			result.wait(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//A essayer
		done = false;
		return result;
	}
	
	
	public String getProgrammeDescription(String name){
		String returnValue;
		ArrayList<Object> args = new ArrayList<Object>();
		
		args.add(name);
		
		processCall("ProgDesc","HOMEMANAGER1SERVER", args);
		returnValue = new String((String)this.result);
		return returnValue;
		
	}
	
	public Object getProgrammeForPeriod(int start, int end){
		String returnValue;
		ArrayList<Object> args = new ArrayList<Object>();
		
		args.add(start);
		args.add(end);
		
		processCall("ProgPeriod","HOMEMANAGER1SERVER", args);
		returnValue = new String((String)this.result); //TODO changer
		return returnValue;
	}

	@Override
	public void notificationReceived(NotificationEvent answer) {
		result = answer.notification.get("RESULT");
		result.notifyAll();
	}
	
}