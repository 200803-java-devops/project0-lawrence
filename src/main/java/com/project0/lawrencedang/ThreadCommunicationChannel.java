package com.project0.lawrencedang;

public class ThreadCommunicationChannel {
    public String message;
    public boolean hasMessage;

    public ThreadCommunicationChannel() {
        hasMessage = false;
        message = null;
    }

    public String takeMessage() {
        synchronized (this) {
            while (!hasMessage) {
                try 
                {
                    wait();
                } catch (InterruptedException e) 
                {
                }
            }
        }

        String retrievedMessage = message;
        hasMessage = false;
        notifyAll();
        return retrievedMessage;
    }

    public void putMessage(String msg) {
        synchronized (this) {
            while (hasMessage) {
                try 
                {
                    wait();
                } catch (InterruptedException e) 
                {
                }
            }
        }

        message = msg;
        hasMessage = true;
        notifyAll();
    }
}