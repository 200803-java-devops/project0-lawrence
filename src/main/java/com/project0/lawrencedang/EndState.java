package com.project0.lawrencedang;

/**
 * An enum representing the possible end states of the game.
 * BLACKJACK represents the end state where the player is dealt a "natural" blackjack
 * WIN represents a normal player victory
 * TIE represents a case where the player ties
 * LOSE represents a case where the player loses
 * NA represents that the game has not yet ended
 */
public enum EndState {
    BLACKJACK, WIN, TIE, LOSE, NA
}