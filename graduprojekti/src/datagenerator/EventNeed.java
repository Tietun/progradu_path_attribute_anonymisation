package datagenerator;

import org.apache.commons.math3.distribution.ExponentialDistribution;

public class EventNeed implements NeedBase{
	String name;
	ExponentialDistribution eventTimeDistribution;
	
	public EventNeed(String name, ExponentialDistribution eventTimeDistribution) {
		this.name = name;
		this.eventTimeDistribution = eventTimeDistribution;
	}
	
	public double getDuration() {
		double sample = eventTimeDistribution.sample();
		return sample;
	}
	public String getName() {
		return this.name;
	}
	
	
}
