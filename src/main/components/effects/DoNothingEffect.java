package main.components.effects;

import main.memento.Game;
import main.components.Player;

import java.io.Serializable;

public class DoNothingEffect implements Effect, Serializable {

    public DoNothingEffect() {
    }

    @Override
    public void applyEffect(Game game, Player player) {
        System.out.println("Player " + player.getNickname() +" landed on a standard square! \n");
    }

    @Override
    public String toString() {
        return "DoNothingEffect{}";
    }
}
