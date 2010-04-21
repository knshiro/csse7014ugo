package au.edu.uq.csse7014.assignment1.model;

public class Programme {

	private int hour;
	private String day,name,description;
	
	public Programme(){
		this.hour = -1;
		this.day = "";
		this.name = "";
		this.description = "";
	}
	
	public Programme(int hour, String day, String name, String description) {
		this.hour = hour;
		this.day = day;
		this.name = name;
		this.description = description;
	}
	
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
