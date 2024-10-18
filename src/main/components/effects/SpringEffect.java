package main.components.effects;

import main.memento.Game;
import main.components.Player;

import java.io.Serializable;

public class SpringEffect implements Effect, Serializable {

    @Override
    public void applyEffect(Game game, Player player) {
        int number = player.getLastDiceRoll();
        game.turn(number, player);
        System.out.println("Player " + player.getNickname() + " got a real luck! It is moving again of " + number + " squares! \n");
    }

    @Override
    public String toString() {
        return "SpringEffect{}";
    }
}
