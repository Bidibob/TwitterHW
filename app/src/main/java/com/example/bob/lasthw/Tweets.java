package com.example.bob.lasthw;

/**
 * Created by Bob on 12/11/2016.
 */

public class Tweets {
    String message;
    String user;
    String timestamp;


    public Tweets() {

    }

    public Tweets(String message, String user, String timestamp) {
        this.message = message;
        this.user = user;
        this.timestamp = timestamp;
    }
}
