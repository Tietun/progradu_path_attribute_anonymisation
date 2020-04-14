package datagenerator;

import distributions.Distribution;

public class WaitNeed implements NeedBase{

	private Distribution waitDistribution;

	public WaitNeed(Distribution waitDistribution) {
		this.waitDistribution = waitDistribution;
	}

	public Integer getDuration() {
		return this.waitDistribution.sample();
	}

}
