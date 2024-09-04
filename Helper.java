/**
 * Copyright (c) 2024 Keigen Godlaski
 * With coding assistance from ChatGPT
 * @version 1.0
 * Date: 09/04/2024
 * The Helper class provides utility functions for processing
 * and manipulating lines of data from a file. The class reads the file,
 * processes each line, and decodes different types of operations such as
 * traditional arithmetic, setting variables, for-loops, and print statements.
 */

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Helper {
    /**
     * A map to store the processed data from the file.
     * The key is the variable name, and the value is the corresponding value.
     */
    HashMap<String, String> data;
    /**
     * The file that is being processed.
     */
    File loadedFile;

    Scanner fileReader;

    /**
     * Keeps track of the current line number being processed.
     */
    int lineNumber;

    /**
     * Constructs a Helper object that processes the specified file.
     * The file is read line by line, and the contents are processed and stored
     * in the data map.
     *
     * @param file the file to be processed.
     */
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

    /**
     * Processes the file line by line. Each line is trimmed of whitespace
     * and then decoded to handle different operations.
     */
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

    /**
     * Decodes a line of input and determines which operation to perform
     * based on its content.
     * Supported operations include setting variables, arithmetic operations,
     * and print statements.
     *
     * @param line the input line to be decoded and processed.
     */
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

    /**
     * Handles subtraction with assignment operations e.g. -=
     *
     * @param line the input line to process, expected to be in the format
     *             "key -= value;"
     * @throws Exception if there are issues processing the line.
     */
    private void handleMinusEquals(String line) {
        try {
            // Split the line by "-=" to get the key and value parts
            String[] parts = line.split("-=", 2);

            // Extract and trim the key and value
            String key = parts[0].trim();
            String value = parts[1].trim();

            // Remove semicolon
            if (value.endsWith(";")) {
                value = value.substring(0, value.length() - 1).trim();
            }

            // Check if the variable (key) exists in the map
            if (!data.containsKey(key)) {
                throw new Exception(" ");
            }

            // Retrieve the current value associated with the key
            int currentValue = Integer.parseInt(data.get(key));

            // Check if the value to subtract is another variable or a
            // direct integer
            int subtractedValue;
            if (data.containsKey(value)) {
                // If the value is another variable
                subtractedValue = Integer.parseInt(data.get(value));
            } else {
                // If the value is a direct integer
                subtractedValue = Integer.parseInt(value);
            }

            // Perform the subtraction
            int newValue = currentValue - subtractedValue;

            // Update the map with the new value
            data.put(key, String.valueOf(newValue));
        } catch (Exception e) {
            System.out.println("RUNTIME ERROR: line " + lineNumber);
            System.exit(1);
        }
    }

    /**
     * Handles multiplication with assignment operations e.g. *=
     *
     * @param line the input line to process, expected to be in the format
     *             "key *= value;"
     * @throws Exception if there are issues processing the line.
     */
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

    /**
     * Handles addition with assignment operations e.g. +=
     *
     * @param line the input line to process, expected to be in the format
     *             "key += value;"
     * @throws Exception if there are issues processing the line.
     */
    private void handlePlusEquals(String line) {
        try {
            // Split the line by "+=" to get the key and value parts
            String[] parts = line.split("\\+=", 2);

            // Extract and trim the key and value
            String key = parts[0].trim(); // The variable name (e.g., A)
            String addedValue = parts[1].trim(); // The value to be added

            // Remove trailing semicolon from value
            addedValue =
                    addedValue.substring(0, addedValue.length() - 1).trim();

            // Retrieve the current value associated with the key
            String currentValue = data.get(key);

            // Check if addedValue is a variable name (i.e., another key in
            // the map)
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
                        "\"" + processedCurrentValue + processedAddedValue +
                                "\"";
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

    /**
     * Handles "FOR" loop operations, simulating a simple loop based on a
     * specific format.
     * The input line is expected to contain a "FOR" loop instruction in the
     * format: FOR i = start TO end;. It runs the specified operation(s)
     * until 'i' is iterated the correct amount.
     *
     * @param line the input line to process, expected to contain a "FOR" loop.
     * @throws Exception if there are issues processing the line.
     */
    private void handleFor(String line) {
        try {
            String[] parts = line.split(" ", 3);
            int iterations = Integer.parseInt(parts[1].trim());

            // Extract the body of the loop (excluding "FOR" and the
            // iteration number)
            String loopBody = parts[2].substring(0,
                    parts[2].indexOf("ENDFOR")).trim();

            // Split the loop body into individual statements by semicolon
            String[] statements = loopBody.split(";");

            // Execute the loop the specified number of times
            for (int i = 0; i < iterations; i++) {
                for (String statement : statements) {
                    statement = statement.trim();
                    if (!statement.isEmpty()) {
                        // Decode and execute each statement
                        // Adding semicolon to simulate proper statement ending
                        decodeLine(statement + ";");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("RUNTIME ERROR: line " + lineNumber);
            System.exit(1);
        }
    }

    /**
     * Handles the "PRINT" operation, which outputs the value of a variable
     * The input line is expected to start with "PRINT" followed by a
     * variable name or a string.
     * If the input is a variable, its value is retrieved from the data map
     * and printed.
     * If the input is intended to be a string (enclosed in double quotes), the
     * string is printed directly.
     *
     * @param line the input line to process, expected to start with "PRINT ".
     */
    private void handlePrint(String line) {
        String[] parts = line.split(" ");
        System.out.println(parts[1] +  "=" + data.get(parts[1]));
    }
}
