package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * A ConnectionHandler serves as the bridge between the client and the game logic.
 * It passes messages to and from the client and the game logic that update the client and/or game state.
 * It implements a simple message protocol to communicate with the client.
 * Regular messages are prefixed with MSG|, state updates are prefixed with STATE|, and the
 * notification that the ConnectionHandler is ready to accept input is READY|
 */
public class ConnectionHandler implements Runnable
{
    public static final String MESSAGE_TEMPLATE = "MSG|%s\n";
    public static final String STATE_TEMPLATE = "STATE|DEALER_TOTAL:%d/PLAYER_TOTAL:%d/PLAYER_STATE:%s/END_STATE:%s\n";
    public static final String READY = "READY|\n";

    private Socket mySocket;
    private ThreadCommunicationChannel commChannel;
    /**
     * Creates a new ConnectionHandler for the supplied socket.
     * Communicates with the game logic via the ThreadCommunicationChannel.
     * @param socket The socket connected to the client.
     * @param comm The ThreadCommunicationChannel shared with the game logic.
     */
    public ConnectionHandler(Socket socket, ThreadCommunicationChannel comm)
    {
        if (socket == null || comm == null)
        {
            throw new NullPointerException("Arguments to ConnectionHandler cannot be null.");
        }
        this.mySocket = socket;
        this.commChannel = comm;
    }
    /**
     * Runs the logic to communicate with the client.
     * First, the client is sent a message confirming the connection.
     * Then the ConnectionHandler repeatedly sends a state update to the client, notifies the client for a response,
     * then delivers the response to the game logic.
     */
    public void run()
    {
        BufferedReader bufferedReader = null;
        PrintStream printStream = null;
        String userResponse = null;
        GameState state = null;
        try
        {
            bufferedReader = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
            printStream = new PrintStream(mySocket.getOutputStream());
        }
        catch (IOException e)
        {
            System.err.println("ConnectionHandler: Problem encountered when getting streams from socket.");
        }
        printStream.printf(MESSAGE_TEMPLATE, "Please wait...");

        while(true)
        {
            state = getState();
            printStream.print(generateStateString(state));
            printStream.print(READY);
            try
            {
                userResponse = bufferedReader.readLine();
                System.out.println(userResponse);
            }
            catch (IOException e)
            {
                System.out.println("Error when communicating with client.");
                return;
            }
            commChannel.putOption(Integer.parseInt(userResponse));
        }
    }


    private GameState getState()
    {
        return commChannel.takeState();
    }

    public static String generateStateString(GameState state)
    {
        return String.format(STATE_TEMPLATE, state.getDealerTotal(), state.getPlayerTotal(), state.getPlayerState(), state.getEndState());
    }
}