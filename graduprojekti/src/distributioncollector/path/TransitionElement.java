package distributioncollector.path;

/**
 * 
 * @author Erkka Nurmi
 *
 * NOTE: Could instead instantiate superclass (make it non abstract), but used subclass for readability
 */
public class TransitionElement extends PathElement{

	public TransitionElement(int durationInstance) {
		super(durationInstance);
	}

	@Override
	public boolean canMatch(String comparedElement) {
		if(comparedElement.length() > 0 && comparedElement.charAt(0) == '(') return true;
		return false;
	}

}
