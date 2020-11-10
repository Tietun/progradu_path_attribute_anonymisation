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

/**
 * Collects distributions from a csv file that is in path attribute format
 * @author Erkka Nurmi
 *
 */
public class DistributionCollector {
	private static final Logger LOG = new Logger(LogLevel.DEBUG);

	public static void main(String[] args) {
		System.out.println("DistributionCollector uses the following Apache v2 -license libraries:");
		System.out.println("Apache Commons Math v3.2: http://commons.apache.org/proper/commons-math/");
		System.out.println(
				"Jackson 2.10.0 (including core, annotations and databind): https://github.com/FasterXML/jackson");
		System.out.println("The Apache v2 license can be found at: https://www.apache.org/licenses/LICENSE-2.0");

		if (args.length < 1) {
			LOG.critical("Missing arguments. Please give argument source data");
			return;
		}

		File inFile = new File(args[0]);
		File variantOutFile;
		File distributionOutFile;
		File timelessOutFile;
		int extensionStart = inFile.getPath().lastIndexOf('.');
		try {
			String extension = inFile.getPath().substring(extensionStart);
			String pathWithoutExtension = inFile.getPath().substring(0, extensionStart);
			//This file will contain the distribution info in variant format
			variantOutFile = new File(pathWithoutExtension + "_variants.json");
			//This file will contain the distribution info in a more easily manipulated format
			distributionOutFile = new File(pathWithoutExtension + "_distributions.json");
			//This file will be identical to the source file except times have been removed from the path attribute
			timelessOutFile = new File(pathWithoutExtension + "_timeless" + extension);
		} catch (Exception e) {
			LOG.critical("Unexpected structure of argument", new IllegalArgumentException(e));
			return;
		}

		ObjectMapper mapper = new ObjectMapper();

		List<Variant> variants = new ArrayList<>();

		//Reads the source file line by line while writing the output files
		try (BufferedReader br = new BufferedReader(new FileReader(inFile));
				BufferedWriter variantWriter = new BufferedWriter(new FileWriter(variantOutFile));
				BufferedWriter distributionWriter = new BufferedWriter(new FileWriter(distributionOutFile));
				BufferedWriter timelessWriter = new BufferedWriter(new FileWriter(timelessOutFile))) {

			String line = br.readLine();
			timelessWriter.write(line + System.lineSeparator());
			String[] splitLine = line.split(";");
			int carePathIndex = -1;
			for (int i = 0; i < splitLine.length; i++) {
				if (splitLine[i].equals("Path")) {
					carePathIndex = i;
					break;
				}
			}
			line = br.readLine();
			while (line != null) {
				splitLine = line.split(";");
				String timelessLine = "";
				for (int i = 0; i < carePathIndex; i++) {
					timelessLine = timelessLine + splitLine[i] + ";";
				}
				String[] pathElements = splitLine[carePathIndex].split(":");
				Variant matchedVariant = null;
				for (Variant variant : variants) {
					if (variant.matchPathAndAdd(pathElements)) {
						matchedVariant = variant;
						break;
					}
				}
				if (matchedVariant == null) {
					matchedVariant = new Variant(pathElements);
					variants.add(matchedVariant);
				}
				timelessLine = timelessLine + matchedVariant.getTimelessPath();

				for (int i = carePathIndex + 1; i < splitLine.length; i++) {
					timelessLine = timelessLine + splitLine[i] + ";";
				}
				timelessLine = timelessLine.substring(0, timelessLine.length() - 1) + System.lineSeparator();
				timelessWriter.write(timelessLine);
				line = br.readLine();
			}
			for (Variant variant : variants) {
				variantWriter.write(mapper.writeValueAsString(variant.getElementListWrapper()) + ";");
				distributionWriter.write(mapper.writeValueAsString(variant.getDistributionWrapper()));
			}
			br.close();
			timelessWriter.close();
			variantWriter.close();
			LOG.info("Ready");

		} catch (FileNotFoundException e) {
			LOG.critical("A critical error has occured: ", e);
			return;
		} catch (IOException e) {
			LOG.critical("A critical error has occured: ", e);
			return;
		} catch (Exception e) {
			LOG.critical("A critical error has occured: ", e);
			return;
		}
	}
}
