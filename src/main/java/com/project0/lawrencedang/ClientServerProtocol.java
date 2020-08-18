package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Utility class for Client-Server communication. Contains various constant strings and templates 
 * for the server to communicate to the client.
 */
public class ClientServerProtocol {
    /**
     * Generic message template
     */
    public static final String MESSAGE_TEMPLATE = "MSG|%s\n";
    /**
     * Template used for sending a representation of the game state to the client
     */
    public static final String STATE_TEMPLATE = "STATE|%s\n";
    /**
     * Used to prompt the client for input
     */
    public static final String READY = "READY|\n";
    /**
     * Used to indicate to the client that input was accepted
     */
    public static final String RECEIVED = "RECEIVED|\n";
    /**
     * Used to indicate to the client that input was rejected
     */
    public static final String REJECT = "REJECTED|\n";
    /**
     * Used to send a login token to the client
     */
    public static final String TOKEN_TEMPLATE = "TOKEN|%s\n";

    /**
     * Allows the client to wait until a ready prompt is received
     */
    public static void waitForReady(BufferedReader reader) throws IOException
    {
        while(!(reader.readLine().split("\\|")[0]).equals("READY"));
    }

    /**
     * Gets the substring of a server message to the left of the divider |
     */
    public static String getType(String s)
    {
        return s.contains("|")? s.split("\\|")[0] : "";
    }

    /**
     * Gets the substring of a servermessage ot the right of the divider |, if it exists
     */
    public static String getMessage(String s)
    {
        if(s.contains("|"))
        {
            String[] splitString = s.split("\\|");
            if(splitString.length < 2)
            {
                return "";
            }
            else
            {
                return splitString[1];
            }
        }
        return "";
    }

}