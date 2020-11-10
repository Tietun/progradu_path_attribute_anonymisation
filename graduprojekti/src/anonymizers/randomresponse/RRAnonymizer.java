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
 * Experimental random response anonymizer for carepath-attribute formatted data.
 * Randomizes the durations in the carepath. To be used after the carepaths have been anonymized.
 * @author Erkka Nurmi
 *
 */
public class RRAnonymizer {
	private static Logger log = new Logger(LogLevel.DEBUG);

	public static void main(String[] args) {
		
		//Hard coded inputs for now
		//Chance of randomizing a line in percents (1.0 = 1%)
		double randomizationChance = 1.0;
		//Epsilon value for Laplace randomness. In other words the minimum used standard deviation
		double laplaceEpsilon = 0.1;
		
		if (args.length == 0) {
			log.critical("No input file specified. Exiting");
			return;
		}
		
		Random r = new Random();

		//Separating file extension from file name
		File inFile = new File(args[0]);
		int extensionStart = inFile.getPath().lastIndexOf('.');
		String extension = inFile.getPath().substring(extensionStart);
		String pathWithoutExtension = inFile.getPath().substring(0, extensionStart);
		
		//Creating output file
		File outFile = new File(pathWithoutExtension + "_RRanonymized" + extension);

		try (
				BufferedReader br = new BufferedReader(new FileReader(inFile));
				BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
				) {
			//Locating carepath attribute column
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
			if(carePathIndex == -1) throw new Exception("Carepath column not found");
			//Collecting possible values
			List<Variant> distributionData = collectDistributionData(inFile, carePathIndex);
			line = br.readLine();
			//Filling output file with input data + randomness
			while(line != null) {
				String outLine = "";
				if ((1.0 + r.nextInt(100))/100 > randomizationChance) {
					outLine = line + System.lineSeparator();
				} else {
					splitLine = line.split(";");
					for(int i = 0; i < carePathIndex; i++) {
						outLine = outLine + splitLine[i] + ";";
					}
					outLine = outLine + generatePath(splitLine[carePathIndex], distributionData, laplaceEpsilon);
					for(int i = carePathIndex + 1; i < splitLine.length; i++) {
						outLine = outLine + splitLine[i] + ";";
					}
					outLine = outLine.substring(0, outLine.length() - 1) + System.lineSeparator();
				}
				bw.write(outLine);
				line = br.readLine();
			}
			
		} catch (FileNotFoundException e) {
			log.critical("Target file not found. Exiting with error:", e);
		} catch (IOException e) {
			log.critical("An OIException has occured. Exiting with error:", e);
		} catch (Exception e) {
			log.critical("Problem while reading inputfile. Exiting with error:", e);
		}

	}

	/**
	 * Generates random carepath attribute value
	 * @param string Existing carepath attribute to match
	 * @param distributionData List of collected connected discrete distributions in form of variants
	 * @param laplaceEpsilon epsilon to be used in generating Laplace randomness
	 * @return Generated carepath attribute with randomness
	 * @throws Exception An exception may be thrown when sampling distributions 
	 */
	private static String generatePath(String string, List<Variant> distributionData, double laplaceEpsilon) throws Exception {
		String generatedPath = null;
		for(Variant variant : distributionData) {
			generatedPath = variant.matchAndGenerate(string.split(":"), laplaceEpsilon);
			if(generatedPath != null) return generatedPath;
		}
		return generatedPath;
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
			String line = br.readLine();
			line = br.readLine();
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
			br.close();
		}
		return variants;
	}
}
