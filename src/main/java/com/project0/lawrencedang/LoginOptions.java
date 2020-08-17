package com.project0.lawrencedang;

public enum LoginOptions {
    LOGIN, REGISTER, LEADERBOARD, INVALID;

    public static LoginOptions fromString(String s)
    {
        try
        {
            return LoginOptions.valueOf(s);
        }
        catch(IllegalArgumentException e)
        {
            return INVALID;
        }
    }
}