package main.board;

import main.board.effects.Effect;
import main.components.Player;
import main.game.Game;

public abstract class Element {

    protected int number;
    protected Effect effect;

    public Element(int number, Effect effect){
        this.number = number;
        this.effect = effect;
    }

    public void applyEffect(Game game, Player player){
        effect.applyEffect(game, player);
    }

    public int getNumber() {
        return number;
    }
}
