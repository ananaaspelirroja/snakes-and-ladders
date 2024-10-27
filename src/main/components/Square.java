package main.components;

import main.components.effects.Effect;

import java.io.Serial;
import java.io.Serializable;

public class Square extends Element {

    protected boolean isADestination = false; //It indicates if it is the tail of a snake or the upper side of a ladder
    @Serial
    private static final long serialVersionUID = -8982858501555795089L;

    public Square(int number, Effect effect) {
        super(number, effect);
    }
    public boolean isADestination() {
        return isADestination;
    }

    public void setIsADestination(boolean ADestination) {
        this.isADestination = ADestination;
    }

    @Override
    public String toString() {
        return "Square{" +
                "number=" + number +
                "} with effect " + (this.getEffect());
    }
}
