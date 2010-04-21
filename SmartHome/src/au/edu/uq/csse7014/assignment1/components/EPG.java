package au.edu.uq.csse7014.assignment1.components;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.avis.client.Elvin;
import org.avis.client.NotificationEvent;
import org.avis.client.NotificationListener;

import au.edu.uq.csse7014.assignment1.model.Programme;

public class EPG implements NotificationListener{

	public enum Keys {name,day,time,description};
	
	private ArrayList<Programme> listProg;
	
	private Elvin elvin;
	
	EPG(){
		listProg = new ArrayList<Programme>();
	}
	
	
	public void loadDataFile(String filename){

		FileReader fr;
		BufferedReader br;
		String line;
		Programme prog;
		String keyValue[];
		
		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
			
			while((line = br.readLine())!=null){
				prog = new Programme();
				while(line != ""){
					keyValue = line.split(":");
					switch( Keys.valueOf( keyValue[0] ) ){
					case name:
						prog.setName(keyValue[1]);
						break;
					case day:
						prog.setDay(keyValue[1]);
						break;
					case time:
						prog.setHour(Integer.parseInt(keyValue[1]));
						break;
					case description:
						prog.setDescription(keyValue[1]);
						break;
					default:
						;
					}
					line = br.readLine();
				}
				listProg.add(prog);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public String getProgrammeDescription(String name){
		
		String description = null;
		int i = 0;
		
		while(i<listProg.size() && !listProg.get(i).getName().equals(name)){
			i++;
		}
		if(i<listProg.size()){
			description = listProg.get(i).getDescription();
		}
		
		return description;
	}
	
	public List<Programme> getProgrammeForPeriod(String day,int start, int end){
		Programme prog;
		ArrayList<Programme> programmes = new ArrayList<Programme>();
		
		for(int i=0; i<listProg.size();i++){
			prog = listProg.get(i);
			if(prog.getDay().equals(day) && prog.getHour()>=start && prog.getHour()<=end){
				programmes.add(prog);
			}
		}
		return programmes;
	}


	@Override
	public void notificationReceived(NotificationEvent event) {
		if(event.notification.get("NAME").equals("shutdown")) {
			elvin.close();
			System.exit(0);
		}	
	}
}
