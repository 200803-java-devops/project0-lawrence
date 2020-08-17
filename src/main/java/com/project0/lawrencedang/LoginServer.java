package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import static com.project0.lawrencedang.ClientServerProtocol.READY;
import static com.project0.lawrencedang.ClientServerProtocol.REJECT;

public class LoginServer extends Server {

    public LoginServer(ServerSocket server)
    {
        super(server);
    }
    
    public void listen()
    {
        while(true)
        {
            BufferedReader reader = null;
            PrintStream writer = null;
            try
            {
                Socket socket = server.accept();
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintStream(socket.getOutputStream());
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
                System.err.println("Problem while reading username");
            }
            LoginOptions option = LoginOptions.fromString(uResponse);
            if( option != LoginOptions.INVALID)
            {
                switch(option)
                {
                    case LOGIN:
                        login(reader, writer);
                        break;
                    case REGISTER:
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
}