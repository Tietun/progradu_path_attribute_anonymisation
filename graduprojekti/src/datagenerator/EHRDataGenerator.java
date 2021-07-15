package datagenerator;

import utils.LogLevel;
import utils.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Generator for health/social care pseudo data
 * @author Erkka Nurmi
 *
 */
public class EHRDataGenerator {

	private static final Logger LOG = new Logger(LogLevel.DEBUG);

	/**
	 * Generates health/social care pseudo data into a file in the path attribute form
	 * @param args unused
	 */
	public static void main(String[] args) {
		//Parameters hard coded for now
		LocalDateTime ehrStart = LocalDateTime.of(LocalDate.of(2017, 1, 1), LocalTime.of(0, 0, 0));
		LocalDateTime ehrEnd = LocalDateTime.of(LocalDate.of(2019, 12, 31), LocalTime.of(23, 59, 59));
		int customerCount = 100000;
		List<CarePathway> carePathways = CarePathwayConfigurations.getAll();
		String filePath = "./data" + customerCount + ".csv";
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
			bw.write("ID;StartTime;Path" + System.lineSeparator());
			for (int i = 0; i < customerCount; i++) {
				Customer customer = new Customer(ehrEnd);
				for (CarePathway carePathway : carePathways) {
					carePathway.addToCustomerByDefaultProbability(customer, ehrStart);
				}
				String outLine = i + ";" + customer.getFirstStart() + ";" + customer.getPathAttribute();
				bw.write(outLine);
			}
		} catch (IOException e) {
			LOG.critical("A critical error has occurred: ", e);
			return;
		} 
		LOG.info("Ready");
	}

}
