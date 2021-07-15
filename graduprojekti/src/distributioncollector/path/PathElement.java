package distributioncollector.path;

import java.util.HashMap;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import distributions.EmpiricalDistribution;
/**
 * Abstract class representing an event or a transition in a Customers care path
 * @author Erkka Nurmi
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EventElement.class, name = "EventElement"),
    @JsonSubTypes.Type(value = TransitionElement.class, name = "TransitionElement") }
)
public abstract class PathElement {
	private Map<Integer, Integer> durationInstances;
	private int totalDurationInstances;

	/**
	 * Constructor
	 * @param durationInstance length of the event
	 */
	protected PathElement(int durationInstance) {
		this.durationInstances = new HashMap<>();
		this.durationInstances.put(durationInstance, 1);
		this.totalDurationInstances = 1;
	}
	
	public PathElement() {
	}

	/**
	 * Can the PathElement match the given part of a path attribute
	 * @param comparedElement Path attribute part to compare
	 * @return If can match
	 */
	public abstract boolean canMatch(String comparedElement);
	
	public void addDurationInstance(Integer durationInstance) {
		if(this.durationInstances.containsKey(durationInstance)) {
			this.durationInstances.put(durationInstance, this.durationInstances.get(durationInstance) + 1);
		} else {
			this.durationInstances.put(durationInstance, 1);
		}
		this.totalDurationInstances++;
	}
	
	/**
	 * Creates a path element based on the given part of a path attribute
	 * @param pathElement Part of a path attribute
	 * @return The created PathElement
	 */
	public static PathElement create(String pathElement) {
		if(pathElement.charAt(0) == '(') return new TransitionElement(Integer.parseInt(pathElement.substring(1, pathElement.indexOf(')'))));
		String[] splitElement = pathElement.split("\\(");
		return new EventElement(splitElement[0], Integer.parseInt(splitElement[1].substring(0, splitElement[1].length() - 1)));
	}
	
	/**
	 * Generates a distribution based on found instances
	 * @return An empirical distribution based on the instances
	 */
	public EmpiricalDistribution generateDurationDistribution() {
		return new EmpiricalDistribution(this.durationInstances, this.totalDurationInstances);
	}

	/**
	 * Generates a Customer specific instance of the path element
	 * @param laplaceEpsilon Epsilon for Laplace randomness
	 * @return Path attribute friendly String of the instance
	 * @throws Exception Possible exception from distribution sampling
	 */
	public abstract String generateInstance(double laplaceEpsilon) throws Exception;

	/**
	 * Gets duration instances
	 * @return Map of duration instances: Key = duration, value = number of instances
	 */
	public Map<Integer, Integer> getDurationInstances() {
		return durationInstances;
	}

	/**
	 * Sets duration instances
	 * @param durationInstances Map of duration instances: Key = duration, value = number of instances
	 */
	public void setDurationInstances(Map<Integer, Integer> durationInstances) {
		this.durationInstances = durationInstances;
	}

	/**
	 * Get total number of duration instances
	 * @return Total number of duration instances
	 */
	public int getTotalDurationInstances() {
		return totalDurationInstances;
	}
}
