package au.edu.uq.csse7014.assignment1.components.sensors;

import java.io.FileReader;
import java.io.IOException;

public class TempSensor extends GenericSensor {

	
	private int tempMin=0,tempMax=0;
	
	@Override
	public String sense() {
		String readValue = "";
		try {
			if( (readValue = br.readLine()) == null){
				fr.close();
				fr = new FileReader(filename);
				readValue = br.readLine();
			}
			if(tempMax-tempMin>0 && (Integer.parseInt(readValue)<=tempMax || Integer.parseInt(readValue)>=tempMin) ){
				readValue = "";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return readValue;
	}
	
	
}