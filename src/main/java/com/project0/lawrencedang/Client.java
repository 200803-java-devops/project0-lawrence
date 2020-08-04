package com.project0.lawrencedang;

import java.lang.Thread;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {

    private static Socket waitForConnection() throws InterruptedException
    {
        Socket tempSocket = new Socket();
        try
        {
            tempSocket.connect(new InetSocketAddress("localhost", 8080), 0);
        } 
        catch (IOException e) 
        {
            Thread.sleep(200);
        }
        return tempSocket;
    }
    public static void main(String[] args) {
        // Wait until server found.
        Socket socket = null;
        while(socket == null)
        {
            try
            {
                Socket tempSocket = waitForConnection();
                if(tempSocket.isConnected())
                {
                    socket = tempSocket;
                }
            }
            catch (Exception e)
            {
                System.err.println("Failed to connect to server.");
                System.exit(1);
            }
        }
        
        
        try
        {
            OutputStream outputStream = socket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            outputStreamWriter.write("Hello!\n");
            outputStreamWriter.flush();
            socket.close();
        }
        catch (IOException e)
        {
            System.err.println("Problem while communicating with server.");
        }
            System.out.println("Done.");
    }
}