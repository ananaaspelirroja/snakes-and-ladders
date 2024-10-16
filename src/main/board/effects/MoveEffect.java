package main.board.effects;

import main.board.Square;
import main.game.Game;
import main.components.Player;

public class MoveEffect implements Effect {

    protected Square destination;

    public MoveEffect(Square destination) {
        this.destination = destination;
    }

    @Override
    public void applyEffect(Game game, Player player) {
        player.setCurrentPosition(destination);
        System.out.println("Player " + player.getNickname() + " has to move to square " + destination.getNumber());
    }

    @Override
    public String toString() {
        return "MoveEffect{" +
                "destination=" + destination +
                '}';
    }
}
