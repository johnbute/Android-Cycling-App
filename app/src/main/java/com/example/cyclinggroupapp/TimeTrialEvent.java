package com.example.cyclinggroupapp;

public class TimeTrialEvent extends EventTypes{
    public int time; //Time in seconds
    public TimeTrialEvent(int age, int pace, String level) {
        super(age, pace, level);
        this.time = time;
    }

    //Setter and getter
    public int getTime() {
        return time;
    }
    public void setTime(int time) {
        this.time = time;
    }
}
