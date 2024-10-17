package main.dice;

public class DiceConfiguration { //For Memento pattern

    private final int numDice;
    private final boolean doubleSixEnabled;
    private final boolean oneDiceAtEndEnabled;


    public DiceConfiguration(int numDice, boolean doubleSixEnabled, boolean oneDiceAtEndEnabled) {
        this.numDice = numDice;
        this.doubleSixEnabled = doubleSixEnabled;
        this.oneDiceAtEndEnabled = oneDiceAtEndEnabled;
    }



    // MEMENTO IMPLEMENTATION


    public DiceMemento getMemento() {
        return new DiceMemento();
    }


    public void setMemento(DiceMemento m) {
        if (this != m.getOriginator()) {
            throw new IllegalArgumentException("Memento does not belong to this dice configuration");
        }

        DiceConfiguration restoredDiceConfig = new DiceConfiguration(m.numDice, m.doubleSixEnabled, m.oneDiceAtEndEnabled);
        System.out.println("Dice configuration restored!");
    }


    private class DiceMemento {

        private final int numDice;
        private final boolean doubleSixEnabled;
        private final boolean oneDiceAtEndEnabled;


        private DiceMemento() {
            this.numDice = DiceConfiguration.this.numDice;
            this.doubleSixEnabled = DiceConfiguration.this.doubleSixEnabled;
            this.oneDiceAtEndEnabled = DiceConfiguration.this.oneDiceAtEndEnabled;
        }

        private DiceConfiguration getOriginator() {
            return DiceConfiguration.this;
        }
    }
}
