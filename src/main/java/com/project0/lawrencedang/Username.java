package com.project0.lawrencedang;

public class Username {
    private String username;

    public Username()
    {
        username = "";
    }

    public Username(String s)
    {
        username = s;
    }

    public String getString()
    {
        return username;
    }

    public boolean isValidUsername()
    {
        return username!=null && username.length() < 12 && username.matches("^[a-zA-Z0-9]+");
    }
}