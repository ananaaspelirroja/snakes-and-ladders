package board;

import Game.Game;
import components.Player;

public class DrawACardSquare extends AbstractSquare {


    protected DrawACardSquare(int number) {
        super(number);
    }

    @Override
    protected Square create() {
        return null;
    }

    @Override
    public void applyEffect(Game game, Player player) {

    }

    @Override
    public int getNumber() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }
}
