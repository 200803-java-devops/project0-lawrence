package com.project0.lawrencedang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;

public class ConnectionHandler implements Runnable
{
    public static final String MESSAGE_TEMPLATE = "MSG|%s\n";
    public static final String STATE_TEMPLATE = "STATE|DEALER_TOTAL:%d/PLAYER_TOTAL:%d/PLAYER_STATE:%s/END_STATE:%s\n";
    public static final String READY = "READY|\n";

    private Socket mySocket;
    private ThreadCommunicationChannel commChannel;
    public ConnectionHandler(Socket socket, ThreadCommunicationChannel comm)
    {
        if (socket == null || commChannel == null)
        {
            throw new NullPointerException("Arguments to ConnectionHandler cannot be null.");
        }
        this.mySocket = socket;
        this.commChannel = comm;
    }
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

    private String generateStateString(GameState state)
    {
        return String.format(STATE_TEMPLATE, state.getDealerTotal(), state.getPlayerTotal(), state.getPlayerState(), state.getEndState());
    }
}