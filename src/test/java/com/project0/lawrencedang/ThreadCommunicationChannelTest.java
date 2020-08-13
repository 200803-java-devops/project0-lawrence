package com.project0.lawrencedang;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ThreadCommunicationChannelTest {
    ThreadCommunicationChannel commChannel;

    public class TakeStateLockHelper implements Runnable
    {
        public void run()
        {
            try
            {
                commChannel.takeState();
            }
            catch(InterruptedException e)
            {
                System.err.println("Interrupted");
            }
        }
    }
    
    @Before
    public void setup()
    {
        commChannel = new ThreadCommunicationChannel();
    }

    @Test
    public void takeStateLockTest() throws InterruptedException
    {
        Thread helper = new Thread(new TakeStateLockHelper());
        helper.start();
        Thread.sleep(1000);
        assertTrue(helper.isAlive());
    }

    @Test
    public void requestValueTest()
    {
        commChannel.putRequest(ClientRequest.DO_STAND);
        commChannel.putRequest(ClientRequest.DO_SURRENDER);
        assert(commChannel.takeRequest().getClientRequest()==ClientRequest.DO_STAND);
        assert(commChannel.takeRequest().getClientRequest()==ClientRequest.DO_SURRENDER);
    }

    @Test
    public void stateValueTest()
    {
        GameState state = new GameState();
        GameStateView view = new GameStateView(state);
        try
        {
            commChannel.putState(view);
            assertTrue(commChannel.takeState() == view);
        }
        catch(InterruptedException e)
        {
            System.err.println("Interrupted");
        }

        GameState state2 = new GameState();
        state2.addDealerHand(Card.Ten);
        GameStateView view2 = new GameStateView(state2);
        try
        {
            commChannel.putState(view2);
            assertTrue(commChannel.takeState() == view2);
        }
        catch(InterruptedException e)
        {
            System.err.println("Interrupted");
        }
    }
}