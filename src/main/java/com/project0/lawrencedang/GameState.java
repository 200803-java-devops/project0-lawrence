package com.project0.lawrencedang;

import java.util.ArrayList;

public class GameState {

    private ArrayList<Card> dealerHand;
    private ArrayList<Card> playerHand;
    private PlayerState playerState;
    private EndState endState;
    
    public GameState()
    {
        dealerHand = new ArrayList<Card>();
        playerHand = new ArrayList<Card>();
        playerState = PlayerState.PLAYING;
        endState = EndState.NA;
    }

    public ArrayList<Card> getDealerHand()
    {
        return new ArrayList<Card>(dealerHand);
    }

    public ArrayList<Card> getPlayerHand()
    {
        return new ArrayList<Card>(playerHand);
    }

    public PlayerState getPlayerState()
    {
        return playerState;
    }

    public EndState getEndState()
    {
        return endState;
    }

    public void addDealerHand(Card card)
    {
        dealerHand.add(card);
    }

    public void addPlayerHand(Card card)
    {
        playerHand.add(card);
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