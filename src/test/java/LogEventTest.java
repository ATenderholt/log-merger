import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LogEventTest {
    @Test
    public void testParse() {
        final LogEvent event = LogEvent.create("2016-12-20T19:00:45Z, Server A started.");

        assertEquals(event.getEpochMilliSeconds(), 1482260445000L);
        assertEquals(event.getEvent(), "2016-12-20T19:00:45Z, Server A started.");
    }
}
