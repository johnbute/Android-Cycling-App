package com.example.cyclinggroupapp.Users;

public class Club {
    String clubname;
    String description;
    ClubOwner owner;
    public Club(String clubname, String description, ClubOwner owner){

        this.clubname = clubname;
        this.description = description;

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
}
