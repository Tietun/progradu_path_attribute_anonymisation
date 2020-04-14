package datagenerator;

import java.util.Random;

public class AgeDistribution {
	private static Random rand = new Random();
	private int[] countCumulativeFemale; //All positive or 0;
	private int[] reverseCountCumulativeMale; //All negative;
	
	public AgeDistribution(int[] countCumulativeFemale,int[] countCumulativeMale) {
		this.countCumulativeFemale = countCumulativeFemale;
		this.reverseCountCumulativeMale = new int[countCumulativeMale.length];
		for(int i = 0; i < countCumulativeMale.length; i++) {
			reverseCountCumulativeMale[i] = 0 - countCumulativeMale[i];
		}
	}
	
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
