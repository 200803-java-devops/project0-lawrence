package com.project0.lawrencedang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class DeckTest {
    Deck deck;
    Deck deck4;
    Random random1;
    Random random2;
    @Before
    public void setup()
    {
        random1 = new Random(5643);
        random2 = new Random(5643);
        deck = new Deck(1, random1);
        deck.shuffle();
        deck4 = new Deck(4, random2);
        deck.shuffle();
    }

    @Test
    public void generatesCorrectNumberCards()
    {
        assertEquals(52, deck.peekAllCards().size());
        assertEquals(4, deck.numCards(Card.Ace));
        assertEquals(4, deck.numCards(Card.Two));

        assertEquals(52*4, deck4.peekAllCards().size());
        assertEquals(16, deck4.numCards(Card.Ace));
        assertEquals(16, deck4.numCards(Card.Two));
    }

    @Test
    public void correctlyDeductsCards()
    {
        int numCards = deck.peekAllCards().size();
        deck.takeCard();
        assertEquals(numCards-1, deck.peekAllCards().size());
    }

    @Test
    public void getsEmptyCard()
    {
        int numCards = deck.peekAllCards().size();
        for(int i=0; i<numCards; i++)
        {
            deck.takeCard();
        }
        assertEquals(Card.None, deck.takeCard());
    }

    public void correctlyShuffles()
    {
        ArrayList<Card> beforeShuffle =  deck.peekAllCards();
        deck.shuffle();
        assertFalse(beforeShuffle.equals(deck.peekAllCards()));
    }
}