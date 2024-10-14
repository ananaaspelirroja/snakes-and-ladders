package dice;

public class DoubleSixDecorator extends DiceDecorator {
    public DoubleSixDecorator(Dice dice){
        super(dice);
    }

    @Override
    public int roll(int position) {
        int result = dice.roll(position);
        if (result == 12) { // Controlla se la somma è 12 (doppio sei)
            System.out.println("Doppio sei! Tira di nuovo.");
            result = dice.roll(position); // Tira di nuovo i dadi
        }
        return result;
    }
}
