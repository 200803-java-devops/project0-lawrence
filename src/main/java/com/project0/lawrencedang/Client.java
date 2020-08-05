package com.project0.lawrencedang;

import java.lang.Thread;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

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
        
        Scanner scanner = new Scanner(System.in);
        try
        {
            OutputStream outputStream = socket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
            String message = null;
            InputStream inputStream = socket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while(message == null || message.compareTo("quit") != 0)
            {
                message = scanner.nextLine().trim();
                outputStreamWriter.write(message+"\n");
                outputStreamWriter.flush();
                if(message.compareTo("quit")!= 0)
                {
                    while(!bufferedReader.ready())
                    {
                        System.out.println("sleeping");
                        Thread.sleep(500);
                    }
                    System.out.println(bufferedReader.readLine());
                }
            }
            socket.close();
        }
        catch (IOException e)
        {
            System.err.println("Problem while communicating with server.");
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
            System.out.println("Done.");
    }
}