package dice;

import components.Board;

class SingleDieDecorator extends DiceDecorator {

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
            return (int) (Math.random() * 6) + 1;
        } else {
            return dice.roll(position);
        }
    }
}