package main.components.effects;

import main.memento.Game;
import main.components.Deck;
import main.components.Player;

import java.io.Serializable;

public class DrawACardEffect implements Effect, Serializable {
    // We use the Singleton
    protected Deck deck;

    public DrawACardEffect() {
    }

    @Override
    public void applyEffect(Game game, Player player) {
        // Initialize the deck if it hasn't been done yet
        if (deck == null) {
            deck = Deck.getInstance();  // Get the Singleton instance of the deck
        }
        System.out.println(deck.getNoStoppingCard());

        // Now the deck is initialized, so we can call methods on it
        deck.pickACard(game, player);
        System.out.println("Player " + player.getNickname() + " has to draw a card! \n");
    }

    @Override
    public String toString() {
        return "DrawACardEffect{}";
    }
}
