package distributioncollector.path;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Path element representing an event in a care path of a Customer
 * @author Erkka Nurmi
 *
 */
public class EventElement extends PathElement{
	private String activity;

	/**
	 * Constructor
	 * @param activity Name of the activity
	 * @param durationInstance Length of the event
	 */
	public EventElement(String activity, int durationInstance) {
		super(durationInstance);
		this.activity = activity;
	}
	
	/**
	 * Default constructor
	 */
	public EventElement() {
		super();
	}

	private EventElement(String activity) {
		this.activity = activity;
	}

	public static PathElement compoundElementFrom(List<PathElement> elements, String activity) {
		EventElement eventElement = new EventElement(activity);
		Map<Integer, Integer> durationInstances = new HashMap<>();
		for(PathElement element : elements){
			for(Integer durationInstanceToAdd : element.getDurationInstances().keySet()){
				if(durationInstances.containsKey(durationInstanceToAdd)){
					durationInstances.put(
							durationInstanceToAdd,
							durationInstances.get(durationInstanceToAdd) + element.getDurationInstances().get(durationInstanceToAdd)
					);
				} else {
					durationInstances.put(
							durationInstanceToAdd,
							element.getDurationInstances().get(durationInstanceToAdd)
					);
				}
			}
		}
		eventElement.setDurationInstances(durationInstances);
		return eventElement;
    }

    @Override
	public boolean canMatch(String comparedElement) {
		if(comparedElement.length() > 0) {
			if(comparedElement.charAt(0) != '(') {
				return comparedElement.substring(0, comparedElement.indexOf('(')).equals(activity);
			}
		}
		return false;
	}

	@Override
	public String generateInstance(double laplaceEpsilon) throws Exception {
		return this.activity + "(" + this.generateDurationDistribution().sampleWithLaplaceRandomness(laplaceEpsilon) + ")";
	}

	/**
	 * Gets activity
	 * @return Activity
	 */
	public String getActivity() {
		return this.activity;
	}

}
