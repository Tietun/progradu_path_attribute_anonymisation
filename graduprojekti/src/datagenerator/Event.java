package datagenerator;

import java.time.Duration;
import java.time.LocalDateTime;

public class Event {
	String activity;
	LocalDateTime startTime;
	LocalDateTime endTime;
	
	

	public Event(String name, LocalDateTime startTime, LocalDateTime endTime) {
		this.activity = name;
		this.startTime = startTime;
		this.endTime = endTime;
	}



	public String getValueLine() {
		return this.activity + ";" + this.startTime.toString().replace('T', ' ') + ":00;"  + this.endTime.toString().replace('T', ' ') +":00";
	}



	public String getSummary() {
		return this.activity + "(" + (Duration.between(this.startTime, this.endTime).getSeconds()) + ")";
	}



	public LocalDateTime getEndTime() {
		return this.endTime;
	}



	public LocalDateTime getStartTime() {
		return this.startTime;
	}



	public String getActivity() {
		return this.activity;
	}

}
