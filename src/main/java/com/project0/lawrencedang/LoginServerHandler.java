package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import static com.project0.lawrencedang.ClientServerProtocol.READY;
import static com.project0.lawrencedang.ClientServerProtocol.REJECT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Superclass for login server handlers.
 */
public abstract class LoginServerHandler implements Runnable {
    protected BufferedReader reader;
    protected PrintStream writer;
    static final Logger logger = LoggerFactory.getLogger(LoginServerHandler.class);

    public LoginServerHandler(BufferedReader reader, PrintStream writer) {
        this.reader = reader;
        this.writer = writer;
    }

    /**
     * Prompt the client for a username, looping until the client enters a valid username.
     */
    public Username getUsername() throws IOException
    {
        Username username = new Username();
        while(!username.isValidUsername())
        {
            writer.print(READY);
            String name = reader.readLine();
            if(name == null)
            {
                throw new IOException();
            }
            username = new Username(name);
            if(!username.isValidUsername())
            {
                logger.debug("Username " + username.getString() +" invalid");
                writer.print(REJECT);
            }
        }
        logger.debug("got username");
        return username;
    }
    
}
