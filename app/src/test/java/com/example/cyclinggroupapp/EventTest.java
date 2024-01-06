package com.example.cyclinggroupapp;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class EventTest {

    @Test
    public void testGetEventName() {
        Event event = new Event();
        event.EventName = "Sample Event";
        assertEquals("Sample Event", event.getEventName());
    }

    @Test
    public void testGetEventRegion() {
        Event event = new Event();
        event.EventRegion = "North";
        assertEquals("North", event.getEventRegion());
    }

    @Test
    public void testGetEventType() {
        Event event = new Event();
        event.EventType = "Conference";
        assertEquals("Conference", event.getEventType());
    }

}
