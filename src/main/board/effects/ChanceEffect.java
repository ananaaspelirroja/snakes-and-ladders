package main.board.effects;

import main.game.Game;
import main.components.Player;

public class ChanceEffect implements Effect {

    public ChanceEffect() {
    }

    @Override
    public void applyEffect(Game game, Player player) {
        int number = game.getDice().roll(player.getCurrentPosition());
        game.turn(number, player);
        System.out.println("Player " + player.getNickname() + " got really lucky! The main.dice are rolling again! \n");
    }

    @Override
    public String toString() {
        return "ChanceEffect{}";
    }
}
