package timestampnormalizer;

import utils.LogLevel;
import utils.Logger;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Normalizes a file from path attribute format to normal csv
 * @author Erkka Nurmi
 *
 */
public class TimestampNormalizer {
	private static final Logger LOG = new Logger(LogLevel.DEBUG);

	public static void main(String[] args) {

		if (args.length < 1) {
			LOG.critical("Missing arguments. Please give argument source data");
			return;
		}

		File dataFile = new File(args[0]);
		File normalizedFile;
		int extensionStart = dataFile.getPath().lastIndexOf('.');
		try {
			String extension = dataFile.getPath().substring(extensionStart);
			String pathWithoutExtension = dataFile.getPath().substring(0, extensionStart);
			//This is where the normalized data will be saved
			normalizedFile = new File(pathWithoutExtension + "_normalized" + extension);
		} catch (Exception e) {
			LOG.critical("Unexpected structure of argument", new IllegalArgumentException(e));
			return;
		}

		//Reading data line by line and writing to output
		try (
				BufferedReader dataReader = new BufferedReader(new FileReader(dataFile));
				BufferedWriter normalizedWriter = new BufferedWriter(new FileWriter(normalizedFile))
			) {

			String line = dataReader.readLine();
			String[] splitLine = line.split(";");
			int startTimeIndex = -1;
			int carePathIndex = -1;
			for (int i = 0; i < splitLine.length; i++) {
				if (splitLine[i].equals("Path")) {
					carePathIndex = i;
				} else if (splitLine[i].equals("StartTime")) {
					startTimeIndex = i;
				}
			}

			StringBuilder outLine = new StringBuilder();
			for (int i = 0; i < splitLine.length; i++) {
				if ((i != startTimeIndex) && (i != carePathIndex))
					outLine.append(splitLine[i]).append(";");
			}
			outLine.append("Activity;StartTime;EndTime").append(System.lineSeparator());

			normalizedWriter.write(outLine.toString());

			line = dataReader.readLine();
			while (line != null) {
				splitLine = line.split(";");

				StringBuilder lineStart = new StringBuilder();
				for (int i = 0; i < splitLine.length; i++) {
					if ((i != startTimeIndex) && (i != carePathIndex))
						lineStart.append(splitLine[i]).append(";");
				}

				//Getting the normalized lines
				List<String> timestampStrings = getTimeStampStrings(splitLine[startTimeIndex],
						splitLine[carePathIndex]);

				for (String timeStampString : timestampStrings) {
					normalizedWriter.write(lineStart + timeStampString + System.lineSeparator());
				}

				line = dataReader.readLine();
			}
			dataReader.close();
			normalizedWriter.close();
			LOG.info("Ready");

		} catch (IOException e) {
			LOG.critical("A critical error has occurred: ", e);
		} catch (Exception e) {
			LOG.critical("A critical error has occurred: ", e);
			e.printStackTrace();
		}
	}

	/**
	 * Creates and returns normalized time stamp lines for a given path attribute value
	 * @param startTimeString Time of the first time stamp as String in format YYYY-MM-DD hh-mm-ss
	 * @param pathString Path attribute value to normalize
	 * @return List of normalized time stamps
	 */
	private static List<String> getTimeStampStrings(String startTimeString, String pathString) {
		String[] splitStart = startTimeString.split(" ");
		String[] splitDate = splitStart[0].split("-");
		String[] splitTime = splitStart[1].split(":");
		LocalDateTime startTime = LocalDateTime.of(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]),
				Integer.parseInt(splitDate[2]), Integer.parseInt(splitTime[0]), Integer.parseInt(splitTime[1]),
				Integer.parseInt(splitTime[2]));
		String[] splitPath = pathString.split(":");
		List<String> lines = new ArrayList<>();
		for (String pathElement : splitPath) {
			if (pathElement.charAt(0) == '(') {
				String minuteString = pathElement.replace("(", "").replace(")", "");
				startTime = startTime.plusMinutes(Long.parseLong(minuteString));
			} else {
				String[] splitElement = pathElement.split("\\(");
				String minuteString = splitElement[1].replace("(", "").replace(")", "");
				LocalDateTime endTime = startTime.plusMinutes(Long.parseLong(minuteString));
				lines.add(splitElement[0] + ";" + startTime.toString().replace("T", " ") + ":00;"
						+ endTime.toString().replace("T", " ") + ":00");
				startTime = endTime;
			}
		}
		return lines;
	}
}
