package com.project0.lawrencedang;

import java.util.Random;

public class GameState {

    private int dealerTotal;
    private int playerTotal;
    private PlayerState playerState;
    private Random rng;
    public GameState()
    {
        dealerTotal = 0;
        playerTotal = 0;
        rng = new Random();
        playerState = PlayerState.PLAYING;
    }

    public int getDealerTotal()
    {
        return dealerTotal;
    }

    public int getPlayerTotal()
    {
        return playerTotal;
    }

    public PlayerState getPlayerState()
    {
        return playerState;
    }

    public void dealFirstCards()
    {
        dealerTotal = rng.nextInt(20)+2;
        playerTotal = rng.nextInt(20)+2;
    }

    public boolean earlyEnd()
    {
        if (playerTotal >= 21 || dealerTotal >= 21)
        {
            return true;
        }
        return false;
    }

    public void hitPlayer()
    {
        playerTotal += rng.nextInt(12)+1;
        updatePlayerStateAfterHit();
    }

    public void standPlayer()
    {
        playerState = PlayerState.STAND;
    }

    public void dealerTurn()
    {
        while(dealerTotal < 17)
        {
            dealerTotal += rng.nextInt(12)+1;
        }
    }

    public EndState resolveGame()
    {
        if (playerState == PlayerState.BUST)
        {
            return EndState.LOSE;
        }
        else if (playerTotal == dealerTotal)
        {
            return EndState.TIE;
        }
        else if (playerTotal == 21 && dealerTotal != 21)
        {
            return EndState.BLACKJACK;
        }
        else if (dealerTotal > 21)
        {
            return EndState.WIN;
        }
        else if (playerTotal < dealerTotal)
        {
            return EndState.LOSE;
        }
        else // playerTotal > dealerTotal AND less than 21
        {
            return EndState.WIN;
        }

    }

    private void updatePlayerStateAfterHit()
    {
        if(playerTotal>21)
        {
            playerState = PlayerState.BUST;
        }
        else if (playerTotal == 21)
        {
            playerState = PlayerState.STAND;
        }
        else
        {
            playerState = PlayerState.PLAYING;
        }
    }

}