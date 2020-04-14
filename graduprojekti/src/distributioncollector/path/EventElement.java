package distributioncollector.path;

public class EventElement extends PathElement{
	private String activity;

	public EventElement(String activity, int durationInstance) {
		super(durationInstance);
		this.activity = activity;
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

}
