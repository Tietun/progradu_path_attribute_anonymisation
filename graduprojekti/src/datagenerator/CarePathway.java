package datagenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.ExponentialDistribution;

import distributions.UniformDistribution;

public class CarePathway {
	private List<NeedBase> needs;
	private int percantagePossibility;
	
	public CarePathway(int percentagePossibility) {
		this.needs = new ArrayList<>();
		this.percantagePossibility = percentagePossibility;
	}

	public void addToCustomerByDefaultProbability(Customer customer, LocalDateTime start, LocalDateTime end) {

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
	
	public CarePathway withNeed(NeedBase need) {
		this.needs.add(need);
		return this;
	}

	private boolean isHit() {
		Random r = new Random();
		int seed = r.nextInt(100);
		if(this.percantagePossibility > seed) return true;
		return false;
	}

}
