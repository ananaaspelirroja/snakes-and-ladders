package board;

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
    public void applyEffect(Player player) {
        System.out.println("Player " + player.getNickname() + " got on a standard square! \n");
    }
}
