package com.project0.lawrencedang;

import static com.project0.lawrencedang.ClientServerProtocol.RECEIVED;
import static com.project0.lawrencedang.ClientServerProtocol.REJECT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;

/**
 * A RegistrationHandler handles client attempts to register a new login name.
 * If the client supplies an unregistered and valid login name, the name will be added to the database
 */
public class RegistrationHandler extends LoginServerHandler {
    UserRepository userDao;

    /**
     * Creates a RegistrationHandler receiving input on the BufferedReader and sending messages on the PrintStream
     */
    public RegistrationHandler(BufferedReader reader, PrintStream writer)
    {
        super(reader, writer);
        userDao = new UserRepository();
    }

    /**
     * Responds to client name registration.
     */
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
                return;
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
                    return;
                }
            }
        }

    }
    
}