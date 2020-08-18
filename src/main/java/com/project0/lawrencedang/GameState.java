package com.project0.lawrencedang;

import java.util.List;

/**
 * A GameState represents the state of player and dealer hands, the players' states (if they are playing, stopped, or busted),
 * and whether the players' results for this game.
 */
public class GameState {

    private Hand dealerHand;
    private Hand[] playerHands;
    private PlayerState[] playerStates;
    private EndState[] endStates;
    
    /**
     * Creates a new GameState for a one player game.
     */
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

    /**
     * Creates a new GameState for a game for the specified number of players
     * @param numPlayers the number of players in this game.
     */
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

    /**
     * Returns the dealer's visible cards.
     * @return a list of the cards in the dealer's hand that are not hidden 
     */
    public List<Card> getDealerHand()
    {
        return dealerHand.getVisibleCards();
    }

    /**
     * Returns the first player's cards
     * @return a list of the card's in the first player's hand.
     */
    public List<Card> getPlayerHand()
    {
        return playerHands[0].getVisibleCards();
    }

    /**
     * Returns the specified player's cards
     * @param player the player number whose hand will be returned
     * @return a list of the cards in the player's hand
     */
    public List<Card> getPlayerHand(int player)
    {
        return playerHands[player].getVisibleCards();
    }

    /**
     * Returns all players' cards.
     * @return An array of lists of cards in players' hands.
     */
    public List[] getPlayerHands()
    {
        List[] output = new List[playerHands.length];
        for(int i=0; i< playerHands.length; i++)
        {
            output[i] = playerHands[i].getVisibleCards();
        }
        return output;
    }

    /**
     * Get the state of the first player
     */
    public PlayerState getPlayerState()
    {
        return playerStates[0];
    }

    /**
     * Get the specified player's state
     * @param player the player whose state will be returned
     * @return
     */
    public PlayerState getPlayerState(int player)
    {
        return playerStates[player];
    }

    /**
     * Return all players' states.
     * @return an array of all players' states
     */
    public PlayerState[] getPlayerStates()
    {
        return playerStates.clone();
    }

    /**
     * Return the first player's end state
     */
    public EndState getEndState()
    {
        return endStates[0];
    }

    /**
     * Return the specified player's end state
     * @param player the player whose end state will be returned
     */
    public EndState getEndState(int player)
    {
        return endStates[player];
    }

    /**
     * Return all players' end states
     * @return an array containing all players' end states
     */
    public EndState[] getEndStates()
    {
        return endStates.clone();
    }

    /**
     * Add the card to the dealer's hand. The card is visible.
     */
    public void addDealerHand(Card card)
    {
        dealerHand.addVisibleCard(card);
    }

    /**
     * Add a hidden card to the dealer's hand.
     */
    public void addHiddenDealerHand(Card card)
    {
        dealerHand.addHiddenCard(card);
    }

    /**
     * Hide a card in the dealer's hand.
     */
    public void hideDealerCard()
    {
        dealerHand.hideCard();
    }

    /**
     * Reveal all hidden dealer cards
     */
    public void revealDealerCards()
    {
        dealerHand.revealHiddenCards();
    }

    /**
     * Add the card to the first player's hand
     */
    public void addPlayerHand(Card card)
    {
        playerHands[0].addVisibleCard(card);
    }

    /**
     * Add the card to the specified player's hand
     * @param player the integer representing which player to add the card to
     * @param card the card to add to the hand
     */
    public void addPlayerHand(int player, Card card)
    {
        playerHands[player].addVisibleCard(card);
    }

    /**
     * Set the first player's state
     */
    public void setPlayerState(PlayerState state)
    {
        playerStates[0] = state;
    }

    /**
     * Set the specified player's state
     * @param player the integer representing which player's state will be set
     * @param state the player's new state
     */
    public void setPlayerState(int player, PlayerState state)
    {
        playerStates[player] = state;
    }

    /**
     * Set the first player's state
     */
    public void setEndState(EndState state)
    {
        endStates[0] = state;
    }

    /**
     * Set the specified player's state
     * @param player an integer representing which player's end state will be set
     * @param state the player's new end state
     */
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