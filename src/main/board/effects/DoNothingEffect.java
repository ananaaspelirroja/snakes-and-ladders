package main.board.effects;

import main.game.Game;
import main.components.Player;

public class DoNothingEffect implements Effect {

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
