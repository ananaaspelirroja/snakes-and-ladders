package board.effects;

import game.Game;
import components.Player;

public interface Effect {  //Implementor of Bridge pattern

    void applyEffect(Game game, Player player);

}
