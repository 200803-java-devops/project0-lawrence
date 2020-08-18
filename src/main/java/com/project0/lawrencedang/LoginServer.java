package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import static com.project0.lawrencedang.ClientServerProtocol.READY;
import static com.project0.lawrencedang.ClientServerProtocol.REJECT;

/**
 * Entry point to the login server application.
 */
public class LoginServer extends Server {

    /**
     * Creates a new LoginServer with the specified server socket.
     */
    public LoginServer(ServerSocket server)
    {
        super(server);
    }
    
    /**
     * Listen for connections and handle client input
     */
    public void listen()
    {
        while(true)
        {
            BufferedReader reader = null;
            PrintStream writer = null;
            Socket socket = null;
            try
            {
                System.out.println("Waiting for connection");
                socket = server.accept();
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintStream(socket.getOutputStream());
                System.out.println("Client connected");
            }
            catch(IOException e)
            {
                System.err.println("Problem while waiting for connection.");
                return;
            }
            
            writer.print(READY);
            String uResponse = "";
            try
            {
                uResponse = reader.readLine().trim();
            }
            catch(IOException e)
            {
                System.err.println("Problem while reading user choice");
            }
            LoginOptions option = LoginOptions.fromString(uResponse);
            if( option != LoginOptions.INVALID)
            {
                switch(option)
                {
                    case LOGIN:
                        System.out.println("Client chose to login");
                        login(reader, writer);
                        break;
                    case REGISTER:
                        System.out.println("Client chose to register");
                        register(reader, writer);
                        break;
                    case LEADERBOARD:
                        sendLeaderboard(reader, writer);
                        break;
                    default:
                        System.err.println("Should never reach here.");
                        break;
                }
            }
            else
            {
                writer.print(REJECT);
            }
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

    private void login(BufferedReader reader, PrintStream writer)
    {
        LoginHandler handler = new LoginHandler(reader, writer);
        handler.run();
    }

    private void register(BufferedReader reader, PrintStream writer)
    {
        RegistrationHandler handler = new RegistrationHandler(reader, writer);
        handler.run();
    }

    private void sendLeaderboard(BufferedReader reader, PrintStream writer)
    {
        return;
    }

    public static void main(String[] args) {
        try(ServerSocket serverSock = new ServerSocket(3465))
        {
            LoginServer server = new LoginServer(serverSock);
            server.listen();
        }
        catch(IOException e)
        {
            System.err.println("Problem while starting server.");
        }
    }
}