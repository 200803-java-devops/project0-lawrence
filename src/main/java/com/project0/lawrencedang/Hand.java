package com.project0.lawrencedang;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class Hand {
    private List<Card> visibleCards;
    private Deque<Card> hiddenCards;

    public Hand()
    {
        visibleCards = new ArrayList<>();
        hiddenCards = new ArrayDeque<>();
    }

    public List<Card> getVisibleCards()
    {
        return Collections.unmodifiableList(visibleCards);
    }

    public void addVisibleCard(Card card)
    {
        visibleCards.add(card);
    }

    public void addHiddenCard(Card card)
    {
        hiddenCards.addFirst(card);
    }

    public void hideCard()
    {
        if(visibleCards.size() > 0)
            hiddenCards.addFirst(visibleCards.remove(0));
    }

    public void revealHiddenCards()
    {
        while(hiddenCards.size() > 0){visibleCards.add(hiddenCards.pop());}
    }

    public int numHiddenCards()
    {
        return hiddenCards.size();
    }
}