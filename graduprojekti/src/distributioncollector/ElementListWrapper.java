package distributioncollector;

import java.util.List;
import distributioncollector.path.PathElement;

/**
 * Wraps a list of path elements (Events and transitions)
 * Wrapper is used for serialization
 * @author Erkka Nurmi
 *
 */
public class ElementListWrapper {
	private List<PathElement> elementList;

	/**
	 * Getter for the elements
	 * @return Elements
	 */
	public List<PathElement> getElementList() {
		return elementList;
	}

	/**
	 * Setter for the elements
	 * @param elementList Elements
	 */
	public void setElementList(List<PathElement> elementList) {
		this.elementList = elementList;
	}
}
