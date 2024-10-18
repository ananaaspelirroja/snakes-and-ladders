package main.components.effects;

import main.components.Square;
import main.memento.Game;
import main.components.Player;

import java.io.Serializable;

public class MoveEffect implements Effect, Serializable {

    protected Square destination;

    public MoveEffect(Square destination) {
        this.destination = destination;
    }

    @Override
    public void applyEffect(Game game, Player player) {
        player.setCurrentPosition(destination);
        System.out.println("Player " + player.getNickname() + " has to move to square " + destination.getNumber() + "\n");
    }

    @Override
    public String toString() {
        return "MoveEffect{" +
                "destination=" + destination +
                '}';
    }
}
