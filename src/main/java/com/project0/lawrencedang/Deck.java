package com.project0.lawrencedang;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Random;

/**
 * A deck represents a deck of playing cards. It can store a variable number of standard 52-card decks.
 * A deck may be shuffled, and cards may be drawn from the deck.
 */
public class Deck {
    private ArrayDeque<Card> cards;
    private Random rng;
    
    /**
     * Create a new deck that stores a standard 52-card deck.
     */
    public Deck()
    {
        cards = new ArrayDeque<Card>(52);
        initializeCards(1);
        rng = new Random();
    }

    /**
     * Create a new deck that stores numDecks number of 52-card decks.
     * @param numDecks the number of 52-card decks to store.
     */
    public Deck(int numDecks)
    {
        cards = new ArrayDeque<Card>(52*numDecks);
        initializeCards(numDecks);
        rng = new Random();
    }

    /**
     * Create a new deck that stores numDecks number of 52 card decks. The specified Random object is used for shuffling.
     * @param numDecks the number of 52-card decks to store.
     * @param rand the pseudoranom number generator used to shuffle the deck.
     */
    public Deck(int numDecks, Random rand)
    {
        cards = new ArrayDeque<Card>(52*numDecks);
        initializeCards(numDecks);
        rng = rand;
    }

    /**
     * Shuffle the contents of the deck using a random number generator.
     */
    public void shuffle()
    {
        ArrayList<Card> uglyCardList = new ArrayList<Card>(cards);
        Collections.shuffle(uglyCardList, rng);
        cards = new ArrayDeque<Card>(uglyCardList);
    }

    /**
     * Return the number of cards of the specified value.
     * @param cardType the value of the cards to check.
     * @return the number of cards in the deck that are have the value cardType
     */
    public int numCards(Card cardType)
    {
        return Collections.frequency(cards, cardType);
    }

    /**
     * Return a list containing the contents of the deck, in order.
     */
    public ArrayList<Card> peekAllCards()
    {
        return new ArrayList<Card>(cards);
    }

    /**
     * Take a card off the top of deck and return it.
     * @return The card at the top of the deck, or Card.None if the deck is empty.
     */
    public Card takeCard()
    {
        return cards.size() > 0? cards.pop() : Card.None;
    }

    private void initializeCards(int decks)
    {
        for(Card card : Card.values())
        {
            if(card != Card.None)
            {
                for(int i = 0; i<4*decks; i++)
                {
                    cards.add(card);
                }
            }
        }
    }
}