package datagenerator;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents a Health/Social care customer
 * @author Erkka Nurmi
 *
 */
public class Customer {
	private static final Random rand = new Random();
	private String nationalID;
	private LocalDate dateOfBirth;
	private Sex sex;
	@SuppressWarnings("unused") // May be used later
	private final Map<String, String> customerAttributes;
	private final List<Event> events;
	//Check symbols for finnish national identification number
	private static final char[] checkSymbols = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','H','J','K','L','M','N','P','R','S','T','U','V','W','X','Y'};

	/**
	 * Constructor
	 * @param currentTime The time "now". Used to generate the customer's age in relation to birth year
	 */
	public Customer(LocalDateTime currentTime) {
		this.setAgeAndSex(currentTime);
		this.customerAttributes = new HashMap<>();
		this.events = new ArrayList<>();
	}
	
	/**
	 * Sets the age and sex for the customer
	 * @param currentTime The time "now". Used to generate the customer's age in relation to birth year
	 */
	private void setAgeAndSex(LocalDateTime currentTime) {
		int yearOfBirth = 2020;
		while(yearOfBirth > 2010) {
			AgeSex ageSex = AgeDistributions.FINLAND_CURRENT.getRandomAgeAndSex();
			this.sex = ageSex.getSex();
			yearOfBirth = currentTime.getYear() - ageSex.getAge();
		}
		int dayOfBirth;
		int monthOfBirth = 0;
		int[] monthDays;
		if(yearOfBirth % 400 == 0 || (yearOfBirth % 100 != 0 && yearOfBirth % 4 == 0)) {
			dayOfBirth = rand.nextInt(366) + 1;
			monthDays = Globals.MONTH_DAYS_LEAP;
		} else {
			dayOfBirth = rand.nextInt(365) + 1;
			monthDays = Globals.MONTH_DAYS;
		}
		for(int i = 0; i < monthDays.length; i++) {
			if(dayOfBirth <= monthDays[i]) {
				monthOfBirth = i + 1;
				break;
			} else {
				dayOfBirth = dayOfBirth - monthDays[i];
			}
		}
		this.dateOfBirth = LocalDate.of(yearOfBirth, monthOfBirth, dayOfBirth);
		if(this.dateOfBirth.isAfter(currentTime.toLocalDate())) this.setAgeAndSex(currentTime);
		
		String dayOfBirthS = "" + dayOfBirth;
		if(dayOfBirthS.length() <= 1) dayOfBirthS = "0" + dayOfBirthS;
		String monthOfBirthS = "" + monthOfBirth;
		if(monthOfBirthS.length() <= 1) monthOfBirthS = "0" + monthOfBirthS;
		String YearOfBirthS = ("" + yearOfBirth).substring(2, 4);
		
		//If this breaks in the year 3000 here's your issue
		char separator = '+';
		if(yearOfBirth >= 1900) separator = '-';
		if(yearOfBirth >= 2000) separator = 'A';
		
		int randomPart = (rand.nextInt(449) + 1) * 2;
		if(this.sex == Sex.MALE) randomPart++;
		
		String randomPartS = "" + randomPart;
		if(randomPartS.length() <= 1) randomPartS = "00" + randomPartS;
		else if(randomPartS.length() <= 2) randomPartS = "0" + randomPartS;
		
		int checkSymbolBase = Integer.parseInt(dayOfBirthS + monthOfBirthS + YearOfBirthS + randomPartS);
		char checkSymbol = checkSymbols[checkSymbolBase % 31];
		this.nationalID = dayOfBirthS + monthOfBirthS + YearOfBirthS + separator + randomPartS + checkSymbol;
	}

	/**
	 * Returns the events relating to this customer in a csv-friendly String
	 * @return CSV friendly String containing info on the customer's events
	 */
	public String getEventLines() {
		StringBuilder lines = new StringBuilder();
		for(Event e : this.events) {
			StringBuilder line = new StringBuilder(this.nationalID + ";" + e.getValueLine() + ";" + this.sex + ";");
				for(Event event : this.events) {
					line.append(event.getSummary()).append(":");
					if(this.events.size() > (this.events.indexOf(event) + 1)) {
						line.append("(").append(Duration.between(event.getEndTime(), this.events.get(this.events.indexOf(event) + 1).getStartTime()).getSeconds()).append("):");
					}
				}
			line = new StringBuilder(line.substring(0, line.length() - 1));
			line.append(System.lineSeparator());
			lines.append(line);
		}
		lines = new StringBuilder(lines.substring(0, lines.length() - 1));
		return lines.toString();
	}

	/**
	 * Adds an event to the Customer
	 * @param event The event to be added
	 */
	public void addEvent(Event event) {
		this.events.add(event);
	}

	/**
	 * Returns the Customer's events in the path attribute format
	 * @return The Customer's events in the path attribute format
	 */
	public String getPathAttribute() {
		StringBuilder line = new StringBuilder();
		for(Event event : this.events) {
			line.append(event.getSummary()).append(":");
			if(this.events.size() > (this.events.indexOf(event) + 1)) {
				Duration duration = Duration.between(event.getEndTime(), this.events.get(this.events.indexOf(event) + 1).getStartTime());
				long durationSeconds = duration.getSeconds();
				line.append("(").append(durationSeconds / 60).append("):");
			}
		}
		line = new StringBuilder(line.substring(0, line.length() - 1) + System.lineSeparator());
		return line.toString();
	}

	/**
	 * Returns the start time of the customer's first event
	 * @return The start time of the customer's first event
	 */
	public String getFirstStart() {
		if(this.events.size() > 0) {
			return this.events.get(0).getStartTime().toString().replace('T', ' ') + ":00";
		}
		return "";
	}
}
