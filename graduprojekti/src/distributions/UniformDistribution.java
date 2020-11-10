package distributions;

import java.util.Random;

/**
 * A uniform distribution of integer (represented by Long) values
 * @author Erkka Nurmi
 *
 */
public class UniformDistribution implements Distribution{
	private static Random r = new Random();
	private long min;
	private long max;
	
	/**
	 * Constructor
	 * @param min Min value
	 * @param max Max value
	 */
	public UniformDistribution(long min, long max) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public Long sample() {
		return (long) (r.nextInt((int) max) + min);
	}
}
