package main.board.effects;


import main.components.Player;
import main.game.Game;

public class NoStoppingEffect implements Effect {
    @Override
    public void applyEffect(Game game, Player player) {
        player.setHasANoStoppingCard(true);
        System.out.println("Player " + player.getNickname() + " acquires a No Stopping card! \n");
    }

    @Override
    public String toString() {
        return "NoStoppingEffect{}";
    }
}
