/**
 * Copyright (c) 2024 Keigen Godlaski
 * With coding assistance from ChatGPT
 */

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
            value = value.substring(0, value.length() - 1).trim();

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
        try {
            // Split the line by "-=" to get the key and value parts
            String[] parts = line.split("-=", 2);

            // Extract and trim the key and value
            String key = parts[0].trim();
            String value = parts[1].trim();
            value = value.substring(0, value.length() - 2);

            // Check if the variable (key) exists in the map
            if (!data.containsKey(key)) {
                throw new Exception(" ");
            }

            // Retrieve the current value associated with the key
            int currentValue = Integer.parseInt(data.get(key));

            // Parse the value to be subtracted
            int subtractedValue = Integer.parseInt(value);

            // Perform the subtraction
            int newValue = currentValue - subtractedValue;

            // Update the map with the new value
            data.put(key, String.valueOf(newValue));
        } catch (Exception e) {
            System.out.println("RUNTIME ERROR: line " + lineNumber);
            System.exit(1);
        }
    }

    private void handleTimesEquals(String line) {
        String[] parts = line.split("\\*=", 2);
        try {
            // Get the variable name on the left-hand side of "*="
            String variableName = parts[0].trim();

            // Get the value associated with the variable from the data map
            int start = Integer.parseInt(data.get(variableName));

            // Retrieve the current value associated with the variableName
            if (!data.containsKey(variableName)) {
                throw new Exception("");
            }

            // Trim and check the right-hand side of "*="
            String rightSide = parts[1].trim();
            rightSide =
                    rightSide.substring(0, rightSide.length() - 1).trim();

            int multiplier;

            // Check if the right side is a variable or a direct integer value
            if (data.containsKey(rightSide)) {
                // It's a variable, so get its value from the data map
                multiplier = Integer.parseInt(data.get(rightSide));
            } else {
                // It's a direct integer value
                multiplier = Integer.parseInt(rightSide);
            }

            // Perform the multiplication
            int calc = start * multiplier;

            // Store the result back in the data map
            data.put(variableName, Integer.toString(calc));
        } catch (Exception e) {
            System.out.println("RUNTIME ERROR: line " + lineNumber);
            System.exit(1);
        }
    }

    private void handlePlusEquals(String line) {
        try {
            // Split the line by "+=" to get the key and value parts
            String[] parts = line.split("\\+=", 2);

            // Extract and trim the key and value
            String key = parts[0].trim(); // The variable name (e.g., A)
            String addedValue = parts[1].trim(); // The value to be added

            // Remove trailing semicolon from value
            addedValue = addedValue.substring(0, addedValue.length() - 1).trim();

            // Retrieve the current value associated with the key
            String currentValue = data.get(key);

            // Check if addedValue is a variable name (i.e., another key in the map)
            if (data.containsKey(addedValue)) {
                addedValue = data.get(addedValue);
            }

            boolean isCurrentValueString = currentValue.contains("\"");
            boolean isAddedValueString = addedValue.contains("\"");

            // Check for type mismatch
            if (isCurrentValueString != isAddedValueString) {
                throw new Exception("");
            }

            if (isCurrentValueString) {  // Both are strings
                // Remove quotes from the string values
                String processedCurrentValue = currentValue.substring(1,
                        currentValue.length() - 1);
                String processedAddedValue = addedValue.substring(1,
                        addedValue.length() - 1);

                // Concatenate the strings and add quotes
                String newValue =
                        "\"" + processedCurrentValue + processedAddedValue + "\"";
                data.put(key, newValue);
            } else {  // Both are integers
                int currentIntValue = Integer.parseInt(currentValue);
                int addedIntValue = Integer.parseInt(addedValue);
                int newValue = currentIntValue + addedIntValue;
                data.put(key, String.valueOf(newValue));
            }

        } catch (Exception e) {
            System.out.println("RUNTIME ERROR: line " + lineNumber);
            System.exit(1);
        }
    }

    private void handleFor(String line) {
    }

    private void handlePrint(String text) {
        String[] parts = text.split(" ");
        System.out.println(parts[1] +  "=" + data.get(parts[1]));
    }
}
