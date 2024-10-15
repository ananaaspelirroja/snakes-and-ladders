package board.effects;

import game.Game;
import components.Player;

public class ChanceEffect implements Effect {
    @Override
    public void applyEffect(Game game, Player player) {
        int number = game.getDice().roll(player.getCurrentPosition());
        game.turn(number, player);
        System.out.println("Player " + player.getNickname() + " got really lucky! The dice are rolling again! \n");
    }
}
