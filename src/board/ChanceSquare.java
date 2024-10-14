package board;

import Game.Game;
import components.Player;

public class ChanceSquare extends AbstractSquare {

    protected final String name = "ChanceSquare";
    protected int number;

    protected ChanceSquare(int number) {
        super(number);
    }

    @Override
    protected Square create() {
        return new ChanceSquare(number);
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
    public void applyEffect(Game game, Player player) {
        int number = game.getDice().roll(player.getCurrentPosition());
        game.turn(number, player);
        System.out.println("Player " + player.getNickname() + " got on a chance square! The dice are rolling again! \n");
    }
}
