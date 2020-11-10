package distributioncollector.path;

/**
 * 
 * @author Erkka Nurmi
 * Object representing a transition in a customers care path
 * NOTE: Could instead instantiate superclass (make it non abstract), but used subclass for readability
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

	@Override
	public boolean canMatch(String comparedElement) {
		if(comparedElement.length() > 0 && comparedElement.charAt(0) == '(') return true;
		return false;
	}

	@Override
	public String generateInstance(double laplaceEpsilon) throws Exception {
		return "(" + this.generateDurationDistribution().sampleWithLaplaceRandomness(laplaceEpsilon) + ")";
	}

}
