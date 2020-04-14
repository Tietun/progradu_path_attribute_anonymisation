package distributioncollector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DistributionCollector {

	public static void main(String[] args){
		if(args.length == 0) {
			System.out.println("No file argument");
			return;
		}
		
		File inFile = new File(args[0]);

		List<Variant> variants = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(inFile))){
			
			String line = br.readLine();
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
				boolean matched = false;
				String[] pathElements = line.split(";")[carePathIndex].split(":");
				for(Variant variant : variants) {
					if(variant.matchPathAndAdd(pathElements)) {
						matched = true;
						break;
					}
				}
				if(!matched) {
					variants.add(new Variant(pathElements));
				}

				line = br.readLine();
			}
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		
	}
}
