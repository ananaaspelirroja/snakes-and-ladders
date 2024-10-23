package main.memento;

import main.dice.Dice;
import main.dice.DoubleSixDecorator;
import main.dice.SingleDieDecorator;
import main.dice.StandardDice;

import java.io.Serial;
import java.io.Serializable;

public class DiceConfiguration implements Serializable{ //For Memento pattern

    private final int numDice;
    private final boolean doubleSixEnabled;
    private final boolean oneDiceAtEndEnabled;
    private Board board;
    @Serial
    private static final long serialVersionUID = 8876296238589836171L;


    public DiceConfiguration(int numDice, boolean doubleSixEnabled, boolean oneDiceAtEndEnabled, Board board) {
        this.numDice = numDice;
        this.doubleSixEnabled = doubleSixEnabled;
        this.oneDiceAtEndEnabled = oneDiceAtEndEnabled;
        this.board = board;
    }



    // MEMENTO IMPLEMENTATION


    public DiceMemento save() {
        return new DiceMemento();
    }


    public void restore(DiceMemento m) {

        DiceConfiguration restoredDiceConfig = new DiceConfiguration(m.numDice, m.doubleSixEnabled, m.oneDiceAtEndEnabled, m.board);
        System.out.println("Dice configuration restored!");
    }

    public Dice createDice() {
        Dice dice = null;
        if(numDice == 1){
            dice = new StandardDice(1);
            return dice;
        } else{
            dice = new StandardDice(2);
            if(doubleSixEnabled){
                dice = new DoubleSixDecorator(dice);
            }
            if(oneDiceAtEndEnabled){
                dice = new SingleDieDecorator(dice, board);
            }
            return dice;
        }
    }


    class DiceMemento implements Memento, Serializable {
        @Serial
        private static final long serialVersionUID = 1122334455L;
        private final int numDice;
        private final boolean doubleSixEnabled;
        private final boolean oneDiceAtEndEnabled;
        private final Board board;


        private DiceMemento() {
            this.numDice = DiceConfiguration.this.numDice;
            this.doubleSixEnabled = DiceConfiguration.this.doubleSixEnabled;
            this.oneDiceAtEndEnabled = DiceConfiguration.this.oneDiceAtEndEnabled;
            this.board = DiceConfiguration.this.board;
        }

        public int getNumDice() {
            return numDice;
        }

        public boolean isDoubleSixEnabled() {
            return doubleSixEnabled;
        }

        public boolean isOneDiceAtEndEnabled() {
            return oneDiceAtEndEnabled;
        }

        public Board getBoard() {
            return board;
        }

        private DiceConfiguration getOriginator() {
            return DiceConfiguration.this;
        }
    }
}
