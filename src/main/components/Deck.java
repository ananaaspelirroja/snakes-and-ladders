package main.components;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

import main.components.effects.*;
import main.gui.ApplicationGUI;
import main.memento.Game;

public class Deck implements Serializable {
    private static Deck instance; // Singleton instance
    private Queue<Card> cards;  // Simulates cards as a queue
    private boolean noStoppingCard;

    // Private constructor for Singleton
    private Deck(boolean noStoppingCard) {
        this.noStoppingCard = noStoppingCard;
        this.cards = new LinkedList<>();
        initializeDeck();
    }

    public static synchronized Deck getInstance(boolean noStoppingCard) {
        if (instance == null) {
            instance = new Deck(noStoppingCard);  // Initialize only on the first call
        }
        return instance;
    }

    // Method to get the existing Singleton instance
    public static synchronized Deck getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Deck not initialized. Call getInstance(boolean noStoppingCard) first.");
        }
        return instance;
    }

    // Method to simulate deck initialization with cards
    private void initializeDeck() {
        // Add only non-stopping cards if noStoppingCard is true

        cards.add(new Card(1, new BenchEffect()));
        cards.add(new Card(2, new ChanceEffect()));
        cards.add(new Card(3, new GuestEffect()));
        cards.add(new Card(4, new SpringEffect()));
        if(noStoppingCard){
            cards.add(new Card(5, new NoStoppingEffect()));
        }

        //System.out.println("The deck has been created with the initial cards."); - DEBUG
    }

    // Method to draw a card and put it back at the bottom of the deck
    public void pickACard(Game game, Player player) {
        Card card = cards.poll();  // Draw the first card (removing it from the top)
        if (card != null) {
            System.out.println("Player " + player.getNickname() + " drew: " + card + "\n");
            card.applyEffect(game, player);
            if(!(card.getEffect() instanceof NoStoppingEffect)){
                cards.add(card);  // Put the card back at the bottom of the deck
            }
        } else {
            System.out.println("The deck is empty. \n");
        }
    }

    public boolean getNoStoppingCard() {
        return noStoppingCard;
    }

    public void addTheStoppingCard() { // Add the card after it is used
        cards.add(new Card(5, new NoStoppingEffect()));
        System.out.println("The stopping card was added to the deck! \n");
    }
}
