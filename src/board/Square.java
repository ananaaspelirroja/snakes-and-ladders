package board;

import board.effects.Effect;

public class Square extends Element {

    protected int number;
    protected Effect effect;
    protected boolean isADestination = false; //It indicates if it is the tail of a snake or the upper side of a ladder

    public Square(int number, Effect effect) {
        super(number, effect);
    }
    public boolean isADestination() {
        return isADestination;
    }

    public void setIsADestination(boolean ADestination) {
        isADestination = ADestination;
    }

    public Effect getEffect() {
        return effect;
    }

    @Override
    public String toString() {
        return "Square{" +
                "number=" + number +
                '}';
    }
}
