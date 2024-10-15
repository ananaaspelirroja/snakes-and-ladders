package board.effects;

import game.Game;
import components.Player;

public class BenchEffect implements Effect {

    private final static int TURNS_TO_WAIT = 1;
    @Override
    public void applyEffect(Game game, Player player) {
        player.setTurnsToWait(TURNS_TO_WAIT);
        System.out.println("Player " + player.getNickname() + " has to wait " + TURNS_TO_WAIT + "turns! \n");
    }
}
