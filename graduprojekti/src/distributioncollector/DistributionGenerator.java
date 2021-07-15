package distributioncollector;

import com.fasterxml.jackson.databind.ObjectMapper;
import utils.LogLevel;
import utils.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class generates new duration info to a timeless path attribute formatted data
 * @author Erkka Nurmi
 *
 */
public class DistributionGenerator {
	private static final Logger LOG = new Logger(LogLevel.DEBUG);

	public static void main(String[] args) {

		LOG.info("DistributionGenerator uses the following Apache v2 -license libraries:");
		LOG.info("Apache Commons Math v3.2: http://commons.apache.org/proper/commons-math/");
		LOG.info("Jackson 2.10.0 (including core, annotations and databind): https://github.com/FasterXML/jackson");
		LOG.info("The Apache v2 license can be found at: https://www.apache.org/licenses/LICENSE-2.0");

		if (args.length < 2) {
			LOG.critical("Missing arguments. Please give arguments timelessData and variantData",
					new IllegalArgumentException());
			return;
		}

		double epsilon = 1;

		File dataFile = new File(args[0]);
		File variantFile = new File(args[1]);
		File retimedOutFile;
		try {
			int extensionStart = dataFile.getPath().lastIndexOf('.');
			String extension = dataFile.getPath().substring(extensionStart);
			String pathWithoutExtension = dataFile.getPath().substring(0, extensionStart);
			//This is the output file
			retimedOutFile = new File(pathWithoutExtension + "_retimed" + extension);
		} catch (Exception e) {
			LOG.critical("Unexpected structure of argument", new IllegalArgumentException(e));
			return;
		}

		ObjectMapper mapper = new ObjectMapper();

		List<Variant> variants = new ArrayList<>();

		try (BufferedReader dataReader = new BufferedReader(new FileReader(dataFile));
				BufferedReader variantReader = new BufferedReader(new FileReader(variantFile));
				BufferedWriter reTimedWriter = new BufferedWriter(new FileWriter(retimedOutFile))) {
			//We gather variant info and frequencies
			StringBuilder allVariantData = new StringBuilder();
			String line = variantReader.readLine();
			while (line != null) {
				allVariantData.append(line);
				line = variantReader.readLine();
			}
			String[] variantElementsLists = allVariantData.toString().split(";");
			//We generate variant objects
			for (String variantElementsList : variantElementsLists) {
				if (!variantElementsList.equals("")) {
					variants.add(new Variant(mapper.readValue(variantElementsList, ElementListWrapper.class)));
				}
			}
			variantReader.close();

			line = dataReader.readLine();
			reTimedWriter.write(line.replace(";TimelessPath", "") + System.lineSeparator());
			String[] splitLine = line.split(";");
			int carePathIndex = -1;
			int originalCarePathIndex = -1;
			for (int i = 0; i < splitLine.length; i++) {
				if (splitLine[i].equals("Path")) {
					originalCarePathIndex = i;
				} else if (splitLine[i].equals("TimelessPath")) {
					carePathIndex = i;
				}
			}
			line = dataReader.readLine();
			//We generate output lines with the generated variants
			while (line != null) {
				splitLine = line.split(";");
				StringBuilder retimedLine = new StringBuilder();
				for (int i = 0; i < carePathIndex; i++) {
					if(i != originalCarePathIndex) retimedLine.append(splitLine[i]).append(";");
				}
				String retimedPath = null;
				if(structureMatches(splitLine[carePathIndex], splitLine[originalCarePathIndex])){
					retimedPath = splitLine[originalCarePathIndex];
				} else  {
					for (Variant variant : variants) {
						retimedPath = variant.matchTimelessAndGenerate(splitLine[carePathIndex].split(":"), epsilon);
						if (retimedPath != null)
							break;
					}
				}
				retimedLine.append(retimedPath).append(";");
				for (int i = carePathIndex + 1; i < splitLine.length; i++) {
					retimedLine.append(splitLine[i]).append(";");
				}
				retimedLine = new StringBuilder(retimedLine.substring(0, retimedLine.length() - 1) + System.lineSeparator());
				reTimedWriter.write(retimedLine.toString());
				line = dataReader.readLine();
			}
			dataReader.close();
			reTimedWriter.close();
			LOG.info("Ready");

		} catch (IOException e) {
			LOG.critical("A critical error has occurred: ", e);
		} catch (Exception e) {
			LOG.critical("A critical error has occurred: ", e);
			e.printStackTrace();
		}
	}

	private static boolean structureMatches(String timelessCarePath, String originalCarePath) {
		return timelessCarePath.equals(originalCarePath.replaceAll("\\([0-9]*\\)", "").replaceAll("::", ":"));

	}

}
