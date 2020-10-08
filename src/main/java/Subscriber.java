import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Subscriber {
    private final static Logger LOGGER = LoggerFactory.getLogger(Subscriber.class);

    final Set<Publisher> publishers = Collections.synchronizedSet(new HashSet<>());
    final SortedMap<Long, PublisherEvent> events = new TreeMap<>();

    void add(Publisher publisher) {
        publishers.add(publisher);
    }

    void run() {
        fill();

        while(events.size() > 0) {
            final Long time = events.firstKey();
            final PublisherEvent event = events.remove(time);
            if (event == null) {
                throw new IllegalStateException("Got null PublisherEvent for time = " + time);
            }

            System.out.println(event.getEvent());

            // If publisher is still subscribed, it still should have data so re-read from it
            final Publisher publisher = event.getPublisher();
            if (publishers.contains(publisher)) {
                LOGGER.debug("Publisher '{}' has size {}", publisher.getName(), publisher.size());
                storeFromPublisher(event.getPublisher());
            }
        }
    }

    private void fill() {
        synchronized (publishers) {
            for (Publisher publisher : publishers) {
                storeFromPublisher(publisher);
            }
        }
    }

    private void storeFromPublisher(final Publisher publisher) {
        final LogEvent logEvent = publisher.readEvent();
        if (logEvent.isFinished()) {
            publishers.remove(publisher);
            return;
        }

        final PublisherEvent publisherEvent = new PublisherEvent(publisher, logEvent.getEvent());
        events.put(logEvent.getEpochMilliSeconds(), publisherEvent);
    }
}
