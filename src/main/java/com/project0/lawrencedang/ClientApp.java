package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import static com.project0.lawrencedang.ClientServerProtocol.waitForReady;


/**
 * The entry point for the client application.
 */
public class ClientApp {


    private static Socket waitForConnection(int port) throws InterruptedException
    {
        Socket tempSocket = new Socket();
        try
        {
            tempSocket.connect(new InetSocketAddress("localhost", port), 0);
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

    private static String login(BufferedReader reader, PrintStream writer, BufferedReader userReader) throws IOException
    {
        waitForReady(reader);
        writer.println("LOGIN");
        String token = null;
        while(token == null)
        {
            waitForReady(reader);
            System.out.print("Enter your login name: ");
            String name = userReader.readLine();
            writer.println(name);
            String serverResponse = reader.readLine();
            if(ClientServerProtocol.getType(serverResponse).equals("REJECTED"))
            {
                System.out.println("Username was invalid or does not exist.");
            }
            else
            {
                if(ClientServerProtocol.getType(serverResponse).equals("TOKEN"))
                {
                    token = ClientServerProtocol.getMessage(serverResponse);
                    System.out.println("Successfully logged in.");
                }
                else
                {
                    System.err.println("Protocol misunderstood: received " + serverResponse);
                    System.exit(1);
                }
            }
        }
        return token;
    }

    private static void register(BufferedReader reader, PrintStream writer, BufferedReader userReader) throws IOException
    {
        waitForReady(reader);
        writer.println("REGISTER");
        boolean registered = false;
        while(!registered)
        {
            waitForReady(reader);
            System.out.print("Enter a name to register: ");
            String name = userReader.readLine();
            writer.println(name);
            String serverResponse = reader.readLine();
            if(ClientServerProtocol.getType(serverResponse).equals("REJECTED"))
            {
                System.out.println("Username was invalid or is already registered");
            }
            else
            {
                if(ClientServerProtocol.getType(serverResponse).equals("RECEIVED"))
                {
                    System.out.println("Successfully registered");
                    registered = true;
                }
                else
                {
                    System.err.println("Protocol misunderstood: received" + serverResponse);
                    System.exit(1);
                }
            }

        }
        
    }

    public static void main(String[] args) {
        int login_port = 3465;
        int game_port = 5643;
        Socket socket = null;
        BufferedReader reader = null;
        PrintStream writer = null;
        BufferedReader userReader = null;
        // Connect to login server and retrieve token
        while(socket == null)
        {
            try
            {
                Socket tempSocket = waitForConnection(login_port);
                if(tempSocket.isConnected())
                {
                    socket = tempSocket;
                }
            }
            catch (Exception e)
            {
                System.err.println("Failed to connect to login server.");
                System.exit(1);
            }
        }
        try
        {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintStream(socket.getOutputStream());
            userReader = new BufferedReader(new InputStreamReader(System.in));
        }
        catch(IOException e)
        {
            System.err.println("Problem while getting socket streams");
            System.exit(1);
        }

        try
        {
            System.out.println("Please enter an option\n1: Login\n2: Register");
            String uinput = "";
            String loginToken;
            while((uinput = userReader.readLine())!= null)
            {
                if(uinput.equals("1"))
                {
                    loginToken = login(reader, writer, userReader);
                    System.out.println(loginToken);
                    break;
                }
                else if(uinput.equals("2"))
                {
                    register(reader, writer, userReader);
                    System.out.println("Start the client again and login.");
                    System.exit(0);
                }
                else
                {
                    System.out.println("Please enter a valid option.");
                }
            }
            // wait for ready prompt
        }
        catch(IOException e)
        {
            System.err.println("Failed to commuincate with login server.");
        }
        try
        {
            socket.close();
        }
        catch(IOException e)
        {
            System.err.println("Problem closing socket to login server.");
        }
        
        socket = null;
        // Connect to game server
        while(socket == null)
        {
            try
            {
                Socket tempSocket = waitForConnection(game_port);
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
            e.printStackTrace();
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