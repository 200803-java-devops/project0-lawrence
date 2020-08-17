package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.nio.Buffer;

public class ClientServerProtocol {
    public static final String MESSAGE_TEMPLATE = "MSG|%s\n";
    public static final String STATE_TEMPLATE = "STATE|%s\n";
    public static final String READY = "READY|\n";
    public static final String RECEIVED = "RECEIVED|\n";
    public static final String REJECT = "REJECTED|\n";
    public static final String TOKEN_TEMPLATE = "TOKEN|%s\n";

    public static void waitForReady(BufferedReader reader) throws IOException
    {
        while(!(reader.readLine().split("\\|")[0]).equals("READY"));
    }

    public static String getType(String s)
    {
        return s.contains("|")? s.split("\\|")[0] : "";
    }

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