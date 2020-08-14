package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import static com.project0.lawrencedang.ClientServerProtocol.MESSAGE_TEMPLATE;

public class RegistrationHandler extends LoginServerHandler {
    UserRepository userDao;

    public RegistrationHandler(BufferedReader reader, PrintStream writer)
    {
        super(reader, writer);
        userDao = new UserRepository();
    }

    public void run()
    {
        Username username = new Username();
        boolean registered = true;
        while(registered = true)
        {
            while(!username.isValidUsername())
            {
                try
                {
                    username = getUsername();
                }
                catch(IOException e)
                {
                    System.err.println("Problem while getting username from client");
                    return;
                }
            }
            
            registered = userDao.isRegistered(username.getString());
            if(registered)
            {
                writer.print(String.format(MESSAGE_TEMPLATE, "Username already exists"));
            }
            else
            {
                userDao.put(username.getString());
            }
        }

    }
    
}