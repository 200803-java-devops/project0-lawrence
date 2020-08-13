package com.project0.lawrencedang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadCommunicationChannel {
    private List<GameStateView> newState;
    private List<Boolean> hasFreshState;
    private List<Lock> lockList;
    private List<Condition> notifiers;
    private Queue<RequestEntry> playerRequest;
    private int numPlayers;


    public ThreadCommunicationChannel() {
        hasFreshState = Collections.synchronizedList(new ArrayList<>(1));
        newState = Collections.synchronizedList(new ArrayList<>(1));
        lockList = new ArrayList<>(1);
        notifiers = new ArrayList<>(1);
        playerRequest = new ConcurrentLinkedQueue<>();
        numPlayers = 1;
        initializeLists();
    }

    public ThreadCommunicationChannel(int numPlayers) {
        hasFreshState = Collections.synchronizedList(new ArrayList<>(numPlayers));
        newState = Collections.synchronizedList(new ArrayList<>(numPlayers));
        lockList = new ArrayList<>(numPlayers);
        notifiers = new ArrayList<>(numPlayers);
        playerRequest = new ConcurrentLinkedQueue<>();
        this.numPlayers = numPlayers;
        initializeLists();
    }


    private void initializeLists()
    {
        for(int i =0; i<numPlayers; i++)
        {
            newState.add(null);
        }

        for(int i =0; i<numPlayers; i++)
        {
            hasFreshState.add(false);
        }

        for(int i =0; i<numPlayers; i++)
        {
            lockList.add(new ReentrantLock());
        }

        for(int i =0; i<numPlayers; i++)
        {
            notifiers.add(lockList.get(i).newCondition());
        }
    }

    public GameStateView takeState() throws InterruptedException
    {
        return takeState(0);
    }

    public GameStateView takeState(int playerId) throws InterruptedException
    {
        GameStateView state = null;
        Lock lock = lockList.get(playerId);
        lock.lockInterruptibly();
        Condition waiter = notifiers.get(playerId);
        try
        {
            while(hasFreshState.get(playerId) == false)
            {
                waiter.await();
            }
            state = newState.get(playerId);
            hasFreshState.set(playerId, false);
        }
        finally
        {
            waiter.signalAll();
            lock.unlock();
        }
        return state;
    }

    public void putState(GameStateView state) throws InterruptedException
    {
        putState(0, state);
    }

    public void putState(int playerId, GameStateView state) throws InterruptedException
    {

        Lock lock = lockList.get(playerId);
        lock.lockInterruptibly();
        Condition notifier = notifiers.get(playerId);
        try
        {
            newState.set(playerId, state);
            hasFreshState.set(playerId, true);
        }
        finally
        {
            notifier.signalAll();
            lock.unlock();
        }
    }

    public RequestEntry takeRequest() 
    {
        return playerRequest.poll();
    }

    public void putRequest(ClientRequest cr)
    {
        putRequest(0, cr);
    }

    public void putRequest(int playerId, ClientRequest cr)
    {
        playerRequest.add(new RequestEntry(playerId, cr));
    }
}