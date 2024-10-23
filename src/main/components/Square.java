package main.components;

import main.components.effects.Effect;

import java.io.Serializable;

public class Square extends Element {

    protected boolean isADestination = false; //It indicates if it is the tail of a snake or the upper side of a ladder

    public Square(int number, Effect effect) {
        super(number, effect);
    }
    public boolean isADestination() {
        return isADestination;
    }

    public void setIsADestination(boolean ADestination) {
        this.isADestination = ADestination;
    }

    public Effect getEffect() {
        return this.effect;
    }

    @Override
    public String toString() {
        return "Square{" +
                "number=" + number +
                "} with effect " + (this.getEffect());
    }
}
