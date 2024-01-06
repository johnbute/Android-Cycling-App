package com.example.cyclinggroupapp;

public abstract class EventTypes {
    protected int age;
    protected int pace;
    protected String level;

    public EventTypes(int age, int pace, String level) {
        this.age = age;
        this.pace = pace;
        this.level = level;
    }

    // Getters and setters for attributes
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getPace() {
        return pace;
    }

    public void setPace(int pace) {
        this.pace = pace;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}