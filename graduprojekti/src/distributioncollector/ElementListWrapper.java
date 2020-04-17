package distributioncollector;

import java.util.List;
import distributioncollector.path.PathElement;

public class ElementListWrapper {
	private List<PathElement> elementList;

	public List<PathElement> getElementList() {
		return elementList;
	}

	public void setElementList(List<PathElement> elementList) {
		this.elementList = elementList;
	}
}
