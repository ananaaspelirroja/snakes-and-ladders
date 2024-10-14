import components.Board;
import dice.Dice;

import java.util.LinkedList;

public class Game {

    private final int nPlayers;
    private final int nDice;
    private final boolean throwSingleDiceAtTheEnd;
    private final boolean doubleSix;
    private final boolean otherCards;

    private LinkedList<Player> players = new LinkedList<>();
    private Dice dice;
    private Board board;


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
        this.nDice = builder.nDice;
        this.throwSingleDiceAtTheEnd = builder.throwSingleDice;
        this.doubleSix = builder.doubleSix;
        this.otherCards = builder.otherCards;
    }

    public void initialise(LinkedList<String> nicknames){ //creare i player e il tabellone
        for(int i = 0; i < nicknames.size(); i++){
            Player player = new Player((i+1), nicknames.get(i));
            players.addLast(player);
        }

    }

    public void start(){
        boolean terminated = false;
        while(!terminated){
            Player player = getCurrentPlayer();
            int currentPosition = player.getCurrentPosition();
            int number = dice.roll(currentPosition);
            int newPosition = player.advanceOf(number);
            if(newPosition == board.getNumSquares()){
                terminated = true;
                System.out.println('Player ' + player.getNickname() + " won!");
            }
            else {
                Square square = getSquareFromNumber(newPosition);
                square.getEffect(player);
            }
        }
    }

    private Player getCurrentPlayer(){

    }

}
