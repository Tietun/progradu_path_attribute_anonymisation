package timestampnormalizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import utils.LogLevel;
import utils.Logger;

public class TimestampNormalizer {
	private static final Logger LOG = new Logger(LogLevel.DEBUG);

	public static void main(String[] args) {

		if (args.length < 1) {
			LOG.critical("Missing arguments. Please give argument source data");
			return;
		}

		File dataFile = new File(args[0]);
		File normalizedFile = null;
		int extensionStart = dataFile.getPath().lastIndexOf('.');
		try {
			String extension = dataFile.getPath().substring(extensionStart);
			String pathWithoutExtension = dataFile.getPath().substring(0, extensionStart);
			normalizedFile = new File(pathWithoutExtension + "_normalized" + extension);
		} catch (Exception e) {
			LOG.critical("Unexpected structure of argument", new IllegalArgumentException(e));
			return;
		}

		try (BufferedReader dataReader = new BufferedReader(new FileReader(dataFile));
				BufferedWriter normalizedWriter = new BufferedWriter(new FileWriter(normalizedFile))) {

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

			String outLine = "";
			for (int i = 0; i < splitLine.length; i++) {
				if ((i != startTimeIndex) && (i != carePathIndex))
					outLine = outLine + splitLine[i] + ";";
			}
			outLine = outLine + "Activity;StartTime;EndTime" + System.lineSeparator();

			normalizedWriter.write(outLine);

			line = dataReader.readLine();
			while (line != null) {
				splitLine = line.split(";");

				String lineStart = "";
				for (int i = 0; i < splitLine.length; i++) {
					if ((i != startTimeIndex) && (i != carePathIndex))
						lineStart = lineStart + splitLine[i] + ";";
				}

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

	private static List<String> getTimeStampStrings(String startTimeString, String pathString) {
		String[] splitStart = startTimeString.split(" ");
		String[] splitDate = splitStart[0].split("-");
		String[] splitTime = splitStart[1].split(":");
		LocalDateTime startTime = LocalDateTime.of(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]),
				Integer.parseInt(splitDate[2]), Integer.parseInt(splitTime[0]), Integer.parseInt(splitTime[1]),
				Integer.parseInt(splitTime[2]));
		String[] splitPath = pathString.split(":");
		List<String> lines = new ArrayList<String>();
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
