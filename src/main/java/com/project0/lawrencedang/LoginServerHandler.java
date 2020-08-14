package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import static com.project0.lawrencedang.ClientServerProtocol.READY;

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
            username = new Username(reader.readLine());
        }
        return username;
    }
    
}
