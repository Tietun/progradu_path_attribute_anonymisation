package distributioncollector;

import java.util.ArrayList;
import java.util.List;

import distributioncollector.path.PathElement;

public class Variant {
	List<PathElement> elements;
	
	public Variant(String[] pathElements) {
		this.elements = new ArrayList<>();
		for(String pathElement : pathElements) {
			this.elements.add(PathElement.create(pathElement));
		}
	}

	public boolean matchPathAndAdd(String[] comparedPath) {
		if(comparedPath.length == this.elements.size()) {
			for(int i = 0; i < comparedPath.length; i++) {
				if(!this.elements.get(i).canMatch(comparedPath[i])) return false;
			}
			for(int i = 0; i < comparedPath.length; i++) {
				this.elements.get(i).addDurationInstance(
						Integer.parseInt(comparedPath[i].substring(
								comparedPath[i].indexOf('(') + 1,
								comparedPath[i].indexOf(')'))));
			}
			return true;
		}
		return false;
	}

	public String matchAndGenerate(String[] comparedPath, double minSDOfMean) {
		if(comparedPath.length == this.elements.size()) {
			for(int i = 0; i < comparedPath.length; i++) {
				if(!this.elements.get(i).canMatch(comparedPath[i])) return null;
			}
			String generatedPath = "";
			for(PathElement pathElement : this.elements) {
				generatedPath = generatedPath + pathElement.generateInstance(minSDOfMean) + ":";
			}
			return generatedPath.substring(0, generatedPath.length());
		}
		return null;
	}
}
