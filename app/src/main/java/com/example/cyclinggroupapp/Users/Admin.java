package com.example.cyclinggroupapp.Users;

public class Admin extends Account {

    Admin() {
        super("admin", "admin", "");
    }

    public Club createClub(String name, String description){
        Club club = new Club(name, description, new ClubOwner("", "", ""));
        return club;
    }

    

}
