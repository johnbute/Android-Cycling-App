package com.example.cyclinggroupapp;
import java.io.Serializable;

public class Account implements Serializable {

    String AccountEmail, AccountUsername, AccountRole;

    public String getAccountEmail() {
        return AccountEmail;
    }

    public String getAccountUsername() {
        return AccountUsername;
    }

    public String getAccountRole() {
        return AccountRole;
    }
}

