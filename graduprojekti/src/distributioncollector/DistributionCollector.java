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

public class DistributionCollector {

	public static void main(String[] args){
		System.out.println("DistributionCollector uses the following Apache v2 -license libraries:");
		System.out.println("Apache Commons Math v3.2: http://commons.apache.org/proper/commons-math/");
		System.out.println("Jackson 2.10.0 (including core, annotations and databind): https://github.com/FasterXML/jackson");
		System.out.println("The Apache v2 license can be found at: https://www.apache.org/licenses/LICENSE-2.0");
		
		if(args.length == 0) {
			System.out.println("No file argument");
			return;
		}
		
		File inFile = new File(args[0]);
		int extensionStart = inFile.getPath().lastIndexOf('.');
		String extension = inFile.getPath().substring(extensionStart);
		String pathWithoutExtension = inFile.getPath().substring(0, extensionStart);
		File variantOutFile = new File(pathWithoutExtension + "_variants.json");
		File timelessOutFile = new File(pathWithoutExtension + "_timeless" + extension);

		ObjectMapper mapper = new ObjectMapper();
		
		List<Variant> variants = new ArrayList<>();
		
		try (
				BufferedReader br = new BufferedReader(new FileReader(inFile));
				BufferedWriter variantWriter = new BufferedWriter(new FileWriter(variantOutFile));
				BufferedWriter timelessWriter = new BufferedWriter(new FileWriter(timelessOutFile))
			){
			
			String line = br.readLine();
			timelessWriter.write(line + System.lineSeparator());
			String[] splitLine = line.split(";");
			int carePathIndex = -1;
			for(int i = 0; i < splitLine.length; i++) {
				if(splitLine[i].equals("Path")) {
					carePathIndex = i;
					break;
				}
			}
			line = br.readLine();
			while(line != null) {
				splitLine = line.split(";");
				String timelessLine = "";
				for(int i = 0; i < carePathIndex; i++) {
					timelessLine = timelessLine + splitLine[i] + ";";
				}
				String[] pathElements = splitLine[carePathIndex].split(":");
				Variant matchedVariant = null;
				for(Variant variant : variants) {
					if(variant.matchPathAndAdd(pathElements)) {
						matchedVariant = variant;
						break;
					}
				}
				if(matchedVariant == null) {
					matchedVariant = new Variant(pathElements);
					variants.add(matchedVariant);
				}
				timelessLine = timelessLine + matchedVariant.getTimelessPath();
				
				for(int i = carePathIndex + 1; i < splitLine.length; i++) {
					timelessLine = timelessLine + splitLine[i] + ";";
				}
				timelessLine = timelessLine.substring(0, timelessLine.length() - 1) + System.lineSeparator();
				timelessWriter.write(timelessLine);
				variantWriter.write(mapper.writeValueAsString(matchedVariant.getElementListWrapper()) + ";");
				line = br.readLine();
			}
			br.close();
			timelessWriter.close();
			variantWriter.close();
			System.out.println("Ready");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		
	}
}
