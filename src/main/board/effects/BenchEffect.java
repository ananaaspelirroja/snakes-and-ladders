package main.board.effects;

import main.board.Deck;
import main.game.Game;
import main.components.Player;

public class BenchEffect implements Effect {

    public BenchEffect() {
    }

    private final static int TURNS_TO_WAIT = 1;

    @Override
    public void applyEffect(Game game, Player player) {
        if (player.getHasANoStoppingCard()) {
            System.out.println("Player " + player.getNickname() + " uses a No Stopping card! \n");
            player.setHasANoStoppingCard(false);
            Deck deck = Deck.getInstance();
            deck.addTheStoppingCard();
        } else {
            player.setTurnsToWait(TURNS_TO_WAIT);
            System.out.println("Player " + player.getNickname() + " has to wait " + TURNS_TO_WAIT + " turn! \n");
        }
    }

    @Override
    public String toString() {
        return "BenchEffect{}";
    }
}
