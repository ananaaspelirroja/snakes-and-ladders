package main.board.effects;

import main.game.Game;
import main.components.Player;

public interface Effect {  //Implementor of Bridge pattern

    void applyEffect(Game game, Player player);

}
