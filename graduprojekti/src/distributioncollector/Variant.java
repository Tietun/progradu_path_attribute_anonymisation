package distributioncollector;

import java.util.ArrayList;
import java.util.List;

import distributioncollector.path.EventElement;
import distributioncollector.path.PathElement;
import distributions.EmpiricalDistribution;

/**
 * An object representing a possible path through a care pathway
 * @author Erkka Nurmi
 *
 */
public class Variant {
	private final List<PathElement> elements;

	/**
	 * Constructor
	 * @param pathElements String containing a durationless path attribute for the variant
	 */
	public Variant(String[] pathElements) {
		this.elements = new ArrayList<>();
		for (String pathElement : pathElements) {
			this.elements.add(PathElement.create(pathElement));
		}
	}

	/**
	 * Constructor
	 * @param wrapper Wrapper containing the PathElements that are assigned to this Variant
	 */
	public Variant(ElementListWrapper wrapper) {
		this.elements = wrapper.getElementList();
	}

	/**
	 * Sees if the given path attribute (with durations) can match this variant
	 * and if so adds the duration to the relevant distributions
	 * @param comparedPath Path attribute to compare
	 * @return Whether the compared path matched or not
	 */
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

	/**
	 * Sees if the given path attribute (with durations) matches this variant
	 * and if so generates a version of the path attribute with durations
	 * @param comparedPath Path attribute to compare
	 * @param laplaceEpsilon Epsilon for Laplace randomness
	 * @return Null if not a match; path attribute with durations if match
	 * @throws Exception Possible exception from distribution sampling
	 */
	public String matchAndGenerate(String[] comparedPath, double laplaceEpsilon) throws Exception {
		if (comparedPath.length == this.elements.size()) {
			for (int i = 0; i < comparedPath.length; i++) {
				if (!this.elements.get(i).canMatch(comparedPath[i]))
					return null;
			}
			StringBuilder generatedPath = new StringBuilder();
			for (PathElement pathElement : this.elements) {
				generatedPath.append(pathElement.generateInstance(laplaceEpsilon)).append(":");
			}
			return generatedPath.substring(0, generatedPath.length());
		}
		return null;
	}

	/**
	 * Sees if the given path attribute (without durations) matches this variant
	 * and if so generates a version of the path attribute with durations
	 * @param comparedPath Path attribute to compare (durationless)
	 * @param epsilon Epsilon for Laplace randomness
	 * @return Null if not a match; path attribute with durations if match
	 * @throws Exception Possible exception from distribution sampling
	 */
	public String matchTimelessAndGenerate(String[] comparedPath, double epsilon) throws Exception {
		int comparedIndex = 0;
		for(PathElement element : this.elements) {
			if(element instanceof EventElement) {
				String activity = ((EventElement)element).getActivity();
				if(!activity.equals(comparedPath[comparedIndex])) return null;
				comparedIndex++;
			}
		}
		StringBuilder generatedPath = new StringBuilder();
		for (PathElement pathElement : this.elements) {
			generatedPath.append(pathElement.generateInstance(epsilon)).append(":");
		}
		return generatedPath.substring(0, generatedPath.length() - 1);
	}

	/**
	 * Returns a path attribute matching this variant without durations
	 * @return A path attribute matching this variant without durations
	 */
	public String getTimelessPath() {
		StringBuilder timelessPath = new StringBuilder();
		for (PathElement element : this.elements) {
			if (element instanceof EventElement) {
				timelessPath.append(((EventElement) element).getActivity()).append(":");
			}
		}
		return timelessPath.substring(0, timelessPath.length() - 1);
	}

	/**
	 * Returns the path elements of this variant
	 * @return The path elements of this variant
	 */
	public ElementListWrapper getElementListWrapper() {
		ElementListWrapper wrapper = new ElementListWrapper();
		wrapper.setElementList(elements);
		return wrapper;
	}

	/**
	 * Returns the distribution wrapper of this variant
	 * @return The distribution wrapper of this variant
	 */
	public DistributionWrapper getDistributionWrapper() {
		DistributionWrapper wrapper = new DistributionWrapper();
		wrapper.setVariant(this.getTimelessPath());
		List<EmpiricalDistribution> distributions = new ArrayList<>();
		for(PathElement element : this.elements) {
			distributions.add(element.generateDurationDistribution());
		}
		wrapper.setDistributions(distributions);
		return wrapper;
	}

}
