package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConnectionHandler implements Runnable
{
    private Socket mySocket;
    public ConnectionHandler(Socket socket)
    {
        if (socket == null)
        {
            throw new NullPointerException("Socket argument to ConnectionHandler cannot be null.");
        }
        this.mySocket = socket;
    }
    public void run()
    {
        try
        {
            InputStream inputStream = mySocket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            System.out.println(bufferedReader.readLine());
            System.out.println("Done.");
            this.mySocket.close();
        }
        catch (IOException e)
        {
            System.err.println("ConnectionHandler: Problem encountered when reading input stream from socket.");
        }
    }
}