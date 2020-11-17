package datagenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.distribution.ExponentialDistribution;

/**
 * Represents a care pathway. Care pathway is a process of care events
 * @author Erkka Nurmi
 *
 */
public class CarePathway {

	private static Random r = new Random();
	private List<NeedBase> needs;
	private int percantagePossibility;
	
	/**
	 * Constructor
	 * @param percentagePossibility How likely is this carepathway to occur during a person's life
	 */
	public CarePathway(int percentagePossibility) {
		this.needs = new ArrayList<>();
		this.percantagePossibility = percentagePossibility;
	}

	/**
	 * Initializes the care pathway with the default needs used in the study
	 * @return This after initialization
	 */
	public CarePathway initializeWithDefaultPath() {
		this.needs = new ArrayList<>();
		Random r = new Random();
		CarePathway baseway = this
				.withNeed(new WaitNeed(new ExponentialDistribution(259200)))
				.withNeed(new EventNeed("A", new ExponentialDistribution(65)))
				.withNeed(new WaitNeed(new ExponentialDistribution(12)));
		int choiceSeed = r.nextInt(100);
		if(choiceSeed != 0) {
			baseway = baseway
				.withNeed(new EventNeed("B1", new ExponentialDistribution(25)));
		} else {
			baseway = baseway
				.withNeed(new EventNeed("B2", new ExponentialDistribution(33)));
		}
		baseway = baseway
				.withNeed(new WaitNeed(new ExponentialDistribution(150)))
				.withNeed(new EventNeed("C", new ExponentialDistribution(120)))
				.withNeed(new WaitNeed(new ExponentialDistribution(270)));
		choiceSeed = r.nextInt(100);
		if(choiceSeed != 0) {
			baseway = baseway
				.withNeed(new EventNeed("D1", new ExponentialDistribution(25)));
		} else {
			baseway = baseway
				.withNeed(new EventNeed("D2", new ExponentialDistribution(33)));
		}
		choiceSeed = r.nextInt(5);
		baseway
			.withNeed(new WaitNeed(new ExponentialDistribution(300)))
			.withNeed(new EventNeed("E" + choiceSeed, new ExponentialDistribution(130)));
		return this;
	}
	
	/**
	 * Adds to customer if the probability is hit
	 * @param customer Customer to whom this CarePathway is added
	 * @param start Start time of the CarePathway
	 */
	public void addToCustomerByDefaultProbability(Customer customer, LocalDateTime start) {
		if (this.isHit()) {
			LocalDateTime currentTime = start.minusDays(0);
			for (NeedBase need : this.needs) {
				if (need instanceof EventNeed) {
					EventNeed eNeed = (EventNeed) need;
					LocalDateTime startTime = currentTime.plusMinutes(0);
					currentTime = currentTime.plusMinutes(Math.round(eNeed.getDuration()));
					customer.addEvent(new Event(eNeed.getName(), startTime, currentTime));
				} else if (need instanceof WaitNeed) {
					WaitNeed wNeed = (WaitNeed) need;
					currentTime = currentTime.plusMinutes(Math.round(wNeed.getDuration()));
				}
			}
		}
	}
	
	/**
	 * Adds a need to the CarePathway
	 * @param need The need to be added
	 * @return This after adding
	 */
	public CarePathway withNeed(NeedBase need) {
		this.needs.add(need);
		return this;
	}

	/**
	 * Is the probability a hit
	 * @return Is the probability a hit
	 */
	private boolean isHit() {
		int seed = r.nextInt(100);
		if(this.percantagePossibility > seed) return true;
		return false;
	}

}
