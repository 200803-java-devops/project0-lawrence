package com.project0.lawrencedang;

public class User {
    private int userId;
    private String username;

    public User(int uid, String uname)
    {
        userId = uid;
        username = uname;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

}