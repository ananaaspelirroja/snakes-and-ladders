package board.effects;

import game.Game;
import components.Player;

public class DoNothingEffect implements Effect {

    @Override
    public void applyEffect(Game game, Player player) {
        System.out.println("Player " + player.getNickname() +" landed on a standard square! \n");
    }
}
