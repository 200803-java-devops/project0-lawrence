package com.project0.lawrencedang;

import java.util.Collection;

/**
 * Card represnts a single playing card in a standard 52-card deck. A card has both a high and low numeric value; for
 * most cards these values are the same, but an Ace has a low value of 1 and a high value of 11.
 */
public enum Card {
    None(0), Ace(1), Two(2), Three(3), Four(4), Five(5), Six(6), Seven(7), Eight(8), Nine(9), Ten(10), Jack(10), Queen(10), King(10);

    private final int value;

    Card(int val)
    {
        value = val;
    }

    /**
     * Returns the low numeric value of a single card.
     */
    public static int lowValueOf(Card card)
    {
        return card == Ace? 1 : card.value;
    }

    /**
     * Returns the sum of the low numeric values of all of the cards.
     * @param cards the collection of cards to evaluate.
     * @return the sum of the low values of the collection of cards.
     */
    public static int lowValueOf(Collection<Card> cards)
    {
        int total = 0;
        for(Card card: cards)
        {
            total += lowValueOf(card);
        }
        return total;
    }

    /**
     * Returns the high numeric value of a single card.
     */
    public static int highValueOf(Card card)
    {
        return card == Ace? 11 : card.value;
    }

    /**
     * Returns the sum of the high numeric values of all of the cards.
     * @param cards the collection of cards to evaluate
     * @return the sum of the high values of the collection of cards.
     */
    public static int highValueOf(Collection<Card> cards)
    {
        int total = 0;
        for(Card card: cards)
        {
            total += highValueOf(card);
        }
        return total;
    }

    /**
     * Gets the Card that corresponds to the specified string, or None otherwise.
     * @param cardName the string that may correspond to a Card.
     * @return the corresponding Card, or None if there is no match.
     */
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