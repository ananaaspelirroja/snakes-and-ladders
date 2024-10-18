package main.components.effects;


import main.components.Player;
import main.memento.Game;

import java.io.Serializable;

public class NoStoppingEffect implements Effect, Serializable {
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
