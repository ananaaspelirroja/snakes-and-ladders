package board;

import Game.Game;
import components.Player;

public class StandardSquare extends AbstractSquare {

    protected final String name = "StandardSquare";
    protected int number;

    private StandardSquare(int number) {
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
        return new StandardSquare(number);
    }


    @Override
    public void applyEffect(Game game, Player player) {
        player.setTurnsToWait(1);
        System.out.println("Player " + player.getNickname() + " got on a bench square! \n");
    }
}