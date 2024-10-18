package main.dice;

import main.memento.Board;

import java.io.Serializable;

public class SingleDieDecorator extends DiceDecorator implements Serializable {

    private final int lowerBound;
    private final int upperBound;


    public SingleDieDecorator(Dice dice, Board board) {
        super(dice);
        int numSquares = board.getNumSquares();
        this.lowerBound = numSquares - 6;
        this.upperBound = numSquares - 1;
    }

    @Override
    public int roll(int position) {
        if (position <= upperBound && position >= lowerBound) {
            System.out.println("You are almost at the end! It's time to roll 1 dice! \n");
            return (int) (Math.random() * 6) + 1;
        } else {
            return dice.roll(position);
        }
    }
}