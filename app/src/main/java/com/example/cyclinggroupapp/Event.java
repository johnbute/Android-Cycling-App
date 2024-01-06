package com.example.cyclinggroupapp;

import java.io.Serializable;

//TO CHANGE, NEED TO ADD MORE VARIABLES
public class Event implements Serializable {

    String EventName, EventRegion, EventType, EventId, EventOwner;

    public String getEventName(){
        return EventName;
    }

    public String getEventRegion(){
        return EventRegion;
    }

    public String getEventType(){
        return EventType;
    }

    public String getEventId() { return EventId; }

    public String getEventOwner() {
        return EventOwner;
    }
}
