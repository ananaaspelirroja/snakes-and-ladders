package main.dice;

import java.io.Serializable;

public abstract class DiceDecorator implements Dice, Serializable {
    protected Dice dice;
    public DiceDecorator(Dice dice) {
        this.dice = dice;
    }

}
