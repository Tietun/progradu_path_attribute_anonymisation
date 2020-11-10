package datagenerator;

/**
 * Wrapper for the age and sex of a person
 * @author Erkka Nurmi
 *
 */
public class AgeSex {
	private int age;
	private Sex sex;
	
	/**
	 * Constructor
	 * @param age Age of the person
	 * @param sex Sex of the person
	 */
	public AgeSex(
			int age,
			Sex sex
			){
		this.age = age;
		this.sex = sex;
	}

	/**
	 * Gets the persons age
	 * @return the persons age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Gets the persons sex
	 * @return the persons sex
	 */
	public Sex getSex() {
		return sex;
	}
}
