package datagenerator;

import java.util.ArrayList;
import java.util.List;

public class CarePathwayConfigurations {
	public static List<CarePathway> carePathWays;

	public static List<CarePathway> getAll() {
		carePathWays = new ArrayList<>();
		carePathWays.add(new CarePathway(100));
		return carePathWays;
	}

}
