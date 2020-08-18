package com.project0.lawrencedang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The ThreadCommunicationChannel allows the Game thread to communicate with multiple CommunicationHandler threads.
 * It provides various thread-safe and synchronized methods to pass state updates and requests between threads.
 */
public class ThreadCommunicationChannel {
    private List<GameStateView> newState;
    private List<Boolean> hasFreshState;
    private List<Lock> lockList;
    private List<Condition> notifiers;
    private Queue<RequestEntry> playerRequest;
    private int numPlayers;


    /**
     * Create a new ThreadCommunciationChannel for one CommunicationHandler thread.
     */
    public ThreadCommunicationChannel() {
        hasFreshState = Collections.synchronizedList(new ArrayList<>(1));
        newState = Collections.synchronizedList(new ArrayList<>(1));
        lockList = new ArrayList<>(1);
        notifiers = new ArrayList<>(1);
        playerRequest = new ConcurrentLinkedQueue<>();
        numPlayers = 1;
        initializeLists();
    }

    /**
     * Create a new ThreadCommunicationChannel for multiple CommunicationHandler threads
     * @param numPlayers the number of CommunicationHandler threads.
     */
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

    /**
     * Takes the state intended for the first thread/player
     * The calling thread will block if the state has not been updated.
     */
    public GameStateView takeState() throws InterruptedException
    {
        return takeState(0);
    }

    /**
     * Takes the state intended for the specified thread.
     * @param playerId the integer representing the thread whose state will be taken
     * @throws InterruptedException
     */
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

    /**
     * Puts a state update for the first thread
     */
    public void putState(GameStateView state) throws InterruptedException
    {
        putState(0, state);
    }

    /**
     * Puts a state update for the specified player
     * @param playerId The player to put the state for
     * @throws InterruptedException
     */
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

    /**
     * Takes a RequestEntry off the front of the queue
     * @return a RequestEntry if there is one, or null if the queue is empty
     */
    public RequestEntry takeRequest() 
    {
        return playerRequest.poll();
    }

    /**
     * Puts the client request in the queue, associated with the first player
     */
    public void putRequest(ClientRequest cr)
    {
        putRequest(0, cr);
    }

    /**
     * Puts the ClientRequest in the queue, associated with the specified player
     * @param playerId the player to associate the ClientRequest with.
     */
    public void putRequest(int playerId, ClientRequest cr)
    {
        playerRequest.add(new RequestEntry(playerId, cr));
    }
}