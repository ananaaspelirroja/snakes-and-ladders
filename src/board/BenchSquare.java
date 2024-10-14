package board;

import Game.Game;
import components.Player;

public class BenchSquare extends AbstractSquare {

    protected final String name = "BenchSquare";
    protected int number;
    private final static int TURNS_TO_WAIT = 1;

    private BenchSquare(int number) {
        super(number);
    }

    @Override
    public int getNumber() {
        return this.number;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    protected Square create() {
        return new BenchSquare(number);
    }


    @Override
    public void applyEffect(Game game, Player player) {
        player.setTurnsToWait(TURNS_TO_WAIT);
        System.out.println("Player " + player.getNickname() + " got on a standard square! \n");
    }
}
