package datagenerator;

import org.apache.commons.math3.distribution.ExponentialDistribution;

/**
 * Represents an activity / event that needs to take place during a care pathway
 * @author Erkka Nurmi
 *
 */
public class EventNeed implements NeedBase{
	private final String name;
	private final ExponentialDistribution eventTimeDistribution;
	
	/**
	 * Constructor
	 * @param name Name of the event / activity
	 * @param eventTimeDistribution Duration distribution of the event / activity
	 */
	public EventNeed(String name, ExponentialDistribution eventTimeDistribution) {
		this.name = name;
		this.eventTimeDistribution = eventTimeDistribution;
	}
	
	/**
	 * Returns a sampled duration for the event / activity
	 * @return Duration value sample
	 */
	public double getDuration() {
		return eventTimeDistribution.sample();
	}
	
	/**
	 * Getter for the event's / activity's name
	 * @return Name
	 */
	public String getName() {
		return this.name;
	}
}
