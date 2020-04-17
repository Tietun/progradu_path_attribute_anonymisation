package distributions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.math3.distribution.LaplaceDistribution;

public class EmpiricalDistribution implements Distribution {
	private static Random r = new Random();
	private Map<Integer, Double> valueProbabilities;
	private double mean;
	private double variance;
	private double standardDeviation;
	private double min;
	private double max;

	public EmpiricalDistribution(Map<Integer, Integer> valueInstances, int totalValueInstances) {
		this.valueProbabilities = new HashMap<>();
		this.min = Double.MAX_VALUE;
		this.max = Double.MIN_VALUE;
		Integer lastKey = null;
		for (Entry<Integer, Integer> durationInstance : valueInstances.entrySet()) {
			lastKey = durationInstance.getKey();
			double probability = (1.0 * durationInstance.getValue()) / (1.0 * totalValueInstances);
			valueProbabilities.put(durationInstance.getKey(), probability);
			if (durationInstance.getKey() > this.max)
				this.max = durationInstance.getKey();
			if (durationInstance.getKey() < this.min)
				this.min = durationInstance.getKey();
		}
		double probabilitySum = 0;
		for(double probability : this.valueProbabilities.values()) {
			probabilitySum = probabilitySum + probability;
		}
		if(probabilitySum < 1) {
			Double roundingNumber = valueProbabilities.get(lastKey);
			valueProbabilities.put(lastKey, roundingNumber + (1 - probabilitySum));
		}

		List<Integer> flatValues = new ArrayList<>();
		for (Entry<Integer, Integer> valueInstance : valueInstances.entrySet()) {
			for (int i = 0; i < valueInstance.getValue(); i++) {
				flatValues.add(valueInstance.getKey());
			}
		}

		this.mean = 0;
		for (Integer value : flatValues) {
			this.mean = this.mean + value;
		}
		this.mean = this.mean / flatValues.size();

		double minusMeanSquaredSum = 0;
		for (int i = 0; i < flatValues.size(); i++) {
			double minusMeanSquared = (flatValues.get(i) - mean) * (flatValues.get(i) - mean);
			minusMeanSquaredSum = minusMeanSquaredSum + minusMeanSquared;
		}

		this.variance = minusMeanSquaredSum / flatValues.size();
		this.standardDeviation = Math.sqrt(variance);
	}

	public Integer sample() {
		double valueSeed = (1.0 * (r.nextInt(100) + 1)) / 100.0;
		double currentProbabilitySum = 0.0;
		for (Entry<Integer, Double> valueProbability : valueProbabilities.entrySet()) {
			currentProbabilitySum = currentProbabilitySum + valueProbability.getValue();
			if (currentProbabilitySum >= valueSeed)
				return valueProbability.getKey();
		}
		return null;
	}

	public long sampleWithLaplaceRandomness(double epsilon) throws Exception {
		long result = -1;
		//while (result < 0) {
			double range = this.max - this.min;
			if (range <= 0) {
				throw new Exception(
						"Data contains events with non variable service/traveltimes. Ensure anonymization has taken place");
			}
			LaplaceDistribution randomnessDistribution = new LaplaceDistribution(0, range / epsilon);
			long shiftBy = Math.round(randomnessDistribution.sample());
			Integer sample = this.sample();
			if(sample == null) {
				throw new Exception();
			}
			result = sample + shiftBy;
		//}
		return result;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getVariance() {
		return variance;
	}

	public void setVariance(double variance) {
		this.variance = variance;
	}

	public double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

}
