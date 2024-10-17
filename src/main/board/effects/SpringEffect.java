package main.board.effects;

import main.game.Game;
import main.components.Player;

public class SpringEffect implements Effect {

    @Override
    public void applyEffect(Game game, Player player) {
        int number = player.lastDiceRoll();
        game.turn(number, player);
        System.out.println("Player " + player.getNickname() + " got a real luck! It is moving again of " + number + " squares! \n");
    }

    @Override
    public String toString() {
        return "SpringEffect{}";
    }
}
