package utils;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import javafx.util.Pair;

public class TestDataFileReader {

    private static final String inputFolderPath = "src/test/resources/In (public)";
    private static final String outputFolderPath = "src/test/resources/Out (public)";

    public static ArrayList<Pair<String, String>> readInputOutputPairs() {
        ArrayList<String> inputs = readInputs(inputFolderPath);
        ArrayList<String> outputs = readOutputs(outputFolderPath);
        ArrayList<Pair<String, String>> inputOutputPairs = new ArrayList<Pair<String, String>>();
        int minSize = Math.min(inputs.size(), outputs.size());
        for (int i = 0; i < minSize; i++) {
            inputOutputPairs.add(new Pair<String, String>(inputs.get(i), outputs.get(i)));
        }
        return inputOutputPairs;
    }

    private static ArrayList<String> readInputs(String folderPath) {
        ArrayList<String> inputs = new ArrayList<String>();
        File folder = new File(folderPath);
        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().startsWith("in")) {
                try {
                    Scanner scanner = new Scanner(file);
                    StringBuilder sb = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        sb.append(scanner.nextLine());
                        sb.append("\n");
                    }
                    inputs.add(sb.toString().trim());
                    scanner.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return inputs;
    }

    private static ArrayList<String> readOutputs(String folderPath) {
        ArrayList<String> outputs = new ArrayList<String>();
        File folder = new File(folderPath);
        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().startsWith("out")) {
                try {
                    Scanner scanner = new Scanner(file);
                    StringBuilder sb = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        sb.append(scanner.nextLine());
                        sb.append("\n");
                    }
                    outputs.add(sb.toString().trim());
                    scanner.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return outputs;
    }


}
