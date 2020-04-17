package distributioncollector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import utils.LogLevel;
import utils.Logger;

public class DistributionGenerator {
	private static final Logger LOG = new Logger(LogLevel.DEBUG);
	
	public static void main(String[] args) {
		
		LOG.info("DistributionGenerator uses the following Apache v2 -license libraries:");
		LOG.info("Apache Commons Math v3.2: http://commons.apache.org/proper/commons-math/");
		LOG.info(
				"Jackson 2.10.0 (including core, annotations and databind): https://github.com/FasterXML/jackson");
		LOG.info("The Apache v2 license can be found at: https://www.apache.org/licenses/LICENSE-2.0");

		if (args.length < 2) {
			LOG.critical("Missing arguments. Please give arguments timelessData and variantData");
			return;
		}

		double minSDOfMean = 0.1;

		File dataFile = new File(args[0]);
		File variantFile = new File(args[1]);
		int extensionStart = dataFile.getPath().lastIndexOf('.');
		String extension = dataFile.getPath().substring(extensionStart);
		String pathWithoutExtension = dataFile.getPath().substring(0, extensionStart);
		File retimedOutFile = new File(pathWithoutExtension + "_retimed" + extension);

		ObjectMapper mapper = new ObjectMapper();

		List<Variant> variants = new ArrayList<>();

		try (BufferedReader dataReader = new BufferedReader(new FileReader(dataFile));
				BufferedReader variantReader = new BufferedReader(new FileReader(variantFile));
				BufferedWriter reTimedWriter = new BufferedWriter(new FileWriter(retimedOutFile))) {
			String allVariantData = "";
			String line = variantReader.readLine();
			while (line != null) {
				allVariantData = allVariantData + line;
				line = variantReader.readLine();
			}
			String[] variantElementsLists = allVariantData.split(";");
			for (String variantElementsList : variantElementsLists) {
				if (!variantElementsList.equals("")) {
					variants.add(new Variant(mapper.readValue(variantElementsList, ElementListWrapper.class)));
				}
			}
			variantReader.close();

			line = dataReader.readLine();
			reTimedWriter.write(line + System.lineSeparator());
			String[] splitLine = line.split(";");
			int carePathIndex = -1;
			for (int i = 0; i < splitLine.length; i++) {
				if (splitLine[i].equals("Path")) {
					carePathIndex = i;
					break;
				}
			}
			line = dataReader.readLine();
			while (line != null) {
				splitLine = line.split(";");
				String retimedLine = "";
				for (int i = 0; i < carePathIndex; i++) {
					retimedLine = retimedLine + splitLine[i] + ";";
				}
				String retimedPath = null;
				for (Variant variant : variants) {
					retimedPath = variant.matchTimelessAndGenerate(splitLine[carePathIndex].split(":"), minSDOfMean);
					if (retimedPath != null)
						break;
				}
				retimedLine = retimedLine + retimedPath + ";";
				for (int i = carePathIndex + 1; i < splitLine.length; i++) {
					retimedLine = retimedLine + splitLine[i] + ";";
				}
				retimedLine = retimedLine.substring(0, retimedLine.length() - 1) + System.lineSeparator();
				reTimedWriter.write(retimedLine);
				line = dataReader.readLine();
			}
			dataReader.close();
			reTimedWriter.close();
			LOG.info("Ready");

		} catch (FileNotFoundException e) {
			LOG.critical("A critical error has occured: ", e);
			return;
		} catch (IOException e) {
			LOG.critical("A critical error has occured: ", e);
			return;
		} catch (Exception e) {
			LOG.critical("A critical error has occured: ", e);
			e.printStackTrace();
		}
	}

}
