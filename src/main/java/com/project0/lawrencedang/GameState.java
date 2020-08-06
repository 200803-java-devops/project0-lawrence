package com.project0.lawrencedang;

public class GameState {

    private int dealerTotal;
    private int playerTotal;
    private PlayerState playerState;
    private EndState endState;
    
    public GameState()
    {
        dealerTotal = 0;
        playerTotal = 0;
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

    public EndState getEndState()
    {
        return endState;
    }

    public void addDealerTotal(int val)
    {
        dealerTotal += val;
    }

    public void addPlayerTotal(int val)
    {
        playerTotal += val;
    }

    public void setPlayerState(PlayerState state)
    {
        playerState = state;
    }

    public void setEndState(EndState state)
    {
        endState = state;
    }

}