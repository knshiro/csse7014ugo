package au.edu.uq.csse7014.assignment1.components.sensors;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import au.edu.uq.csse7014.assignment1.components.ISensor;

public class GenericSensor implements ISensor{

	protected String filename = null;
	protected FileReader fr;
	protected BufferedReader br;
	protected String currentValue;
	protected Date endTime;
	
	@Override
	public void loadDataFile(String filename) {
		this.filename = filename;
		
		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	@Override
	public String sense() {
		String readValue = "";
		String values[];

		// create a calendar instance
		Calendar calendar = Calendar.getInstance();

		// get a java.util.Date from the Calendar instance.
		// this date will represent the current instant, or "now".
		Date now = calendar.getTime();

		if(now.getTime() > endTime.getTime()){

			try {
				if( (readValue = br.readLine()) == null){
					fr.close();
					fr = new FileReader(filename);
					readValue = br.readLine();
				}
				values = readValue.split(",");
				currentValue = values[0];
				endTime.setTime(now.getTime()+(Integer.parseInt(values[1])*1000));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return currentValue;
	}

	@Override
	public void shutDown() {
		try {
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
