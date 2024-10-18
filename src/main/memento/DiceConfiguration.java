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


    public DiceConfiguration(int numDice, boolean doubleSixEnabled, boolean oneDiceAtEndEnabled) {
        this.numDice = numDice;
        this.doubleSixEnabled = doubleSixEnabled;
        this.oneDiceAtEndEnabled = oneDiceAtEndEnabled;
    }



    // MEMENTO IMPLEMENTATION


    public DiceMemento save() {
        return new DiceMemento();
    }


    public void restore(DiceMemento m) {

        DiceConfiguration restoredDiceConfig = new DiceConfiguration(m.numDice, m.doubleSixEnabled, m.oneDiceAtEndEnabled);
        System.out.println("Dice configuration restored!");
    }



    class DiceMemento implements Memento, Serializable {
        @Serial
        private static final long serialVersionUID = 1122334455L;
        private final int numDice;
        private final boolean doubleSixEnabled;
        private final boolean oneDiceAtEndEnabled;


        private DiceMemento() {
            this.numDice = DiceConfiguration.this.numDice;
            this.doubleSixEnabled = DiceConfiguration.this.doubleSixEnabled;
            this.oneDiceAtEndEnabled = DiceConfiguration.this.oneDiceAtEndEnabled;
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

        private DiceConfiguration getOriginator() {
            return DiceConfiguration.this;
        }
    }
}
