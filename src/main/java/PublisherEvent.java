/**
 * POJO to store event and source publisher in a Map so we know which file to read from
 * next.
 */
public class PublisherEvent {
    private final Publisher publisher;
    private final String event;

    PublisherEvent(Publisher publisher, String event) {
        this.publisher = publisher;
        this.event = event;
    }

    Publisher getPublisher() {
        return publisher;
    }

    String getEvent() {
        return event;
    }
}
