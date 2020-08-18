package com.project0.lawrencedang;

public class Token {
    private int userId;
    private String token;

    public Token(int userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    
}