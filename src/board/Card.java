package board;

import board.effects.Effect;

public class Card extends Element {

    protected int number;
    protected Effect effect;

    public Card(int number, Effect effect) {
        super(number, effect);
    }
    @Override
    public String toString() {
        return "Card{" +
                "effect=" + effect +
                '}';
    }
    public Effect getEffect() {
        return effect;
    }
}
