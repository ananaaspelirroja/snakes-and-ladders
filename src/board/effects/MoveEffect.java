package board.effects;

import board.Square;
import game.Game;
import components.Player;

public class MoveEffect implements Effect {

    protected Square destination;

    public MoveEffect(Square destination) {
        this.destination = destination;
    }

    @Override
    public void applyEffect(Game game, Player player) {
        player.setCurrentPosition(destination);
    }

}
