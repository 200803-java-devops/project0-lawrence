package com.project0.lawrencedang;

import java.util.List;

public class GameState {

    private Hand dealerHand;
    private Hand playerHand;
    private PlayerState playerState;
    private EndState endState;
    
    public GameState()
    {
        dealerHand = new Hand();
        playerHand = new Hand();
        playerState = PlayerState.PLAYING;
        endState = EndState.NA;
    }

    public List<Card> getDealerHand()
    {
        return dealerHand.getVisibleCards();
    }

    public List<Card> getPlayerHand()
    {
        return playerHand.getVisibleCards();
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
        dealerHand.addVisibleCard(card);
    }

    public void addHiddenDealerHand(Card card)
    {
        dealerHand.addHiddenCard(card);
    }

    public void hideDealerCard()
    {
        dealerHand.hideCard();
    }

    public void addPlayerHand(Card card)
    {
        playerHand.addVisibleCard(card);
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