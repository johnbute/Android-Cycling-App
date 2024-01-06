package com.example.cyclingapp.Users;

import com.example.cyclinggroupapp.Users.Account;

public class Admin extends Account {

    public Admin(String username, String password, String email) {
        super(username, password, email);
    }

    public Admin(){
        super("admin","admin", null);
    }

    

}
