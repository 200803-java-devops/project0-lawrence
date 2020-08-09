package com.project0.lawrencedang;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ThreadCommunicationChannelTest {
    ThreadCommunicationChannel commChannel;

    public class TakeOptionLockHelper implements Runnable
    {
        public void run()
        {
            commChannel.takeOption();
        }
    }

    public class PutOptionLockHelper implements Runnable
    {
        public void run()
        {
            commChannel.putOption(1);
        }
    }

    public class TakeStateLockHelper implements Runnable
    {
        public void run()
        {
            commChannel.takeState();
        }
    }

    public class PutStateLockHelper implements Runnable
    {
        public void run()
        {
            commChannel.putState(new GameState());
        }
    }
    
    @Before
    public void setup()
    {
        commChannel = new ThreadCommunicationChannel();
    }

    @Test
    public void putOptionLockTest() throws InterruptedException
    {
        commChannel.putOption(1);
        Thread helper = new Thread(new PutOptionLockHelper());
        helper.start();
        Thread.sleep(1000);
        assertTrue(helper.isAlive());
    }

    @Test
    public void takeOptionLockTest() throws InterruptedException
    {
        Thread helper = new Thread(new TakeOptionLockHelper());
        helper.start();
        Thread.sleep(1000);
        assertTrue(helper.isAlive());
    }

    @Test
    public void putStateLockTest() throws InterruptedException
    {
        commChannel.putState(new GameState());
        Thread helper = new Thread(new PutStateLockHelper());
        helper.start();
        Thread.sleep(1000);
        assertTrue(helper.isAlive());
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
    public void optionValueTest()
    {
        commChannel.putOption(5);
        assert(commChannel.takeOption()==5);
        commChannel.putOption(11);
        assert(commChannel.takeOption()==11);
    }

    @Test
    public void stateValueTest()
    {
        GameState state = new GameState();
        commChannel.putState(state);
        assertTrue(commChannel.takeState() == state);

        GameState state2 = new GameState();
        state2.addDealerHand(Card.Ten);
        commChannel.putState(state2);
        assertTrue(commChannel.takeState() == state2);
    }
}