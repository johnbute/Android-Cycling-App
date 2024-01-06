package com.example.cyclinggroupapp;

import java.io.Serializable;

public class EventType implements Serializable {
    String EventType, EventDescription;

    public String getEventType() {
        return EventType;
    }

    public String getEventDescription() {
        return EventDescription;
    }
}
