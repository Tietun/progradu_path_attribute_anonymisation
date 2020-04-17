package datagenerator;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Customer {
	private static Random rand = new Random();
	private String nationalID;
	private LocalDate dateOfBirth;
	private Sex sex;
	private Map<String, String> customerAttributes;
	private List<Event> events;
	private static char[] checkSymbols = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','H','J','K','L','M','N','P','R','S','T','U','V','W','X','Y'};

	public Customer(LocalDateTime currentTime) {
		this.setAgeAndSex(currentTime);
		this.customerAttributes = new HashMap<String, String>();
		this.events = new ArrayList<>();
	}
	
	private void setAgeAndSex(LocalDateTime currentTime) {
		int yearOfBirth = 2020;
		while(yearOfBirth > 2010) {
			AgeSex ageSex = AgeDistributions.finlandCurrent.getRandomAgeAndSex();
			this.sex = ageSex.sex;
			yearOfBirth = currentTime.getYear() - ageSex.age;
		}
		int dayOfBirth;
		int monthOfBirth = 0;
		int[] monthDays;
		if(yearOfBirth % 400 == 0 || (yearOfBirth % 100 != 0 && yearOfBirth % 4 == 0)) {
			dayOfBirth = rand.nextInt(366) + 1;
			monthDays = Globals.monthDaysLeap;
		} else {
			dayOfBirth = rand.nextInt(365) + 1;
			monthDays = Globals.monthDays;
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

	public String getEventLines() {
		String lines = "";
		for(Event e : this.events) {
			String line = this.nationalID + ";" + e.getValueLine() + ";" + this.sex + ";";
				for(Event event : this.events) {
					line = line + event.getSummary() + ":";
					if(this.events.size() > (this.events.indexOf(event) + 1)) {
						line = line + "(" + (Duration.between(event.getEndTime(), this.events.get(this.events.indexOf(event) + 1).getStartTime()).getSeconds()) + "):";
					}
				}
			line = line.substring(0, line.length()-1);
			line = line + System.lineSeparator();
			lines = lines + line;
		}
		lines = lines.substring(0, lines.length()-1);
		return lines;
	}

	public void addEvent(Event event) {
		this.events.add(event);
	}

	public String getPathAttribute() {
		String line = "";
		for(Event event : this.events) {
			line = line + event.getSummary() + ":";
			if(this.events.size() > (this.events.indexOf(event) + 1)) {
				Duration duration = Duration.between(event.getEndTime(), this.events.get(this.events.indexOf(event) + 1).getStartTime());
				long durationSeconds = duration.getSeconds();
				line = line + "(" + (durationSeconds / 60) + "):";
			}
		}
		line = line.substring(0, line.length()-1) + System.lineSeparator();
		return line;
	}

	public String getFirstStart() {
		if(this.events.size() > 0) {
			return this.events.get(0).startTime.toString().replace('T', ' ') + ":00";
		}
		return "";
	}
}
