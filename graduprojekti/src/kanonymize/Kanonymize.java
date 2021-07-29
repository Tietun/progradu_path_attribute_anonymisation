package kanonymize;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Kanonymize {

    public static ArrayList<ArrayList<String>> readCsvColumns(String filePath) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        String line;
        ArrayList<ArrayList<String>> columns = new ArrayList<>();
        int lineCount = 0;
        while((line = bufferedReader.readLine()) != null) {
            String[] cells = line.split(";");
            columns.add(lineCount, new ArrayList<>());
            for (String cell : cells) {
                columns.get(lineCount).add(cell);
            }
            ++lineCount;
        }
        return columns;
    }

    public static ArrayList<String> findColumnByName(ArrayList<ArrayList<String>> columns, String columnName) {
        int i = 0;
        while(true) {
            if(columns.get(0).get(i).equals(columnName))
                break;
            if(i >= columns.get(0).size())
                throw new ArrayIndexOutOfBoundsException("No such column");
            ++i;
        }

        ArrayList<String> selectedColumn = new ArrayList<>();
        for(var column : columns) {
            selectedColumn.add(column.get(i));
        }

        return selectedColumn;
    }

    public static void writeLineToFile() {

    }

    public static void anonymize(String filePath, String columnName, Integer k) throws IOException {
        ArrayList<ArrayList<String>> columns = readCsvColumns(filePath);
        ArrayList<String> column = findColumnByName(columns, columnName);
        column.remove(0);

        Map<String, Long> uniqueCount = column.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        var belowK = uniqueCount.entrySet().stream()
                .filter(entry -> entry.getValue() <= k)
                .collect(Collectors.toSet());


        File file = new File("out.txt");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));


        for(var element : belowK)
            column.set(column.indexOf(element.getKey()), element.getKey().replaceFirst("E[0-9]", "E#"));
        for(var element : column) {
            bufferedWriter.write(element);
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }

    public static void main(String[] args) {
        String columnName = "TimelessPath";
        String filePath = "C:\\Users\\tietun\\IdeaProjects\\untitled\\data100000_timelessNew.csv";
        int k = 2;

        //if(args.length < 3)
        //    return;
        for (int i = 0; i < args.length; ++i)
            switch (args[i]) {
                case "-f":
                    filePath = args[++i];
                    break;
                case "-c":
                    columnName = args[++i];
                    break;
                case "-k":
                    k = Integer.parseInt(args[++i]);
                    break;
                default:
                    break;
            }
        if(columnName == null || filePath == null || k <= 0)
            return;

        try {
            anonymize(filePath, columnName, k);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    }
