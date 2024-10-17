package main.dice;

public class StandardDice implements Dice{
    private int numDice;
    private final int N_FACES = 6;

    public StandardDice(int numDice) {
        this.numDice = numDice;
    }

    @Override
    public int roll(int position) {
        int result = 0;
        for (int i = 0; i < numDice; i++) {
            result += (int) (Math.random() * N_FACES) + 1;
        }
        return result;
    }
}
