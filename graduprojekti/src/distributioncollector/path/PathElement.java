package distributioncollector.path;

import java.util.HashMap;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import distributions.EmpiricalDistribution;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(value = EventElement.class, name = "EventElement"),

    @JsonSubTypes.Type(value = TransitionElement.class, name = "TransitionElement") }
)
public abstract class PathElement {
	private Map<Integer, Integer> durationInstances;
	private int totalDurationInstances;

	protected PathElement(int durationInstance) {
		this.durationInstances = new HashMap<>();
		this.durationInstances.put(durationInstance, 1);
		this.totalDurationInstances = 1;
	}
	
	public PathElement() {
	}

	public abstract boolean canMatch(String comparedElement);
	
	public void addDurationInstance(Integer durationInstance) {
		if(this.durationInstances.containsKey(durationInstance)) {
			this.durationInstances.put(durationInstance, this.durationInstances.get(durationInstance) + 1);
		} else {
			this.durationInstances.put(durationInstance, 1);
		}
		this.totalDurationInstances++;
	}
	
	public static PathElement create(String pathElement) {
		if(pathElement.charAt(0) == '(') return new TransitionElement(Integer.parseInt(pathElement.substring(1, pathElement.indexOf(')'))));
		String[] splitElement = pathElement.split("\\(");
		return new EventElement(splitElement[0], Integer.parseInt(splitElement[1].substring(0, splitElement[1].length() - 1)));
	}
	
	public EmpiricalDistribution generateDurationDistribution() {
		return new EmpiricalDistribution(this.durationInstances, this.totalDurationInstances);
	}

	public abstract String generateInstance(double minSDOfMean) throws Exception;

	
	public Map<Integer, Integer> getDurationInstances() {
		return durationInstances;
	}

	public void setDurationInstances(Map<Integer, Integer> durationInstances) {
		this.durationInstances = durationInstances;
	}

	public int getTotalDurationInstances() {
		return totalDurationInstances;
	}

	public void setTotalDurationInstances(int totalDurationInstances) {
		this.totalDurationInstances = totalDurationInstances;
	}
}
