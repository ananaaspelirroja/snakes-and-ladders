import java.util.LinkedList;

public class Game {

    private final int nPlayers;
    private final int nColumns;
    private final int nRows;
    private final int nLadders;
    private final int nSnakes;
    private final int nDice;
    private final boolean throwSingleDice;
    private final boolean doubleSix;
    private final boolean restSquares;
    private final boolean bonusSquare;
    private final boolean chanceSquare;
    private final boolean otherCards;

    private LinkedList<Player> players = new LinkedList<>();


    public static class Builder {

        private final int nPlayers;
        private int nColumns = 10;
        private int nRows = 10;
        private int nLadders = 5;
        private int nSnakes = 5;
        private int nDice = 2;
        private boolean throwSingleDice = false;
        private boolean doubleSix = false;
        private boolean restSquares = false;
        private boolean bonusSquare = false;
        private boolean chanceSquare = false;
        private boolean otherCards = false;

        public Builder(int nPlayers) {
            this.nPlayers = nPlayers;
        }

        public void nColumns(int nColumns) {
            this.nColumns = nColumns;
        }

        public void nRows(int nRows) {
            this.nRows = nRows;
        }

        public void nLadders(int nLadders) {
            this.nLadders = nLadders;
        }

        public void nSnakes(int nSnakes) {
            this.nSnakes = nSnakes;
        }

        public void nDice(int nDice) {
            this.nDice = nDice;
        }

        public void throwSingleDice(boolean throwSingleDice) {
            this.throwSingleDice = throwSingleDice;
        }

        public void doubleSix(boolean doubleSix) {
            this.doubleSix = doubleSix;
        }

        public void restSquares(boolean restSquares) {
            this.restSquares = restSquares;
        }

        public void bonusSquare(boolean bonusSquare) {
            this.bonusSquare = bonusSquare;
        }

        public void chanceSquare(boolean chanceSquare) {
            this.chanceSquare = chanceSquare;
        }

        public void otherCards(boolean otherCards) {
            this.otherCards = otherCards;
        }

        public Game build(){
            return new Game(this);
        }
    } //Builder

    private Game(Builder builder){

        this.nPlayers = builder.nPlayers;
        this.nColumns = builder.nColumns;
        this.nRows = builder.nRows;
        this.nLadders = builder.nLadders;
        this.nSnakes = builder.nSnakes;
        this.nDice = builder.nDice;
        this.throwSingleDice = builder.throwSingleDice;
        this.doubleSix = builder.doubleSix;
        this.restSquares = builder.restSquares;
        this.bonusSquare = builder.bonusSquare;
        this.chanceSquare = builder.chanceSquare;
        this.otherCards = builder.otherCards;
    }

    public void startGame(){
        boolean terminated = false;
        while(!terminated){
            Player player = getCurrentPlayer();
            int number = dice.roll();
            int newPosition = player.advanceOf(number);
            if(newPosition == numSquares){
                terminated = true;
                print('Player ' + player.getNickname() + " won!");
            }
            else {
                Square square = getSquareFromNumber(newPosition);
                square.effect(player);
            }
        }
    }

}
