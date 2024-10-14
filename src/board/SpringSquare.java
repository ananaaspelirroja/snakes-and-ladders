package board;

import Game.Game;
import components.Player;

public class SpringSquare extends AbstractSquare {

    protected final String name = "SpringSquare";
    protected int number;

    protected SpringSquare(int number) {
        super(number);
    }

    @Override
    protected Square create() {
        return new SpringSquare(number);
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
        game.turn(player.lastDiceRoll(), player);
        System.out.println("Player " + player.getNickname() + " got on a spring square! You are advancing again! \n");
    }
}
