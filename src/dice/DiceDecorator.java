package dice;

public abstract class DiceDecorator implements Dice {
    protected Dice dice;
    public DiceDecorator(Dice dice) {
        this.dice = dice;
    }

}
