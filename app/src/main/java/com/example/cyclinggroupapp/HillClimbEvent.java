package com.example.cyclinggroupapp;

public class HillClimbEvent extends EventTypes{
    private int height; // Height in kilometers
    public HillClimbEvent(int age, int pace, String level) {
        super(age, pace, level);
        this.height = height;
    }
    public int getHeight(){
        return height;
    }
    public void setHeight(int height){
        this.height = height;
    }
}
