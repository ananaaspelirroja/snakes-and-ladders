package board;

import Game.Game;
import components.Player;

public interface Effect {

    void applyEffect(Game game, Player player);

}
