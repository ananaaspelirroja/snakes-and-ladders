package main.components.effects;

import main.memento.Game;
import main.components.Player;

import java.io.Serializable;

public class ChanceEffect implements Effect, Serializable {

    public ChanceEffect() {
    }

    @Override
    public void applyEffect(Game game, Player player) {
        int number = game.getDice().roll(player.getCurrentPosition());
        game.turn(number, player);
        System.out.println("Player " + player.getNickname() + " got really lucky! The dice are rolling again! \n");
    }

    @Override
    public String toString() {
        return "ChanceEffect{}";
    }
}
