package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

import com.google.gson.Gson;
import static com.project0.lawrencedang.ClientServerProtocol.*;

/**
 * A CommunicationHandler serves as the bridge between the client and the game logic.
 * It passes messages to and from the client and the game logic that update the client and/or game state.
 * It implements a simple message protocol to communicate with the client.
 * Regular messages are prefixed with MSG|, state updates are prefixed with STATE|, and the
 * notification that the CommunicationHandler is ready to accept input is READY|
 */
public class CommunicationHandler implements Runnable
{
    private BufferedReader bufferedReader;
    private PrintStream printStream;
    private ThreadCommunicationChannel commChannel;
    private int playerId;
    /**
     * Creates a new CommunicationHandler for the supplied socket.
     * Communicates with the game logic via the ThreadCommunicationChannel.
     * @param socket The socket connected to the client.
     * @param comm The ThreadCommunicationChannel shared with the game logic.
     */
    
    public CommunicationHandler(BufferedReader reader, PrintStream printer, int id, ThreadCommunicationChannel comm)
    {
        if (reader == null || printer == null || comm == null)
        {
            throw new NullPointerException("Arguments to CommunicationHandler cannot be null.");
        }
        this.bufferedReader = reader;
        this.printStream = printer;
        this.commChannel = comm;
        this.playerId = id;
    }

    public void greet()
    {
        printStream.printf(MESSAGE_TEMPLATE, "Please wait...");
    }
    /**
     * Runs the logic to communicate with the client.
     * First, the client is sent a message confirming the connection.
     * Then the CommunicationHandler repeatedly sends a state update to the client, notifies the client for a response,
     * then delivers the response to the game logic.
     */
    public void run()
    {
        String userResponse = null;
        GameStateView state = null;
        

        while(true)
        {
            printStream.print(READY);
            try
            {
                if((userResponse = bufferedReader.readLine())== null)
                {
                    System.out.println(userResponse);
                    throw new IOException("Client disconnected while reading.");
                }
            }
            catch (IOException e)
            {
                System.out.println("Error when communicating with client.");
                return;
            }
            userResponse = userResponse.trim();
            ClientRequest request = ClientRequest.fromString(userResponse);
            String serverResponse = "";
            switch(request)
            {
                case GET_STATE:
                    try
                    {
                        commChannel.putRequest(this.playerId, request);
                        state = commChannel.takeState(this.playerId);
                        serverResponse = generateStateString(state);
                    }
                    catch(InterruptedException e)
                    {
                        System.err.println("Interrupted while handling client input");
                        Thread.currentThread().interrupt();
                    }
                    break;
                case INVALID:
                    serverResponse = REJECT;
                    break;
                default:
                    commChannel.putRequest(this.playerId, request);
                    serverResponse = RECEIVED;
                    break;
            }
            printStream.print(serverResponse);
        }
    }


    public static String generateStateString(GameStateView state)
    {
        Gson jsonSerializer = new Gson();
        return String.format(STATE_TEMPLATE, jsonSerializer.toJson(state));
    }
}