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
        data = new HashMap<String, String>();

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
            // Reads the data one line at a time
            String line = fileReader.nextLine();

            // Trims whitespace
            String processedLine = line.trim();

            // Store the data with line number and full string for future
            // processing
            data.put("Line " + lineNumber, processedLine);

            lineNumber++;
        }

        fileReader.close();
        print();
    }

    private void print() {
        System.out.println(data.toString());
    }
}
