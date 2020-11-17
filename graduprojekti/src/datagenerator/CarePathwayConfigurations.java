package datagenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Existing configured CarePathways
 * @author Erkka Nurmi
 *
 */
public class CarePathwayConfigurations {
	public static List<CarePathway> carePathWays;

	/**
	 * Returns all prepared CarePathways
	 * @return All prepared CarePathways
	 */
	public static List<CarePathway> getAll() {
		carePathWays = new ArrayList<>();
		//Only the one used in the study with 100% probability. It gets added to all Customers
		carePathWays.add(new CarePathway(100).initializeWithDefaultPath());
		return carePathWays;
	}

}
