package com.project0.lawrencedang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Game is a class that implements the game logic for Blackjack. It is intended to be run from a thread.
 * It stores the current state of the game in a GameState object.
 * It can communicate state and player options with a CommunicationHandler using a shared ThreadCommunicationChannel Object.
 */
public class Game implements Runnable
{
    public static final String GAME_STATE_STRING_FORMAT = "STATE %d %d";
    public static final String END_STATE_STRING_FORMAT = "END %s";

    private GameState state;
    private Thread commThread;
    private ThreadCommunicationChannel commChannel;
    private Deck deck;
    /**
     * Creates a new Game object with the specified ThreadCommunicationChannel,
     * which should be shared to the CommunicationHandler run by commThread.
     * commThread is used to get the status of the CommunicationHandler thread, to allow this thread to die
     * if the commThread is dead.
     * @param comm The shared ThreadCommunicationChannel between the Game and the ConnectionHandler.
     * @param commThread The thread running the ConnectionHandler.
     */
    public Game(ThreadCommunicationChannel comm,Thread commThread)
    {  
        this.commChannel = comm;
        this.commThread = commThread;
        this.state = new GameState();
        this.deck = new Deck(4, new Random());
    }

    /**
     * Starts running the game logic.
     */
    public void run()
    {
        runGame();
    }

    private void runGame()
    {
        dealFirstCards();
        try
        {
            if(!isEarlyEnd())
            {
                sendStateToPlayer();
                handlePlayerOptions();
                if(state.getPlayerState() != PlayerState.BUST)
                {
                    dealerTurn();
                }
            }
            resolveGame();
            sendStateToPlayer();
        }
        catch (IOException e)
        {
            System.out.println("Client handler thread died. Terminating game thread.");
            return;
        }
        
    }

    private void handlePlayerOptions() throws IOException
    {
        PlayerState playerState = state.getPlayerState();
        while(playerState == PlayerState.PLAYING)
        {
            int option = readPlayerInput();
            switch(option)
            {
                case 1:
                    hitPlayer();
                case 2:
                    standPlayer();
            }
            playerState = state.getPlayerState();
            if(state.getPlayerState() == PlayerState.PLAYING)
            {
                sendStateToPlayer();
            }
        }
    } 

    private void sendStateToPlayer() throws IOException
    {
        if(commThread.isAlive())
        {
            commChannel.putState(state);
        }
        else
        {
            throw new IOException("Client handler thread died.");
        }
    }

    private int readPlayerInput() throws IOException
    {
        if(commThread.isAlive())
        {
            int option = commChannel.takeOption();
            return option;
        }
        else
        {
            throw new IOException("Client handler thread died.");
        }
    }

    private Card dealCard()
    {
        return deck.takeCard();
    }

    private void dealFirstCards()
    {
        state.addDealerHand(dealCard()); 
        state.addDealerHand(dealCard());
        state.addPlayerHand(dealCard());
        state.addPlayerHand(dealCard());
    }

    private boolean isEarlyEnd()
    {
        ArrayList<Card> playerHand = state.getPlayerHand();
        ArrayList<Card> dealerHand = state.getDealerHand();
        if (bestHandValue(playerHand) >= 21 || bestHandValue(dealerHand) >= 21)
        {
            return true;
        }
        return false;
    }

    private void hitPlayer()
    {
        state.addPlayerHand(dealCard());
        updatePlayerStateAfterHit();
    }

    private void standPlayer()
    {
        state.setPlayerState(PlayerState.STAND);
    }

    private void dealerTurn()
    {
        while(bestHandValue(state.getDealerHand()) < 17)
        {
            state.addDealerHand(dealCard());
        }
    }

    private void resolveGame()
    {
        int playerHandVal =  bestHandValue(state.getPlayerHand());
        int dealerHandVal =  bestHandValue(state.getDealerHand());
        if (state.getPlayerState() == PlayerState.BUST)
        {
            state.setEndState(EndState.LOSE);     
        }
        else if (playerHandVal == dealerHandVal)
        {
            state.setEndState(EndState.TIE);
        }
        else if (playerHandVal == 21 && dealerHandVal != 21)
        {
            state.setEndState(EndState.BLACKJACK);
        }
        else if (dealerHandVal > 21)
        {
            state.setEndState(EndState.WIN);
        }
        else if (playerHandVal < dealerHandVal)
        {
            state.setEndState(EndState.LOSE);
        }
        else // playerTotal > dealerTotal AND less than 21
        {
            state.setEndState(EndState.WIN);   
        }
    }

    private void updatePlayerStateAfterHit()
    {
        int playerHandVal = bestHandValue(state.getPlayerHand());
        if(playerHandVal>21)
        {
            state.setPlayerState(PlayerState.BUST);
        }
        else if (playerHandVal == 21)
        {
            state.setPlayerState(PlayerState.STAND); 
        }
        else
        {
            state.setPlayerState(PlayerState.PLAYING); 
        }
    }

    private int bestHandValue(ArrayList<Card> cards)
    {
        return Card.highValueOf(cards) <= 21? Card.highValueOf(cards) : Card.lowValueOf(cards);
    }
    
}