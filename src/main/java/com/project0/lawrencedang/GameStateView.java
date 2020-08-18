package com.project0.lawrencedang;

import java.util.List;

/**
 * A GameStateView is a simplified representation of the game state, stripped of
 * information that only the game logic should know. It is designed to be easily
 * translated into a JSON representation.
 */
public class GameStateView 
{
    Card[] dealerHand;
    Card[][] playerHands;
    PlayerState[] playerStates;
    EndState[] endStates;

    /**
     * Creates a new GameStateView from a GameState
     * @param state the GameState from which to draw data from.
     */
    public GameStateView(GameState state)
    {
        playerStates = state.getPlayerStates();
        endStates = state.getEndStates();
        int numPlayers = endStates.length;
        dealerHand = new Card[state.getDealerHand().size()];
        dealerHand = state.getDealerHand().toArray(dealerHand);
        List[] hands = state.getPlayerHands();
        playerHands = new Card[numPlayers][];
        for(int i = 0; i< numPlayers; i++)
        {
            Card[] newArray = new Card[hands[i].size()];
            hands[i].toArray(newArray);
            playerHands[i] = newArray;
        }
    }
}