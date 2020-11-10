package distributioncollector;

import java.util.List;

import distributions.EmpiricalDistribution;

/**
 * Wrapper for a variant and a list of relevant distributions
 * Wrapper is used for serialization
 * @author Erkka Nurmi
 *
 */
public class DistributionWrapper {

	private String variant;
	private List<EmpiricalDistribution> distributions;
	
	/**
	 * Gets distributions
	 * @return Distributions
	 */
	public List<EmpiricalDistribution> getDistributions() {
		return distributions;
	}
	
	/**
	 * Sets distributions
	 * @param distribution The distributions to be set
	 */
	public void setDistributions(List<EmpiricalDistribution> distribution) {
		this.distributions = distribution;
	}
	
	/**
	 * Gets variant
	 * @return Variant
	 */
	public String getVariant() {
		return variant;
	}
	
	/**
	 * Sets variant
	 * @param variant The variant to be set
	 */
	public void setVariant(String variant) {
		this.variant = variant;
	}
}
