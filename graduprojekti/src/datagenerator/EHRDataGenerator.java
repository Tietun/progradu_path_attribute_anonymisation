package datagenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import utils.LogLevel;
import utils.Logger;

public class EHRDataGenerator {

	private static final Logger LOG = new Logger(LogLevel.DEBUG);

	public static void main(String[] args) {
		LocalDateTime ehrStart = LocalDateTime.of(LocalDate.of(2017, 1, 1), LocalTime.of(0, 0, 0));
		LocalDateTime ehrEnd = LocalDateTime.of(LocalDate.of(2019, 12, 31), LocalTime.of(23, 59, 59));
		int customerCount = 10000;
		List<CarePathway> carePathways = CarePathwayConfigurations.getAll();
		boolean print = true;
		String filePath = "./data" + customerCount + ".csv";
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath)))) {
			bw.write("ID;StartTime;Path" + System.lineSeparator());
			for (int i = 0; i < customerCount; i++) {
				Customer customer = new Customer(ehrEnd);
				for (CarePathway carePathway : carePathways) {
					carePathway.addToCustomerByDefaultProbability(customer, ehrStart, ehrEnd);
				}
				if (print) {
					String outLine = i + ";" + customer.getFirstStart() + ";" + customer.getPathAttribute();
					bw.write(outLine);
				}
			}
			bw.close();
		} catch (IOException e) {
			LOG.critical("A critical error has occured: ", e);
			return;
		} 
		LOG.info("Ready");
	}

}
