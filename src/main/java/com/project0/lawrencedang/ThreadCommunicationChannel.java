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

    public GameState takeState() {
        synchronized (this) {
            while (!hasStateUpdate) {
                try 
                {
                    wait();
                } catch (InterruptedException e) 
                {
                }
            }
        }

        GameState retrievedState = newState;
        hasStateUpdate = false;
        notifyAll();
        return retrievedState;
    }

    public void putState(GameState state) {
        synchronized (this) {
            while (hasStateUpdate) {
                try 
                {
                    wait();
                } catch (InterruptedException e) 
                {
                }
            }
        }

        newState = state;
        hasStateUpdate= true;
        notifyAll();
    }

    public int takeOption() {
        synchronized (this) {
            while (!hasPlayerOption) {
                try 
                {
                    wait();
                } catch (InterruptedException e) 
                {
                }
            }
        }

        int retrievedOption = playerOption;
        hasPlayerOption = false;
        notifyAll();
        return retrievedOption;
    }

    public void putOption(int option) {
        synchronized (this) {
            while (hasPlayerOption) {
                try 
                {
                    wait();
                } catch (InterruptedException e) 
                {
                }
            }
        }

        playerOption = option;
        hasPlayerOption = true;
        notifyAll();
    }
}