import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class Publisher extends Thread {
    private final Path path;
    private final Subscriber subscriber;
    private final BlockingQueue<LogEvent> logQueue = new SynchronousQueue<>();

    Publisher(String id, Path path, Subscriber subscriber) {
        super(id);
        this.path = path;
        this.subscriber = subscriber;
        subscriber.subscribe(this);
    }

    @Override
    public void run() {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                logQueue.put(LogEvent.create(line));
            }
        } catch (IOException e) {
            System.err.println("Problem reading from '" + path + "': " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Problem adding to queue from '" + path + "': " + e.getMessage());
        }

        subscriber.unsubscribe(this);
    }

    public LogEvent readEvent() {
        try {
            return logQueue.take();
        } catch (InterruptedException exception) {
            subscriber.unsubscribe(this);
            return null;
        }
    }
}
