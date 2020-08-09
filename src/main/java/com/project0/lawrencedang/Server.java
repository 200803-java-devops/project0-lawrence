package com.project0.lawrencedang;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server is a class that acts as the server for the multiplayer blackjack game.
 * It waits for a client(s) to connect then spawns one thread to handle communicating with the client
 * and one thread to handle the game logic.
 */
public class Server {
    public static final int MAX_CONNECTIONS= 4;

    private ServerSocket server;
    private Thread gameThread;
    private Thread handlerThread;


    /**
     * Creates a Server with the specified port.
     * @param port the port number to listen for connections on.
     * @throws IOException
     */
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
    }

    /**
     * Listens for incoming connections, then creates threads to handle client actions and game logic.
     */
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

            ThreadCommunicationChannel comm = new ThreadCommunicationChannel();
            ConnectionHandler handler = new ConnectionHandler(socket, comm);
            gameThread = new Thread(new Game(comm, handlerThread));
            gameThread.start();
            handlerThread = new Thread(handler);
            handlerThread.start();
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
