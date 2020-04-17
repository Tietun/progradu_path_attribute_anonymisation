package distributioncollector.path;

public class EventElement extends PathElement{
	private String activity;

	public EventElement(String activity, int durationInstance) {
		super(durationInstance);
		this.activity = activity;
	}
	
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
	public String generateInstance(double minSDOfMean) throws Exception {
		return this.activity + "(" + this.generateDurationDistribution().sampleWithLaplaceRandomness(minSDOfMean) + ")";
	}

	public String getActivity() {
		return this.activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}

}
