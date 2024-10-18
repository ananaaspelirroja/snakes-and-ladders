package main.components;

import main.components.effects.Effect;
import main.memento.Game;

import java.io.Serializable;

public abstract class Element implements Serializable {

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
