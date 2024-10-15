package board.effects;


import components.Player;
import game.Game;

public class NoStoppingEffect implements Effect {
    @Override
    public void applyEffect(Game game, Player player) {
        player.setHasANoStoppingCard(true);
    }
}
