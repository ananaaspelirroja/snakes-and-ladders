package main.dice;

import java.io.Serializable;

public class DoubleSixDecorator extends DiceDecorator implements Serializable {
    public DoubleSixDecorator(Dice dice){
        super(dice);
    }

    @Override
    public int roll(int position) {
        int result = dice.roll(position);
        if (result == 12) {
            System.out.println("Doppio sei! Tira di nuovo.");
            result = dice.roll(position);
        }
        return result;
    }
}
