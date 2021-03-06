package datagenerator;

import java.util.Random;

/**
 * A distribution of age and sex
 * @author Erkka Nurmi
 *
 */
public class AgeDistribution {
	private static final Random rand = new Random();
	private final int[] countCumulativeFemale; //All positive or 0;
	private final int[] reverseCountCumulativeMale; //All negative;
	
	/**
	 * Constructor
	 * @param countCumulativeFemale Cumulative count of females by age (index)
	 * @param countCumulativeMale Cumulative count of males by age (index)
	 */
	public AgeDistribution(int[] countCumulativeFemale,int[] countCumulativeMale) {
		this.countCumulativeFemale = countCumulativeFemale;
		this.reverseCountCumulativeMale = new int[countCumulativeMale.length];
		for(int i = 0; i < countCumulativeMale.length; i++) {
			reverseCountCumulativeMale[i] = -countCumulativeMale[i];
		}
	}
	
	/**
	 * Samples the distribution for an age and a sex
	 * @return Wrapper for the generated age and sex
	 */
	public AgeSex getRandomAgeAndSex() {
		int femaleMax = countCumulativeFemale[countCumulativeFemale.length - 1];
		int maleMin = reverseCountCumulativeMale[reverseCountCumulativeMale.length - 1]; 
		int seed = rand.nextInt(femaleMax
				- maleMin)
				+ maleMin;
		int age = 0;
		Sex sex;
		if (seed >= 0) {
			sex = Sex.FEMALE;
			for(int i = 0; i < countCumulativeFemale.length; i++) {
				if(seed <= countCumulativeFemale[i]) {
					age = i;
					break;
				}
			}
		} else {
			sex = Sex.MALE;
			for(int i = 0; i < reverseCountCumulativeMale.length; i++) {
				if(seed >= reverseCountCumulativeMale[i]) {
					age = i;
					break;
				}
			}
		}
		return new AgeSex(age,sex);
	}
}
