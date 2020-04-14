package distributions;

import java.util.Random;

public class UniformDistribution implements Distribution{
	private static Random r = new Random();
	private int min;
	private int max;
	

	public UniformDistribution(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	public Integer sample() {
		return r.nextInt(max) + min;
	}

}
