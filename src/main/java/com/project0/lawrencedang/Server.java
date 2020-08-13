package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server is a class that acts as the server for the multiplayer blackjack game.
 * It waits for a client(s) to connect then spawns one thread to handle communicating with the client
 * and one thread to handle the game logic.
 */
public class Server {
    public static final int MAX_CONNECTIONS= 4;

    private ServerSocket server;
    private ExecutorService gameThreadPool;
    private ExecutorService handlerThreadPool;



    /**
     * Creates a Server with the specified port.
     * @param port the port number to listen for connections on.
     * @throws IOException
     */
    public Server(int port) throws IOException
    {
        try 
        {
            server = new ServerSocket(port);
            gameThreadPool = Executors.newFixedThreadPool(2);
            handlerThreadPool = Executors.newFixedThreadPool(MAX_CONNECTIONS);
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
        int connections = 0;
        ThreadCommunicationChannel comm = new ThreadCommunicationChannel();
        List<CommunicationHandler> connectionList  = new ArrayList<CommunicationHandler>();
        while(connections < MAX_CONNECTIONS)
        {
            BufferedReader reader;
            PrintStream writer;
            try
            {
                socket = server.accept();
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintStream(socket.getOutputStream());
            }
            catch (IOException e)
            {
                System.err.println("Problem while listening for connections.");
                return;
            }

            System.out.println("Client connected");
            CommunicationHandler handler = new CommunicationHandler(reader, writer, connections, comm);
            handler.greet();
            connectionList.add(handler);
            connections++;
        }

        gameThreadPool.execute(new Game(connections, comm));
        for(CommunicationHandler h: connectionList)
        {
            handlerThreadPool.execute(h);
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
