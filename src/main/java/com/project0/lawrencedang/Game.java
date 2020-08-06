package com.project0.lawrencedang;

import java.util.Random;

public class Game implements Runnable
{
    public static final String GAME_STATE_STRING_FORMAT = "STATE %d %d";
    public static final String END_STATE_STRING_FORMAT = "END %s";

    private GameState state;
    private Random rng;
    private ThreadCommunicationChannel commChannel;
    public Game(ThreadCommunicationChannel comm)
    {  
        this.commChannel = comm;
        state = new GameState();
        rng = new Random();
    }

    public void run()
    {
        runGame();
    }

    private void runGame()
    {
        dealFirstCards();
        if(!earlyEnd())
        {
            sendStateToPlayer(state);
            handlePlayerOptions(state);
            if(state.getPlayerState() != PlayerState.BUST)
            {
                dealerTurn();
            }
        }
        resolveGame();
        sendStateToPlayer(state);
    }

    private void handlePlayerOptions(GameState state)
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
                sendStateToPlayer(state);
            }
        }
    } 

    private void sendStateToPlayer(GameState state)
    {
        commChannel.putState(state);
    }

    private int readPlayerInput()
    {
        int option = commChannel.takeOption();
        return option;
    }

    private int dealCard()
    {
        return rng.nextInt(11)+1;
    }

    private void dealFirstCards()
    {
        state.addDealerTotal(dealCard()); 
        state.addDealerTotal(dealCard());
        state.addPlayerTotal(dealCard());
        state.addDealerTotal(dealCard());
    }

    private boolean earlyEnd()
    {
        if (state.getPlayerTotal() >= 21 || state.getDealerTotal() >= 21)
        {
            return true;
        }
        return false;
    }

    private void hitPlayer()
    {
        state.addPlayerTotal(dealCard());
        updatePlayerStateAfterHit();
    }

    private void standPlayer()
    {
        state.setPlayerState(PlayerState.STAND);
    }

    private void dealerTurn()
    {
        while(state.getDealerTotal() < 17)
        {
            state.addDealerTotal(rng.nextInt(11)+1);
        }
    }

    private void resolveGame()
    {
        if (state.getPlayerState() == PlayerState.BUST)
        {
            state.setEndState(EndState.LOSE);     
        }
        else if (state.getPlayerTotal() == state.getDealerTotal())
        {
            state.setEndState(EndState.TIE);
        }
        else if (state.getPlayerTotal() == 21 && state.getDealerTotal() != 21)
        {
            state.setEndState(EndState.BLACKJACK);
        }
        else if (state.getDealerTotal() > 21)
        {
            state.setEndState(EndState.WIN);
        }
        else if (state.getPlayerTotal() < state.getDealerTotal())
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
        if(state.getPlayerTotal()>21)
        {
            state.setPlayerState(PlayerState.BUST);
        }
        else if (state.getPlayerTotal() == 21)
        {
            state.setPlayerState(PlayerState.STAND); 
        }
        else
        {
            state.setPlayerState(PlayerState.PLAYING); 
        }
    }
}