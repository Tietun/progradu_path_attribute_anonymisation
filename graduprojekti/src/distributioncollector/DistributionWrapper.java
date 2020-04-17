package distributioncollector;

import java.util.List;

import distributions.EmpiricalDistribution;

public class DistributionWrapper {

	private String variant;
	private List<EmpiricalDistribution> distributions;
	
	public List<EmpiricalDistribution> getDistributions() {
		return distributions;
	}
	public void setDistributions(List<EmpiricalDistribution> distribution) {
		this.distributions = distribution;
	}
	public String getVariant() {
		return variant;
	}
	public void setVariant(String variant) {
		this.variant = variant;
	}
}
