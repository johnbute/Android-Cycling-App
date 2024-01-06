package com.example.cyclingapp.Clubs;

import com.example.cyclinggroupapp.Users.ClubOwner;

public class Club {

    private String clubname;
    private String description;
    private ClubOwner owner;
    public Club(String clubname, String description, ClubOwner owner){

        this.clubname = clubname;
        this.description = description;
        this.owner = owner;

    }

    public String getClubname(){
        return this.clubname;
    }

    public String getDescription(){
        return this.description;
    }

    public ClubOwner getOwner(){
        return this.owner;
    }

    public void setClubname(String clubname){
        this.clubname = clubname;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setOwner(ClubOwner owner){
        this.owner = owner;
    }
    
}
