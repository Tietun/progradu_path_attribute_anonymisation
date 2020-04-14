package datagenerator;

import distributions.Distribution;

public class EventNeed implements NeedBase{
	String name;
	Distribution eventTimeDistribution;
	
	public EventNeed(String name, Distribution eventTimeDistribution) {
		this.name = name;
		this.eventTimeDistribution = eventTimeDistribution;
	}
	
	public Integer getDuration() {
		return eventTimeDistribution.sample();
	}
	public String getName() {
		return this.name;
	}
	
	
}
