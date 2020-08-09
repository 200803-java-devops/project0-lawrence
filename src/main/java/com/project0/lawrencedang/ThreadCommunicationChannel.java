package com.project0.lawrencedang;

public class ThreadCommunicationChannel {
    private GameState newState;
    private boolean hasStateUpdate;
    private int playerOption;
    private boolean hasPlayerOption;

    public ThreadCommunicationChannel() {
        hasStateUpdate = false;
        newState = null;
        hasPlayerOption = false;
        playerOption = -1;
    }

    public synchronized GameState takeState() 
    {
        while (!hasStateUpdate) {
            try 
            {
                wait();
            } catch (InterruptedException e) 
            {
            }
        }

        GameState retrievedState = newState;
        hasStateUpdate = false;
        notifyAll();
        return retrievedState;
    }

    public synchronized void putState(GameState state)
    {

        while (hasStateUpdate) {
            try 
            {
                wait();
            } catch (InterruptedException e) 
            {
            }
        }
    

        newState = state;
        hasStateUpdate= true;
        notifyAll();
    }

    public synchronized int takeOption() 
    {
        while (!hasPlayerOption) {
            try 
            {
                wait();
            } catch (InterruptedException e) 
            {
            }
        }

        int retrievedOption = playerOption;
        hasPlayerOption = false;
        notifyAll();
        return retrievedOption;
    }

    public synchronized void putOption(int option)
    {
        while (hasPlayerOption) {
            try 
            {
                wait();
            } catch (InterruptedException e) 
            {
            }
        }
        
        playerOption = option;
        hasPlayerOption = true;
        notifyAll();
    }
}