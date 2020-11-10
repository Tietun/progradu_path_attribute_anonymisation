package distributions;

/**
 * Interface for integer (represented by Long) distributions
 * @author Erkka Nurmi
 *
 */
public interface Distribution {
	
	/**
	 * Samples the distribution
	 * @return A value sampled from the distribution
	 */
	public Long sample();
}
