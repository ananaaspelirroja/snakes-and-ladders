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
    // Costruttore privato per impedire l'instanziamento esterno
    private Deck() {
        this.noStoppingCard = false;
        this.cards = new LinkedList<>();
        initializeDeck();
    }

    // Metodo per ottenere l'istanza Singleton
    public static Deck getInstance() {
        if (instance == null) {
            instance = new Deck();  // Inizializza l'istanza se non esiste già
        }
        return instance;
    }

    public static Deck getInstance(boolean noStoppingCard) {
        if (instance == null) {
            instance = new Deck();
            instance.noStoppingCard = noStoppingCard;  // Imposta il valore della carta NoStopping se passato
        }
        return instance;
    }

    // Metodo per simulare l'inizializzazione del mazzo con delle carte
    private void initializeDeck() {
        // Aggiungi solo carte non di sosta se noStoppingCard è true

        cards.add(new Card(1, new BenchEffect()));
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

    public boolean getNoStoppingCard() {
        return noStoppingCard;
    }
}
