package com.project0.lawrencedang;

/**
 * An enum used to represent the status of the player.
 * A player is PLAYING when they may take actions.
 * A player STANDs after choosing the STAND option or upon achieving a blackjack
 * A player BUSTs if their card values total more than 21.
 */
public enum PlayerState {
    PLAYING, STAND, BUST
}