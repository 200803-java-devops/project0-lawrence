package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable
{
    private Socket mySocket;
    private ThreadCommunicationChannel commChannel;
    public ConnectionHandler(Socket socket, ThreadCommunicationChannel comm)
    {
        if (socket == null)
        {
            throw new NullPointerException("Socket argument to ConnectionHandler cannot be null.");
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
            OutputStream outputStream = mySocket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            String message = null;
            while(message == null || message.compareTo("quit") != 0)
            {
                if(!bufferedReader.ready())
                {
                    System.out.println("sleeping");
                    Thread.sleep(500);
                }
                else
                {
                    message = bufferedReader.readLine().trim();
                    System.out.println(message);
                    outputStreamWriter.write(message +"\n");
                    outputStreamWriter.flush();
                }
            }
            this.mySocket.close();
        }
        catch (IOException e)
        {
            System.err.println("ConnectionHandler: Problem encountered when reading input stream from socket.");
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}