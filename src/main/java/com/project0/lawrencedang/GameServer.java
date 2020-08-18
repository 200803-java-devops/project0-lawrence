package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.project0.lawrencedang.ClientServerProtocol.READY;
import static com.project0.lawrencedang.ClientServerProtocol.RECEIVED;
import static com.project0.lawrencedang.ClientServerProtocol.REJECT;

/**
 * GameServer is a class that acts as the server for the multiplayer blackjack game.
 * It waits for a client(s) to connect then spawns one thread to handle communicating with the client
 * and one thread to handle the game logic.
 */
public class GameServer extends Server {
    public static final int MAX_CONNECTIONS= 1;

    private ExecutorService gameThreadPool;
    private ExecutorService handlerThreadPool;



    /**
     * Creates a Server with the specified port.
     * @param port the port number to listen for connections on.
     * @throws IOException
     */
    public GameServer(ServerSocket server) {
        super(server);
        gameThreadPool = Executors.newFixedThreadPool(2);
        handlerThreadPool = Executors.newFixedThreadPool(MAX_CONNECTIONS);
    
    }

    /**
     * Listens for incoming connections, then creates threads to handle client actions and game logic.
     */
    @Override
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
            // Check login token
            try
            {
                if(checkLogin(reader, writer) == false)
                {
                    socket.close();
                    continue;
                }
            }
            catch(IOException e)
            {
                System.err.println("Problem while verifying login");
                try
                {
                    socket.close();
                }
                catch(IOException e2)
                {
                    e2.printStackTrace();
                }
                continue;
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
        try
        {
            handlerThreadPool.shutdown();
            handlerThreadPool.awaitTermination(2, TimeUnit.HOURS);
            System.err.println("Shutting down");
            gameThreadPool.shutdownNow();
        }
        catch(InterruptedException e)
        {
            return;
        }
        
    }

    public boolean checkLogin(BufferedReader reader, PrintStream writer) throws IOException
    {
        writer.print(READY);
        String clientResponse = reader.readLine();
        TokenRepository repo = new TokenRepository();
        try
        {
            if(clientResponse == null)
            {
                System.err.println("Client disconnected while verifying login");
                return false;
            }
            else if(repo.tokenExists(clientResponse))
            {
                writer.print(RECEIVED);
                return true;
            }
            else
            {
                writer.print(REJECT);
                return false;
            }
        }
        catch(SQLException e)
        {
            System.err.println("Problem while connecting to db");
            return false;
        }
        
    }


    public static void main(String[] args) {
        int port = 5643;
        GameServer gameServer = null;
        try
        {
            ServerSocket server = new ServerSocket(port);
            gameServer = new GameServer(server);
        }
        catch (IOException e)
        {
            System.err.println("Problem while starting server.");
            System.exit(1);
        }
        System.out.println("Server started.");
        gameServer.listen();
        System.out.println("Server shutdown");
        
    }
}
