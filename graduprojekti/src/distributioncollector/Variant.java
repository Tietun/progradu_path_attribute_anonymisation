package distributioncollector;

import java.util.ArrayList;
import java.util.List;

import distributioncollector.path.EventElement;
import distributioncollector.path.PathElement;

public class Variant {
	List<PathElement> elements;

	public Variant(String[] pathElements) {
		this.elements = new ArrayList<>();
		for (String pathElement : pathElements) {
			this.elements.add(PathElement.create(pathElement));
		}
	}

	public Variant(ElementListWrapper wrapper) {
		this.elements = wrapper.getElementList();
	}

	public boolean matchPathAndAdd(String[] comparedPath) {
		if (comparedPath.length == this.elements.size()) {
			for (int i = 0; i < comparedPath.length; i++) {
				if (!this.elements.get(i).canMatch(comparedPath[i]))
					return false;
			}
			for (int i = 0; i < comparedPath.length; i++) {
				this.elements.get(i).addDurationInstance(Integer.parseInt(
						comparedPath[i].substring(comparedPath[i].indexOf('(') + 1, comparedPath[i].indexOf(')'))));
			}
			return true;
		}
		return false;
	}

	public String matchAndGenerate(String[] comparedPath, double minSDOfMean) throws Exception {
		if (comparedPath.length == this.elements.size()) {
			for (int i = 0; i < comparedPath.length; i++) {
				if (!this.elements.get(i).canMatch(comparedPath[i]))
					return null;
			}
			String generatedPath = "";
			for (PathElement pathElement : this.elements) {
				generatedPath = generatedPath + pathElement.generateInstance(minSDOfMean) + ":";
			}
			return generatedPath.substring(0, generatedPath.length());
		}
		return null;
	}

	public String matchTimelessAndGenerate(String[] comparedPath, double minSDOfMean) throws Exception {
		int comparedIndex = 0;
		for(PathElement element : this.elements) {
			if(element instanceof EventElement) {
				String activity = ((EventElement)element).getActivity();
				if(!activity.equals(comparedPath[comparedIndex])) return null;
				comparedIndex++;
			}
		}
		String generatedPath = "";
		for (PathElement pathElement : this.elements) {
			generatedPath = generatedPath + pathElement.generateInstance(minSDOfMean) + ":";
		}
		return generatedPath.substring(0, generatedPath.length() - 1);
	}

	public String getTimelessPath() {
		String timelessPath = "";
		for (PathElement element : this.elements) {
			if (element instanceof EventElement) {
				timelessPath = timelessPath + ((EventElement) element).getActivity() + ":";
			}
		}
		return timelessPath.substring(0, timelessPath.length());
	}

	public ElementListWrapper getElementListWrapper() {
		ElementListWrapper wrapper = new ElementListWrapper();
		wrapper.setElementList(elements);
		return wrapper;
	}

}
