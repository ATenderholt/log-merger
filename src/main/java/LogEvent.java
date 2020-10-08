import java.time.Instant;

/**
 * POJO to store a Log event from a file.
 *
 * epochMilliseconds - self explanatory
 * event - full event (i.e. timestamp and message)
 * finished - whether it's an event to signal EOF
 */
public class LogEvent {
    private final long epochMilliSeconds;
    private final String event;
    private final boolean finished;

    static LogEvent create(String event) {
        final String[] infos = event.split(",");
        final long time = Instant.parse(infos[0]).toEpochMilli();
        return new LogEvent(time, event, false);
    }

    /**
     * Create a LogEvent to signal finished reading from file.
     */
    static LogEvent finished() {
        return new LogEvent(0L, null, true);
    }

    LogEvent(long epochMilliSeconds, String event, boolean finished) {
        this.epochMilliSeconds = epochMilliSeconds;
        this.event = event;
        this.finished = finished;
    }

    long getEpochMilliSeconds() {
        return epochMilliSeconds;
    }

    String getEvent() {
        return event;
    }

    boolean isFinished() {
        return finished;
    }
}
