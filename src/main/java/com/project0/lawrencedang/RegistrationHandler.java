package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;

import static com.project0.lawrencedang.ClientServerProtocol.MESSAGE_TEMPLATE;
import static com.project0.lawrencedang.ClientServerProtocol.RECEIVED;
import static com.project0.lawrencedang.ClientServerProtocol.REJECT;

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
        while(registered == true)
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
            
            try
            {
                registered = userDao.isRegistered(username.getString());
            }
            catch(SQLException e)
            {
                System.err.println("Problem checking if username is registered");
            }
            if(registered)
            {
                System.out.println("Client tried to register a registered name");
                writer.print(REJECT);
            }
            else
            {
                try
                {
                    userDao.put(username.getString());
                    System.out.println("Created new user " + username.getString());
                    writer.print(RECEIVED);
                    return;
                }
                catch(SQLException e)
                {
                    System.err.println("Failed to register new user.");
                }
            }
        }

    }
    
}