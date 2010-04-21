package au.edu.uq.csse7014.assignment1.components;

public interface ISensor {
	
	public String sense();
	public void loadDataFile(String filename);
	public void shutDown();
	
}
