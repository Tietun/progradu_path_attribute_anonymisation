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

public class RRAnonymizer {
	private static Logger log = new Logger(LogLevel.DEBUG);

	public static void main(String[] args) {
		
		double randomizationChance = 1.0;
		double minSDOfMean = 0.1;
		
		if (args.length == 0) {
			log.critical("No input file specified. Exiting");
			return;
		}
		
		Random r = new Random();

		File inFile = new File(args[0]);
		int extensionStart = inFile.getPath().lastIndexOf('.');
		String extension = inFile.getPath().substring(extensionStart);
		String pathWithoutExtension = inFile.getPath().substring(0, extensionStart);
		File outFile = new File(pathWithoutExtension + "_RRanonymized" + extension);

		try (
				BufferedReader br = new BufferedReader(new FileReader(inFile));
				BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
				) {
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
			List<Variant> distributionData = collectDistributionData(inFile, carePathIndex);
			line = br.readLine();
			while(line != null) {
				String outLine = "";
				if ((1.0 + r.nextInt(100))/100 > randomizationChance) {
					outLine = line + System.lineSeparator();
				} else {
					splitLine = line.split(";");
					for(int i = 0; i < carePathIndex; i++) {
						outLine = outLine + splitLine[i] + ";";
					}
					outLine = outLine + generatePath(splitLine[carePathIndex], distributionData, minSDOfMean);
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

	private static String generatePath(String string, List<Variant> distributionData, double minSDOfMean) {
		String generatedPath = null;
		for(Variant variant : distributionData) {
			generatedPath = variant.matchAndGenerate(string.split(":"), minSDOfMean);
			if(generatedPath != null) return generatedPath;
		}
		return generatedPath;
	}

	private static List<Variant> collectDistributionData(File inFile, int targetColumn) throws FileNotFoundException, IOException {
		List<Variant> variants = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(inFile))) {
			//Skipping first line
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
