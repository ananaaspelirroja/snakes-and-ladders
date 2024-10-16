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
    private Deck(boolean noStoppingCard) {
        this.noStoppingCard = noStoppingCard;
        this.cards = new LinkedList<>();
        initializeDeck();
    }

    public static Deck getInstance(boolean noStoppingCard) {
        if (instance == null) {
            instance = new Deck(noStoppingCard);  // Inizializza solo alla prima chiamata
        }
        return instance;
    }

    // Metodo per ottenere l'istanza esistente del Singleton
    public static Deck getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Deck non inizializzato. Chiamare getInstance(boolean noStoppingCard) prima.");
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
            if(!(card.getEffect() instanceof NoStoppingEffect)){
                cards.add(card);  // Rimetti la carta in fondo al mazzo
            }
        } else {
            System.out.println("Il mazzo è vuoto.");
        }
    }

    public boolean getNoStoppingCard() {
        return noStoppingCard;
    }


    public void addTheStoppingCard() { //add the card after it is used
        cards.add(new Card(5, new NoStoppingEffect()));
        System.out.println("The stopping card was added to the deck! \n");
    }
}
