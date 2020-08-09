package com.project0.lawrencedang;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

public class CardTest {
    
    @Test
    public void lowAndHighEqualForNonAce()
    {
        assertEquals(Card.lowValueOf(Card.Eight), 8);
        assertEquals(Card.lowValueOf(Card.Eight), Card.highValueOf(Card.Eight));
        assertEquals(Card.lowValueOf(Card.King), 10);
        assertEquals(Card.lowValueOf(Card.King), Card.highValueOf(Card.King));
    }

    @Test
    public void getsCorrectLowAceValue()
    {
        assertEquals(Card.lowValueOf(Card.Ace), 1);
    }

    @Test
    public void getsCorrectLowHandValue()
    {
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(Card.Ace);
        hand.add(Card.King);
        assertEquals(11, Card.lowValueOf(hand));
    }

    @Test
    public void getsCorrectHighAceValue()
    {
        assertEquals(Card.highValueOf(Card.Ace), 11);
    }

    @Test
    public void getsCorrectHighHandValue()
    {
        ArrayList<Card> hand = new ArrayList<Card>();
        hand.add(Card.Ace);
        hand.add(Card.King);
        assertEquals(21, Card.highValueOf(hand));
    }

    @Test
    public void nameConvertsToCard()
    {
        String name = Card.Ace.name();
        assertEquals(Card.Ace, Card.fromCardName(name));
    }
}