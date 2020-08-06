package com.project0.lawrencedang;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class Server {
    public static final int MAX_CONNECTIONS= 4;

    private ServerSocket server;
    private Thread gameThread;
    ThreadCommunicationChannel commChannel;

    public Server(int port) throws IOException
    {
        try 
        {
            server = new ServerSocket(8080);
            gameThread = null;
        }
        catch(IOException e)
        {
            System.err.println("Failed to start server");
            throw e;
        }
        commChannel = new ThreadCommunicationChannel();
    }

    public void listen()
    {
        Socket socket = null;
        while(true)
        {
            try
            {
                socket = server.accept();
            }
            catch (IOException e)
            {
                System.err.println("Problem while listening for connections.");
            }
            ConnectionHandler handler = new ConnectionHandler(socket, commChannel);
            gameThread = new Thread(handler);
            gameThread.start();
            /*if(connections == MAX_CONNECTIONS)
            {
                runGame();
            }*/

            break; // For testing single player
        }
    }


    public static void main(String[] args) {
        int port = 5643;
        Server gameServer = null;
        try
        {
            gameServer = new Server(port);
        }
        catch (IOException e)
        {
            System.exit(1);
        }
        System.out.println("Server started.");
        gameServer.listen();
        
    }
}
