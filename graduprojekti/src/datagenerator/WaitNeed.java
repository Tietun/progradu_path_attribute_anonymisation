package datagenerator;

import org.apache.commons.math3.distribution.ExponentialDistribution;

public class WaitNeed implements NeedBase{

	private ExponentialDistribution waitDistribution;

	public WaitNeed(ExponentialDistribution waitDistribution) {
		this.waitDistribution = waitDistribution;
	}

	public double getDuration() {
		return this.waitDistribution.sample();
	}

}
