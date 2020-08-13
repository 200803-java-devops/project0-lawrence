package com.project0.lawrencedang;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

public class CommunicationHandlerTest {
    ThreadCommunicationChannel commChannel;

    @Before
    public void setup()
    {
        commChannel = new ThreadCommunicationChannel();
    }
    
    @Test 
    public void generatesCorrectStateString()
    {
        GameState state = new GameState();
        state.addPlayerHand(Card.Four);
        state.addPlayerHand(Card.Ace);
        GameStateView view = new GameStateView(state);
        String stateString = CommunicationHandler.generateStateString(view).split("\\|")[1];
        GameStateView view2 = new Gson().fromJson(stateString, GameStateView.class);
        assertArrayEquals(view.playerHands, view2.playerHands);
        assertArrayEquals(view.dealerHand, view2.dealerHand);
        assertArrayEquals(view.playerStates, view2.playerStates);
        assertArrayEquals(view.endStates, view2.endStates);
    }

    @Test(timeout = 1500)
    public void sendsCorrectState() throws IOException, InterruptedException
    {
        byte[] requestString = "GET_STATE\n".getBytes();
        GameState state = new GameState();
        state.addDealerHand(Card.Ace);
        GameStateView view = new GameStateView(state);
        commChannel.putState(view);
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(requestString)));
        PrintStream writer = new PrintStream(new ByteArrayOutputStream()); 
        CommunicationHandler handler = new CommunicationHandler(reader, writer, 0, commChannel);
        Thread handlerThread = new Thread(handler);
        handlerThread.start();
        
        assertEquals(view, commChannel.takeState());
        
    }

    @Test(timeout=1500)
    public void putsCorrectOption() throws IOException, InterruptedException
    {
        byte[] requestString = "DO_HIT\n".getBytes();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(requestString)));
        PrintStream writer = new PrintStream(new ByteArrayOutputStream()); 
        CommunicationHandler handler = new CommunicationHandler(reader, writer, 0, commChannel);
        Thread handlerThread = new Thread(handler);
        handlerThread.start();
        RequestEntry request;
        while((request = commChannel.takeRequest())==null)
        {
            Thread.sleep(100);
        }
        
        assertEquals(ClientRequest.DO_HIT, request.getClientRequest());
    }
}