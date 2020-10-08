import java.util.*;

public class Subscriber {
    final Set<Publisher> publishers = Collections.synchronizedSet(new HashSet<>());
    final SortedMap<Long, PublisherEvent> events = new TreeMap<>();

    void subscribe(Publisher publisher) {
        publishers.add(publisher);
    }

    void unsubscribe(Publisher publisher) {
        publishers.remove(publisher);
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
            if (publishers.contains(event.getPublisher())) {
                storeFromPublisher(event.getPublisher());
            }
        }
    }

    private void fill() {
        for (Publisher publisher : publishers) {
            storeFromPublisher(publisher);
        }
    }

    private void storeFromPublisher(final Publisher publisher) {
        final LogEvent logEvent = publisher.readEvent();
        final PublisherEvent publisherEvent = new PublisherEvent(publisher, logEvent.getEvent());
        events.put(logEvent.getEpochMilliSeconds(), publisherEvent);
    }
}
