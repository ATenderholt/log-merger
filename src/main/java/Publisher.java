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
    private final BlockingQueue<LogEvent> logQueue = new ArrayBlockingQueue<>(1);

    Publisher(String id, Path path) {
        super(id);
        this.path = path;
    }

    @Override
    public void run() {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                logQueue.put(LogEvent.create(line));
            }

            logQueue.put(LogEvent.finished());
        } catch (IOException e) {
            LOGGER.error("Problem reading from '{}': {}", path, e.getMessage());
        } catch (InterruptedException e) {
            LOGGER.error("Problem adding to queue from '{}': {}", path, e.getMessage());
        }
    }

    public LogEvent readEvent() {
        try {
            return logQueue.take();
        } catch (InterruptedException exception) {
            try {
                logQueue.put(LogEvent.finished());
            } catch (InterruptedException e) {
                LOGGER.error("Problem adding to queue from '{}': {}", path, e.getMessage());
            }
            return null;
        }
    }

    public int size() {
        return logQueue.size();
    }
}
