package main.board.effects;

import main.board.Deck;
import main.game.Game;
import main.components.Player;

public class GuestEffect implements Effect {

    public GuestEffect() {
    }

    private final static int TURNS_TO_WAIT = 3;
    @Override
    public void applyEffect(Game game, Player player) {
        if (player.getHasANoStoppingCard()) {
            System.out.println("Player " + player.getNickname() + " uses a No Stopping card! \n");
            Deck deck = Deck.getInstance();
            deck.addTheStoppingCard();
            player.setHasANoStoppingCard(false);
        } else {
            player.setTurnsToWait(TURNS_TO_WAIT);
            System.out.println("Player " + player.getNickname() + " has to wait " + TURNS_TO_WAIT + " turns! \n");
        }
    }

    @Override
    public String toString() {
        return "GuestEffect{}";
    }
}