package main.dice;

import main.memento.Board;

import java.io.*;

public class SingleDieDecorator extends DiceDecorator implements Serializable {

    private int lowerBound;
    private int upperBound;
    private transient Board board;  // Marked as transient, not serialized

    @Serial
    private static final long serialVersionUID = -6278977291285639519L;

    // Main constructor
    public SingleDieDecorator(Dice dice, Board board) {
        super(dice);
        this.board = board;
        initializeBounds();
    }

    // Default constructor for deserialization
    public SingleDieDecorator() {
        super(new StandardDice(2));  // Default dice with 2 dice
    }

    // Method to initialize bounds based on the board
    private void initializeBounds() {
        if (board != null) {
            int numSquares = board.getNumSquares();
            this.lowerBound = numSquares - 6;
            this.upperBound = numSquares - 1;
        }
    }

    @Override
    public int roll(int position) {
        if (position <= upperBound && position >= lowerBound) {
            System.out.println("You are almost at the end! Rolling only 1 dice now! \n");
            return (int) (Math.random() * 6) + 1;
        } else {
            return dice.roll(position);
        }
    }

}
