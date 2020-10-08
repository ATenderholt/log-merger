import java.time.Instant;

/**
 * POJO to store a Log event from a file.
 *
 * epochMilliseconds - self explanatory
 * event - full event (i.e. timestamp and message)
 */
public class LogEvent {
    private final long epochMilliSeconds;
    private final String event;

    static LogEvent create(String event) {
        final String[] infos = event.split(",");
        final long time = Instant.parse(infos[0]).toEpochMilli();
        return new LogEvent(time, event);
    }

    LogEvent(long epochMilliSeconds, String event) {
        this.epochMilliSeconds = epochMilliSeconds;
        this.event = event;
    }

    long getEpochMilliSeconds() {
        return epochMilliSeconds;
    }

    String getEvent() {
        return event;
    }
}
