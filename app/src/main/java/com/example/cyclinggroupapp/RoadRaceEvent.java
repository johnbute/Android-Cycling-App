package com.example.cyclinggroupapp;

public class RoadRaceEvent extends EventTypes{

    private int group; // Group number
    private int distance; // Distance in kilometers

    public RoadRaceEvent(int age, int pace, String level,int group, int distance) {
        super(age, pace, level);
        this.group = group;
        this.distance = distance;
    }
    // Setters and getters
    public int getGroup() {
        return group;
    }
    public void setGroup(int group) {
        this.group = group;
    }

    public int getDistance() {
        return distance;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }
}
