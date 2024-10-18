package main.components.effects;

import main.memento.Game;
import main.components.Player;

public interface Effect {  //Implementor of Bridge pattern

    void applyEffect(Game game, Player player);

}
