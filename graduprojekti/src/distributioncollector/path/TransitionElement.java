package distributioncollector.path;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Erkka Nurmi
 * Object representing a transition in a customers care path
 * NOTE: Could instead instantiate superclass (make it non-abstract), but used subclass for readability
 */
public class TransitionElement extends PathElement{

	/**
	 * Constructor
	 * @param durationInstance How long did the transition take
	 */
	public TransitionElement(int durationInstance) {
		super(durationInstance);
	}
	
	/**
	 * Default constructor
	 */
	public TransitionElement() {
		super();
	}

    public static PathElement compoundElementFrom(List<PathElement> elements) {
		TransitionElement transitionElement = new TransitionElement();
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
		transitionElement.setDurationInstances(durationInstances);
		return transitionElement;
    }

    @Override
	public boolean canMatch(String comparedElement) {
		return comparedElement.length() > 0 && comparedElement.charAt(0) == '(';
	}

	@Override
	public String generateInstance(double laplaceEpsilon) throws Exception {
		return "(" + this.generateDurationDistribution().sampleWithLaplaceRandomness(laplaceEpsilon) + ")";
	}

}
