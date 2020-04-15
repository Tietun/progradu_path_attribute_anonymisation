package distributioncollector.path;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import distributions.EmpiricalDistribution;

public abstract class PathElement {
	private Map<Integer, Integer> durationInstances;
	private int totalDurationInstances;
	
	protected PathElement(int durationInstance) {
		this.durationInstances = new HashMap<>();
		this.durationInstances.put(durationInstance, 1);
		this.totalDurationInstances = 1;
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
	
	public EmpiricalDistribution getDurationDistribution() {
		return new EmpiricalDistribution(this.durationInstances, this.totalDurationInstances);
	}

	public abstract String generateInstance(double minSDOfMean);

}
