package board.effects;

import game.Game;
import components.Player;

public class SpringEffect implements Effect {

    @Override
    public void applyEffect(Game game, Player player) {
        int number = game.getDice().roll(player.getCurrentPosition());
        game.turn(number, player);
        System.out.println("Player " + player.getNickname() + " got on a chance square! The dice are rolling again! \n");
    }

    @Override
    public String toString() {
        return "SpringEffect{}";
    }
}
