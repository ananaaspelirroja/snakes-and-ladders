package board;

import java.util.LinkedList;
import java.util.Queue;

import board.effects.*;
import components.Player;
import game.Game;

public class Deck {
    private static Deck instance; // Singleton instance
    private Queue<Card> cards;  // Simulazione delle carte come coda
    private boolean noStoppingCard;

    // Costruttore privato per il Singleton
    private Deck(boolean noStoppingCard) {
        this.noStoppingCard = noStoppingCard;
        cards = new LinkedList<Card>();
        initializeDeck();
    }

    // Metodo per ottenere l'unica istanza del mazzo con parametro opzionale (lazy initialization)
    public static Deck getInstance(boolean noStoppingCard) {
        if (instance == null) {
            instance = new Deck(noStoppingCard);
        }
        return instance;
    }

    // Metodo per simulare l'inizializzazione del mazzo con delle carte
    private void initializeDeck() {
        // Aggiungi solo carte non di sosta se noStoppingCard è true
        if (!noStoppingCard) {
            cards.add(new Card(1, new BenchEffect()));  // Carta di sosta
        }
        cards.add(new Card(2, new ChanceEffect()));
        cards.add(new Card(3, new GuestEffect()));
        cards.add(new Card(4, new SpringEffect()));
        if(noStoppingCard){
            cards.add(new Card(5, new NoStoppingEffect()));
        }

        System.out.println("Il mazzo è stato creato con le carte iniziali.");
    }

    // Metodo per pescare una carta e rimetterla in fondo al mazzo
    public void pickACard(Game game, Player player) {
        Card card = cards.poll();  // Pesca la prima carta (rimuovendola dalla cima)
        if (card != null) {
            System.out.println("Il giocatore " + player.getNickname() + " ha pescato: " + card);
            card.applyEffect(game, player);
            cards.add(card);  // Rimetti la carta in fondo al mazzo
        } else {
            System.out.println("Il mazzo è vuoto.");
        }
    }
}
