package distributions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.math3.distribution.LaplaceDistribution;

/**
 * Represents a discrete empirical distribution of integer (represented by Long) values
 * @author Erkka Nurmi
 *
 */
public class EmpiricalDistribution implements Distribution {
	private static Random r = new Random();
	private Map<Integer, Double> valueProbabilities;
	private double mean;
	private double variance;
	private double standardDeviation;
	private double min;
	private double max;

	/**
	 * Constructor
	 * @param valueInstances Map of value instances. Key = empirical value, value = empirical frequency
	 * @param totalValueInstances Total number of value instances. Should be equal to the sum of empirical frequencies.
	 * Is given separately so that there's no need to calculate this repeatedly
	 */
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

	@Override
	public Long sample() {
		double valueSeed = (1.0 * (r.nextInt(100) + 1)) / 100.0;
		double currentProbabilitySum = 0.0;
		for (Entry<Integer, Double> valueProbability : valueProbabilities.entrySet()) {
			currentProbabilitySum = currentProbabilitySum + valueProbability.getValue();
			if (currentProbabilitySum >= valueSeed)
				return 0L + valueProbability.getKey();
		}
		return null;
	}

	/**
	 * Samples the distribution with Laplace randomness
	 * @param epsilon Epsilon value for the Laplace randomness
	 * @return Value sampled from the distribution
	 * @throws Exception Thrown if only one value is possible or if the sampled value would some how be null
	 */
	public Long sampleWithLaplaceRandomness(double epsilon) throws Exception {
		long result = -1;
		while (result < 0) {
			double range = this.max - this.min;
			if (range <= 0) {
				throw new Exception(
						"Data contains events with non variable service/traveltimes. Ensure anonymization has taken place");
			}
			LaplaceDistribution randomnessDistribution = new LaplaceDistribution(0, range / epsilon);
			long shiftBy = Math.round(randomnessDistribution.sample());
			Long sample = this.sample();
			if(sample == null) {
				throw new Exception();
			}
			result = sample + shiftBy;
		}
		return result;
	}

	/**
	 * Gets the mean of the distribution
	 * @return mean of the distribution
	 */
	public double getMean() {
		return mean;
	}
	
	/**
	 * Gets the variance of the distribution
	 * @return variance of the distribution
	 */
	public double getVariance() {
		return variance;
	}

	/**
	 * Gets the standard deviation of the distribution
	 * @return standard deviation of the distribution
	 */
	public double getStandardDeviation() {
		return standardDeviation;
	}

}
