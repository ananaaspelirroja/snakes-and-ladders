package board.effects;

import game.Game;
import board.Deck;
import components.Player;

public class DrawACardEffect implements Effect {
    //usiamo il Singleton
    protected Deck deck;

    public DrawACardEffect() {
    }

    @Override
    public void applyEffect(Game game, Player player) {
        // Inizializza il deck se non è già stato fatto
        if (deck == null) {
            deck = Deck.getInstance();  // Ottieni l'istanza Singleton del mazzo
        }
        System.out.println(deck.getNoStoppingCard());

        // Ora il deck è inizializzato, quindi possiamo chiamare i metodi su di esso
        deck.pickACard(game, player);
        System.out.println("Il giocatore " + player.getNickname() + " deve pescare una carta! \n");
    }

    @Override
    public String toString() {
        return "DrawACardEffect{}";
    }
}
