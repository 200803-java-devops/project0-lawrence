package com.project0.lawrencedang;

import java.util.ArrayList;

public enum Card {
    None(0), Ace(1), Two(2), Three(3), Four(4), Five(5), Six(6), Seven(7), Eight(8), Nine(9), Ten(10), Jack(10), Queen(10), King(10);

    private final int value;

    Card(int val)
    {
        value = val;
    }

    public static int lowValueOf(Card card)
    {
        return card == Ace? 1 : card.value;
    }

    public static int lowValueOf(ArrayList<Card> cards)
    {
        int total = 0;
        for(Card card: cards)
        {
            total += lowValueOf(card);
        }
        return total;
    }

    public static int highValueOf(Card card)
    {
        return card == Ace? 11 : card.value;
    }

    public static int highValueOf(ArrayList<Card> cards)
    {
        int total = 0;
        for(Card card: cards)
        {
            total += highValueOf(card);
        }
        return total;
    }

    public static Card fromCardName(String cardName)
    {
        for(Card card: Card.values())
        {
            if(cardName == card.name())
            {
                return card;
            }
        }
        return null;
    }
}