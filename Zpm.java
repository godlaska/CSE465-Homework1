import java.io.File;

public class Zpm {
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
