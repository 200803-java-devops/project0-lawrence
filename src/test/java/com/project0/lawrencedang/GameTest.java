package com.project0.lawrencedang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class GameTest 
{
    ThreadCommunicationChannel commChannel;
    Game game;
    @Before
    public void setup()
    {
        commChannel = new ThreadCommunicationChannel();
        game = new Game(1, commChannel);
    }
    /**
     * Rigorous Test :-)
     */
    @Test(timeout = 3000)
    public void returnsCorrectState() throws InterruptedException
    {
        new Thread(game).start();
        commChannel.putRequest(ClientRequest.GET_STATE);
        GameStateView view = commChannel.takeState();
        assertNotNull(view);

        ThreadCommunicationChannel comm2 = new ThreadCommunicationChannel(4);
        Game game2 = new Game(4, comm2);
        new Thread(game2).start();
        comm2.putRequest(ClientRequest.GET_STATE);
        view = comm2.takeState();
        assertEquals(4, view.playerHands.length);
        assertEquals(4, view.playerStates.length);
        assertEquals(4, view.endStates.length);
    }
    @Test(timeout = 3000)
    public void hitsCorrectly() throws InterruptedException
    {
        new Thread(game).start();
        commChannel.putRequest(ClientRequest.GET_STATE);
        GameStateView view = commChannel.takeState();
        commChannel.putRequest(ClientRequest.DO_HIT);
        commChannel.putRequest(ClientRequest.GET_STATE);
        GameStateView view2 = commChannel.takeState();
        assertEquals(1, view2.playerHands[0].length - view.playerHands[0].length);
    }

    @Test(timeout = 3000)
    public void standsCorrectly() throws InterruptedException
    {
        new Thread(game).start();
        commChannel.putRequest(ClientRequest.DO_STAND);
        commChannel.putRequest(ClientRequest.GET_STATE);
        GameStateView view = commChannel.takeState();
        assertEquals(view.playerStates[0], PlayerState.STAND);
    }
}
