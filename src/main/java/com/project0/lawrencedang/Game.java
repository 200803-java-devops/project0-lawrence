package com.project0.lawrencedang;

import java.util.Collection;
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
    private ThreadCommunicationChannel commChannel;
    private Deck deck;
    private int numPlayers;
    /**
     * Creates a new Game object with the specified ThreadCommunicationChannel,
     * which should be shared to the CommunicationHandler run by commThread.
     * commThread is used to get the status of the CommunicationHandler thread, to allow this thread to die
     * if the commThread is dead.
     * @param comm The shared ThreadCommunicationChannel between the Game and the ConnectionHandler.
     * @param commThread The thread running the ConnectionHandler.
     */
    public Game(int numPlayers, ThreadCommunicationChannel comm)
    {  
        this.commChannel = comm;
        this.state = new GameState();
        this.deck = null;
        this.numPlayers = numPlayers;
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
        while(!allPlayersStopped())
        {
            RequestEntry requestEntry;
            while((requestEntry = commChannel.takeRequest())==null);
            int playerId = requestEntry.getRequestorID();
            ClientRequest request = requestEntry.getClientRequest();
            try
            {
                switch(request)
                {
                    case GET_STATE:
                        commChannel.putState(playerId, getStateView());
                        break;
                    case DO_HIT:
                        if(!isPlayerDone(playerId))
                        {
                            hitPlayer(playerId);
                        }
                        break;
                    case DO_STAND:
                        if(!isPlayerDone(playerId))
                        {
                            standPlayer(playerId);
                        }
                        break;
                    default:
                        System.out.println("This request isn't supported yet.");
                        break;
                }
            }
            catch(InterruptedException e)
            {
                System.err.println("Interrupted while handling client request.");
                Thread.currentThread().interrupt();
            }
        }
        dealerTurn();
        resolveGame();

        
    }

    private GameStateView getStateView()
    {
        return new GameStateView(this.state);
    }

    private Card dealCard()
    {
        return deck.takeCard();
    }

    private void dealFirstCards()
    {
        this.deck = new Deck(4, new Random());
        this.deck.shuffle();
        state.addDealerHand(dealCard()); 
        state.addHiddenDealerHand(dealCard());
        for(int i =0; i< numPlayers; i++)
        {
            this.state.addPlayerHand(i, dealCard());
            this.state.addPlayerHand(i, dealCard());
        }
    }

    private void hitPlayer(int playerId)
    {
        state.addPlayerHand(playerId, dealCard());
        updatePlayerStateAfterHit(playerId);
    }

    private void standPlayer(int playerId)
    {
        state.setPlayerState(playerId, PlayerState.STAND);
    }

    private void dealerTurn()
    {
        while(Card.lowValueOf(state.getDealerHand()) < 17)
        {
            state.addDealerHand(dealCard());
        }
    }

    private void resolveGame()
    {
        System.out.println("Resolving game");
        int dealerHandVal = bestHandValue(state.getDealerHand());
        for(int i = 0; i<numPlayers; i++)
        {
            int playerHandVal = bestHandValue(state.getPlayerHand(i));
            EndState result;
            if (state.getPlayerState(i) == PlayerState.BUST)
            {
                result = EndState.LOSE;  
            }
            else if (playerHandVal == dealerHandVal)
            {
                result = EndState.TIE;
            }
            else if (playerHandVal == 21 && dealerHandVal != 21)
            {
                result = EndState.BLACKJACK;
            }
            else if (dealerHandVal > 21)
            {
                result = EndState.WIN;
            }
            else if (playerHandVal < dealerHandVal)
            {
                result = EndState.LOSE;
            }
            else // playerTotal > dealerTotal AND less than 21
            {
                result = EndState.WIN;
            }
            
            state.setEndState(i, result);
        }
    }

    private void updatePlayerStateAfterHit(int playerId)
    {
        int playerHandVal = bestHandValue(state.getPlayerHand());
        if(playerHandVal>21)
        {
            state.setPlayerState(playerId, PlayerState.BUST);
        }
        else if (playerHandVal == 21)
        {
            state.setPlayerState(playerId, PlayerState.STAND); 
        }
        else
        {
            state.setPlayerState(playerId, PlayerState.PLAYING); 
        }
        System.out.println(playerHandVal);
        System.out.println(state.getPlayerState());
    }

    private int bestHandValue(Collection<Card> cards)
    {
        return Card.highValueOf(cards) <= 21? Card.highValueOf(cards) : Card.lowValueOf(cards);
    }
    
    private boolean isPlayerDone(int player)
    {
        return state.getPlayerState() != PlayerState.PLAYING;
    }

    private boolean allPlayersStopped()
    {
        boolean anyActive = false;
        for(int i=0; i<numPlayers; i++)
        {
            if(state.getPlayerState() == PlayerState.PLAYING)
            {
                anyActive = true;
                break;
            }
        }
        return !anyActive;

    }
}