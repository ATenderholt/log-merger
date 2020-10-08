import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    // Need to set a max because there is a limit to number of open file handles
    private final static int MAX_FILES = 2048;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("No files specified.");
            System.exit(-1);
        }

        if (args.length > MAX_FILES) {
            System.err.println("Too many files.");
            System.exit(-1);
        }

        final Subscriber subscriber = new Subscriber();
        for (String filename : args) {
            final Path path = Paths.get(filename);
            final Publisher publisher = new Publisher(filename, path);
            subscriber.add(publisher);
            publisher.start();
        }

        subscriber.run();
    }
}
