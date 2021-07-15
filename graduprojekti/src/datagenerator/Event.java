package datagenerator;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Represents a single event that has happened to a single customer
 * @author Erkka Nurmi
 *
 */
public class Event {
	private final String activity;
	private final LocalDateTime startTime;
	private final LocalDateTime endTime;
	
	/**
	 * Constructor
	 * @param name Name of the activity
	 * @param startTime When did the event start
	 * @param endTime When did the event end
	 */
	public Event(String name, LocalDateTime startTime, LocalDateTime endTime) {
		this.activity = name;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/**
	 * Returns the event's data in a csv friendly format
	 * @return The event's data in a csv friendly format
	 */
	public String getValueLine() {
		return this.activity + ";" + this.startTime.toString().replace('T', ' ') + ":00;"  + this.endTime.toString().replace('T', ' ') +":00";
	}

	/**
	 * Returns a path attribute friendly summary of the event
	 * @return A path attribute friendly summary of the event
	 */
	public String getSummary() {
		return this.activity + "(" + (Duration.between(this.startTime, this.endTime).getSeconds() / 60) + ")";
	}

	/**
	 * Getter for the end time
	 * @return End time
	 */
	public LocalDateTime getEndTime() {
		return this.endTime;
	}

	/**
	 * Getter for the start time
	 * @return Start time
	 */
	public LocalDateTime getStartTime() {
		return this.startTime;
	}

	/**
	 * Getter for the activity
	 * @return Activity
	 */
	public String getActivity() {
		return this.activity;
	}

}
