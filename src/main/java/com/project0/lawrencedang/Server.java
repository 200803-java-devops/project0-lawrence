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
    public static final String GAME_STATE_STRING_FORMAT = "STATE %d %d";
    public static final String END_STATE_STRING_FORMAT = "END %s";

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

    public void runGame()
    {
        GameState state = new GameState();
        state.dealFirstCards();
        sendStateToPlayer(state);
        if(state.earlyEnd())
        {
            handlePlayerOptions(state);
            state.dealerTurn();
        }
        EndState endState = state.resolveGame();
        sendResultsToPlayer(endState);
    }

    private void handlePlayerOptions(GameState state)
    {
        PlayerState playerState = state.getPlayerState();
        while(playerState == PlayerState.PLAYING)
        {
            int option = readPlayerInput();
            switch(option)
            {
                case 1:
                    state.hitPlayer();
                case 2:
                    state.standPlayer();
            }
            playerState = state.getPlayerState();
            sendStateToPlayer(state);
        }
    } 

    private void sendStateToPlayer(GameState state)
    {
        String stateString = String.format(GAME_STATE_STRING_FORMAT, state.getDealerTotal(), state.getPlayerTotal());
        commChannel.putMessage(stateString);
    }

    private int readPlayerInput()
    {
        String option = commChannel.takeMessage();
        return Integer.parseInt(option.trim());
    }

    private void sendResultsToPlayer(EndState endState)
    {
        String endString = String.format(END_STATE_STRING_FORMAT, endState.name());
        commChannel.putMessage(endString);
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
