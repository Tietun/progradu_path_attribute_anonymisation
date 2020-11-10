package datagenerator;

import org.apache.commons.math3.distribution.ExponentialDistribution;

/**
 * Represents a transition or wait time that need to take place during a care pathway
 * @author Erkka Nurmi
 *
 */
public class WaitNeed implements NeedBase{

	private ExponentialDistribution waitDistribution;

	/**
	 * Constructor
	 * @param waitDistribution Duration distribution for the wait time
	 */
	public WaitNeed(ExponentialDistribution waitDistribution) {
		this.waitDistribution = waitDistribution;
	}

	/**
	 * Samples the duration distribution
	 * @return A value sampled from the duration distribution
	 */
	public double getDuration() {
		return this.waitDistribution.sample();
	}
}
