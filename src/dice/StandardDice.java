package dice;

public class StandardDice implements Dice{
    private int numDice;

    public StandardDice(int numDice) {
        this.numDice = numDice;
    }

    @Override
    public int roll(int position) {
        int result = 0;
        for (int i = 0; i < numDice; i++) {
            result += (int) (Math.random() * 6) + 1;
        }
        return result;
    }
}
