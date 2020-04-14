package distributions;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class EmpiricalDistribution implements Distribution{
	private static Random r = new Random();
	Map<Integer, Double> valueProbabilities;
	
	public EmpiricalDistribution(Map<Integer, Double> valueProbabilities) {
		this.valueProbabilities = valueProbabilities;
	}

	public Integer sample() {
		double valueSeed = (1.0 * (r.nextInt(100) + 1)) / 100.0;
		double currentProbabilitySum = 0.0;
		for(Entry<Integer, Double> valueProbability : valueProbabilities.entrySet()) {
			currentProbabilitySum = currentProbabilitySum + valueProbability.getValue();
			if(currentProbabilitySum > valueSeed) return valueProbability.getKey();
		}
		return null;
	}
}
