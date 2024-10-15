package board.effects;

import game.Game;
import board.Deck;
import components.Player;

public class DrawACardEffect implements Effect {
    //usiamo il Singleton
    protected Deck deck;

    @Override
    public void applyEffect(Game game, Player player) {
        deck = Deck.getInstance(deck.getNoStoppingCard());  // Ottiene l'istanza del Singleton (viene creato alla prima invocazione)
        deck.pickACard(game, player);
        System.out.println("Il giocatore " + player.getNickname() + " deve pescare una carta! \n");
    }
}
