package main.components;

import main.components.effects.Effect;

public class Card extends Element {

    public Card(int number, Effect effect) {
        super(number, effect);
    }
    @Override
    public String toString() {
        return "Card{" +
                "effect=" + effect.toString() +
                '}';
    }
}
