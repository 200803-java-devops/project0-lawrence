package com.project0.lawrencedang;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * A hand represents the cards held by a player or dealer.
 * A hand may have visible cards, which are known to all, and hidden cards which are only known to the game logic.
 */
public class Hand {
    private List<Card> visibleCards;
    private Deque<Card> hiddenCards;

    /**
     * Create an empty hand with no visible or hidden cards.
     */
    public Hand()
    {
        visibleCards = new ArrayList<>();
        hiddenCards = new ArrayDeque<>();
    }

    /**
     * Gets all visible cards in this hand
     * @return a list of all visible cards in this hand
     */
    public List<Card> getVisibleCards()
    {
        return Collections.unmodifiableList(visibleCards);
    }

    /**
     * Adds the card to the visible cards in this hand
     */
    public void addVisibleCard(Card card)
    {
        visibleCards.add(card);
    }

    /**
     * Adds the card to the hidden cards in this hand
     */
    public void addHiddenCard(Card card)
    {
        hiddenCards.addFirst(card);
    }

    /**
     * Hides the first card in the visible cards.
     */
    public void hideCard()
    {
        if(visibleCards.size() > 0)
            hiddenCards.addFirst(visibleCards.remove(0));
    }

    /** 
     * Adds all hidden cards to the visible hand.
     */
    public void revealHiddenCards()
    {
        while(hiddenCards.size() > 0){visibleCards.add(hiddenCards.pop());}
    }

    /**
     * Returns the number of hidden cards.
     */
    public int numHiddenCards()
    {
        return hiddenCards.size();
    }
}