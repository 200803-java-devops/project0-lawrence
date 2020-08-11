package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientApp {

    private BufferedReader reader;
    private PrintStream writer;

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

    private void beginGame()
    {

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

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }


        
    }
}