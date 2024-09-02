/**
 * Copyright (c) 2024 Keigen Godlaski
 */

import javax.sound.midi.SysexMessage;
import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Helper {
    HashMap<String, String> data;
    File loadedFile;
    Scanner fileReader;
    int lineNumber;

    public Helper(File file) {
        // Stores the lines in a hashmap for unordered access
        data = new HashMap<>();

        // Store the file location as an instance
        this.loadedFile = file;

        try {
            fileReader = new Scanner(loadedFile);
        } catch (Exception e) {
            System.out.println("Unable to load the file: " + loadedFile + ". " +
                    "Double check to make sure this file exists.");
        }

        process();
    }

    private void process() {
        lineNumber = 1;

        while (fileReader.hasNextLine()) {
            // Reads the data one line at a time and trims whitespace
            String processedLine = fileReader.nextLine().trim();

            // Decode line by line as it gets processed
            decodeLine(processedLine);

            lineNumber++;
        }

        fileReader.close();
    }

    private void decodeLine(String line) {
        // Check for PRINT or a FOR LOOP first
        if (line.startsWith("PRINT ")) {  // PRINT
            handlePrint(line);
        } else if (line.contains("FOR")) {  // FOR
            handleFor(line);
        } else if (line.contains("+=")) {  // PLUS EQUALS
            handlePlusEquals(line);
        } else if (line.contains("*=")) {  // TIMES EQUALS
            handleTimesEquals(line);
        } else if (line.contains("-=")) {  // MINUS EQUALS
            handleMinusEquals(line);
        } else if (line.contains("=")) {  // EQUALS
            handleEquals(line);
        }
    }

    /**
     * Processes a line of input to handle an assignment operation.
     * The line is expected to be in the format of "key = value;", where the
     * key is a variable name and the value can be an integer or a string
     *
     * @param line the input line to process, assumed to be in the format of
     *             "key = value;"
     * @throws Exception if there are issues processing the line.
     */
    private void handleEquals(String line) {
        try {
            // Split the line by '=' to get the key and value parts
            String[] parts = line.split("=", 2);

            String key = parts[0].trim(); // The variable name (e.g., A)
            String value = parts[1].trim(); // The value (e.g., 5 or "5")

            // Remove trailing semicolon from value
            if (value.endsWith(";")) {
                value = value.substring(0, value.length() - 1).trim();
            }

            // Check if the value starts and ends with a quote
            if (value.startsWith("\"") && value.endsWith("\"")) {
                // It's a string value, keep the quotes
                data.put(key, value);
            } else {
                // Assume it's an integer (or any other type)
                // Convert it to string and store in the map
                data.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMinusEquals(String line) {
    }

    private void handleTimesEquals(String line) {
    }

    private void handlePlusEquals(String line) {
    }

    private void handleFor(String line) {
    }

    private void handlePrint(String text) {
        String[] split = text.split(" ");
        System.out.println(split[1] +  "=" + data.get(split[1]));
    }
}
