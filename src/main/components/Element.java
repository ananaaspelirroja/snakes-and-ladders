package main.components;

import main.components.effects.Effect;
import main.memento.Game;

import java.io.Serial;
import java.io.Serializable;

public abstract class Element implements Serializable {

    protected int number;
    protected Effect effect;
    @Serial
    private static final long serialVersionUID = -8138479252936226678L;

    public Element(int number, Effect effect){
        this.number = number;
        this.effect = effect;
    }

    public void applyEffect(Game game, Player player){
        effect.applyEffect(game, player);
    }

    public Effect getEffect() {
        return effect;
    }

    public int getNumber() {
        return number;
    }
}
