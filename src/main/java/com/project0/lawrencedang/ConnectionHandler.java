package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;

public class ConnectionHandler implements Runnable
{
    private Socket mySocket;
    private ThreadCommunicationChannel commChannel;
    public ConnectionHandler(Socket socket, ThreadCommunicationChannel comm)
    {
        if (socket == null || commChannel == null)
        {
            throw new NullPointerException("Arguments to ConnectionHandler cannot be null.");
        }
        this.mySocket = socket;
        this.commChannel = comm;
    }
    public void run()
    {
        try
        {
            InputStream inputStream = mySocket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            PrintStream printStream = new PrintStream(mySocket.getOutputStream());
            String message = null;
        }
        catch (IOException e)
        {
            System.err.println("ConnectionHandler: Problem encountered when reading input stream from socket.");
        }

    }
}