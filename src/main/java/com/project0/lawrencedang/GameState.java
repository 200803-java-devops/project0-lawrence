package com.project0.lawrencedang;

import java.util.List;

public class GameState {

    private Hand dealerHand;
    private Hand[] playerHands;
    private PlayerState[] playerStates;
    private EndState[] endStates;
    
    public GameState()
    {
        dealerHand = new Hand();
        playerHands = new Hand[1];
        playerStates = new PlayerState[1];
        endStates = new EndState[1];
        initializePlayerHands(1);
        initializePlayerStates(1);
        initializeEndStates(1);
    }

    public GameState(int numPlayers)
    {
        dealerHand = new Hand();
        playerHands = new Hand[numPlayers];
        playerStates = new PlayerState[numPlayers];
        endStates = new EndState[numPlayers];
        initializePlayerHands(numPlayers);
        initializePlayerStates(numPlayers);
        initializeEndStates(numPlayers);
    }

    public List<Card> getDealerHand()
    {
        return dealerHand.getVisibleCards();
    }

    public List<Card> getPlayerHand()
    {
        return playerHands[0].getVisibleCards();
    }

    public List<Card> getPlayerHand(int player)
    {
        return playerHands[player].getVisibleCards();
    }

    public List[] getPlayerHands()
    {
        List[] output = new List[playerHands.length];
        for(int i=0; i< playerHands.length; i++)
        {
            output[i] = playerHands[i].getVisibleCards();
        }
        return output;
    }

    public PlayerState getPlayerState()
    {
        return playerStates[0];
    }

    public PlayerState getPlayerState(int player)
    {
        return playerStates[player];
    }

    public PlayerState[] getPlayerStates()
    {
        return playerStates.clone();
    }

    public EndState getEndState()
    {
        return endStates[0];
    }

    public EndState getEndState(int player)
    {
        return endStates[player];
    }

    public EndState[] getEndStates()
    {
        return endStates.clone();
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
        playerHands[0].addVisibleCard(card);
    }

    public void addPlayerHand(int player, Card card)
    {
        playerHands[player].addVisibleCard(card);
    }

    public void setPlayerState(PlayerState state)
    {
        playerStates[0] = state;
    }

    public void setPlayerState(int player, PlayerState state)
    {
        playerStates[player] = state;
    }

    public void setEndState(EndState state)
    {
        endStates[0] = state;
    }

    public void setEndState(int player, EndState state)
    {
        endStates[player] = state;
    }

    private void initializePlayerHands(int players)
    {
        for(int i = 0; i< players; i++)
        {
            playerHands[i]=new Hand();
        }
    }

    private void initializePlayerStates(int players)
    {
        for(int i = 0; i< players; i++)
        {
            playerStates[i]=PlayerState.PLAYING;
        }
    }

    private void initializeEndStates(int players)
    {
        for(int i = 0; i< players; i++)
        {
            endStates[i]=EndState.NA;
        }
    }

}