package com.project0.lawrencedang;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Random;

public class Deck {
    private ArrayDeque<Card> cards;
    private Random rng;
    
    public Deck()
    {
        cards = new ArrayDeque<Card>(52);
        initializeCards(1);
        rng = new Random();
    }

    public Deck(int numDecks)
    {
        cards = new ArrayDeque<Card>(52*numDecks);
        initializeCards(numDecks);
        rng = new Random();
    }

    public Deck(int numDecks, Random rand)
    {
        cards = new ArrayDeque<Card>(52*numDecks);
        initializeCards(numDecks);
        rng = rand;
    }

    public void shuffle()
    {
        ArrayList<Card> uglyCardList = new ArrayList<Card>(cards);
        Collections.shuffle(uglyCardList, rng);
        cards = new ArrayDeque<Card>(uglyCardList);
    }

    public int numCards(Card cardType)
    {
        return Collections.frequency(cards, cardType);
    }

    public ArrayList<Card> peekAllCards()
    {
        return new ArrayList<Card>(cards);
    }

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