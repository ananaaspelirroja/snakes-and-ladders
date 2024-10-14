package board;

public abstract class AbstractSquare implements Square {

    protected int number;
    protected String name;

    protected AbstractSquare(int number) {
        this.number = number;
    }

    protected abstract Square create();
}
