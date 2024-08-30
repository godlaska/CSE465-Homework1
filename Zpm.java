import java.io.File;

/**
 * The Zpm class is an entry point for the compiler. It takes a .zpm file as a
 * command-line argument and passes it to the Helper class for the compiler
 * processing logic. If no arguments are provided or if the file cannot be
 * opened, the program will print an error message and exit.
 */
public class Zpm {
    /**
     * The main method of the program.
     *
     * @param args Command-line arguments. The first argument should be a
     *             .zpm file to be processed
     */
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Please input a file");
            System.exit(0);
        }
        try {
            File file = new File(args[0]);
            // Runs compiler logic
            new Helper(file);
        }
        catch (Exception e) {
            System.err.println("Cannot open file.");
            System.exit(0);
        }

    }
}
