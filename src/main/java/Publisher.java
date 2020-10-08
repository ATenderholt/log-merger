import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Publisher extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(Publisher.class);

    private final Path path;
    private final Subscriber subscriber;
    private final BlockingQueue<LogEvent> logQueue = new ArrayBlockingQueue<>(1);
    private boolean isFinished = false;

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
            LOGGER.error("Problem reading from '{}': {}", path, e.getMessage());
        } catch (InterruptedException e) {
            LOGGER.error("Problem adding to queue from '{}': {}", path, e.getMessage());
        }

        isFinished = true;
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

    public int size() {
        return logQueue.size();
    }

    public boolean isFinished() {
        return isFinished;
    }
}
