package distributioncollector.path;

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

	@Override
	public boolean canMatch(String comparedElement) {
		if(comparedElement.length() > 0) {
			if(comparedElement.charAt(0) != '(') {
				if(comparedElement.substring(0, comparedElement.indexOf('(')).equals(activity)) return true;
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
