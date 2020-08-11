package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;


public class ClientApp {


    private static Socket waitForConnection() throws InterruptedException
    {
        Socket tempSocket = new Socket();
        try
        {
            tempSocket.connect(new InetSocketAddress("localhost", 5643), 0);
        } 
        catch (IOException e) 
        {
            Thread.sleep(200);
        }
        return tempSocket;
    }

    private static void joinGame(BufferedReader reader, PrintStream writer, BufferedReader uinput) throws IOException
    {
        System.out.println("Joining game");
        Client client = new Client(reader, writer, uinput);
        client.run();
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
        System.out.println("Connected to server");

        try
        {
            socket.setSoTimeout(20000);
        }
        catch(SocketException e)
        {
            System.err.println("Problem when setting socket timeout.");
            System.exit(1);
        }

        BufferedReader reader = null;
        PrintStream writer = null;
        BufferedReader userReader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintStream(socket.getOutputStream());
            userReader = new BufferedReader(new InputStreamReader(System.in));

        }
        catch(IOException e)
        {
            System.err.println("Problem getting socket streams.");
            System.exit(1);
        }

        try
        {
            joinGame(reader, writer, userReader);
        }
        catch(IOException e)
        {
            System.err.println("There was a problem communicating with the server.");
        }
        catch(Exception e)
        {
            System.err.println("");
        }
        finally
        {
            try
            {
                socket.close();
            }
            catch(IOException e)
            {
                System.err.println("Problem closing socket");
            }
        }


    
    }
}