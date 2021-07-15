package anonymizers.randomresponse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import distributioncollector.Variant;
import utils.LogLevel;
import utils.Logger;

/**
 * Experimental random response anonymizer for care path-attribute formatted data.
 * Randomizes the durations in the care path. To be used after the care paths have been anonymized.
 * @author Erkka Nurmi
 *
 */
public class RRAnonymizer {
	private static final Logger LOG = new Logger(LogLevel.DEBUG);

	public static void main(String[] args) {
		
		//Hard coded inputs for now
		//Chance of randomizing a line in percents (1.0 = 1%)
		double randomizationChance = 1.0;
		//Epsilon value for Laplace randomness. In other words the minimum used standard deviation
		double laplaceEpsilon = 0.1;
		
		if (args.length == 0) {
			LOG.critical("No input file specified. Exiting");
			return;
		}
		
		Random r = new Random();

		//Separating file extension from file name
		File inFile = new File(args[0]);
		int extensionStart = inFile.getPath().lastIndexOf('.');
		String extension = inFile.getPath().substring(extensionStart);
		String pathWithoutExtension = inFile.getPath().substring(0, extensionStart);
		
		//Creating output file
		File outFile = new File(pathWithoutExtension + "_RRAnonymized" + extension);

		try (
				BufferedReader br = new BufferedReader(new FileReader(inFile));
				BufferedWriter bw = new BufferedWriter(new FileWriter(outFile))
				) {
			//Locating care path attribute column
			String line = br.readLine();
			bw.write(line + System.lineSeparator());
			String[] splitLine = line.split(";");
			int carePathIndex = -1;
			for (int i = 0; i < splitLine.length; i++) {
				if (splitLine[i].equals("Path")) {
					carePathIndex = i;
					break;
				}
			}
			if(carePathIndex == -1) throw new Exception("Care path column not found");
			//Collecting possible values
			List<Variant> distributionData = collectDistributionData(inFile, carePathIndex);
			line = br.readLine();
			//Filling output file with input data + randomness
			while(line != null) {
				StringBuilder outLine = new StringBuilder();
				if ((1.0 + r.nextInt(100))/100 > randomizationChance) {
					outLine = new StringBuilder(line + System.lineSeparator());
				} else {
					splitLine = line.split(";");
					for(int i = 0; i < carePathIndex; i++) {
						outLine.append(splitLine[i]).append(";");
					}
					outLine.append(generatePath(splitLine[carePathIndex], distributionData, laplaceEpsilon));
					for(int i = carePathIndex + 1; i < splitLine.length; i++) {
						outLine.append(splitLine[i]).append(";");
					}
					outLine = new StringBuilder(outLine.substring(0, outLine.length() - 1) + System.lineSeparator());
				}
				bw.write(outLine.toString());
				line = br.readLine();
			}
			
		} catch (FileNotFoundException e) {
			LOG.critical("Target file not found. Exiting with error:", e);
		} catch (IOException e) {
			LOG.critical("An OIException has occurred. Exiting with error:", e);
		} catch (Exception e) {
			LOG.critical("Problem while reading input file. Exiting with error:", e);
		}

	}

	/**
	 * Generates random care path attribute value
	 * @param string Existing care path attribute to match
	 * @param distributionData List of collected connected discrete distributions in form of variants
	 * @param laplaceEpsilon epsilon to be used in generating Laplace randomness
	 * @return Generated care path attribute with randomness
	 * @throws Exception An exception may be thrown when sampling distributions 
	 */
	private static String generatePath(String string, List<Variant> distributionData, double laplaceEpsilon) throws Exception {
		String generatedPath;
		for(Variant variant : distributionData) {
			generatedPath = variant.matchAndGenerate(string.split(":"), laplaceEpsilon);
			if(generatedPath != null) return generatedPath;
		}
		return null;
	}

	/**
	 * Collects discrete duration distribution per variant
	 * @param inFile Input file from which the duration data is collected
	 * @param targetColumn index of the target column in the file
	 * @return List of the collected variants each containing their distribution data
	 * @throws FileNotFoundException Thrown if the target file is not found
	 * @throws IOException Thrown if there's a problem reading the file
	 */
	private static List<Variant> collectDistributionData(File inFile, int targetColumn) throws FileNotFoundException, IOException {
		List<Variant> variants = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {
			//Skipping header line
			br.readLine();
			String line = br.readLine();
			while (line != null) {
				boolean matched = false;
				String[] pathElements = line.split(";")[targetColumn].split(":");
				for (Variant variant : variants) {
					if (variant.matchPathAndAdd(pathElements)) {
						matched = true;
						break;
					}
				}
				if (!matched) {
					variants.add(new Variant(pathElements));
				}
				line = br.readLine();
			}
		}
		return variants;
	}
}
