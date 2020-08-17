package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import static com.project0.lawrencedang.ClientServerProtocol.READY;
import static com.project0.lawrencedang.ClientServerProtocol.REJECT;

public abstract class LoginServerHandler implements Runnable {
    protected BufferedReader reader;
    protected PrintStream writer;

    public LoginServerHandler(BufferedReader reader, PrintStream writer) {
        this.reader = reader;
        this.writer = writer;
    }

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
                System.err.println("Username " + username.getString() +" invalid");
                writer.print(REJECT);
            }
        }
        System.out.println("got username");
        return username;
    }
    
}
