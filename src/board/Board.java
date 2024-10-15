package board;

import board.effects.DoNothingEffect;

import java.util.HashMap;
import java.util.Map;

public class Board {

    private final int nColumns;
    private final int nRows;
    private final int nLadders;
    private final int nSnakes;
    private final boolean restSquare;
    private final boolean bonusSquare;
    private final boolean chanceSquare;
    private final boolean otherCards;

    private Map<Integer, Square> grid;



    public static class Builder {

        private int nColumns = 10;
        private int nRows = 10;
        private int nLadders = 5;
        private int nSnakes = 5;
        private boolean bonusSquare = false;
        private boolean chanceSquare = false;
        private boolean restSquare = false;
        private boolean otherCards = false;

        public Builder() {
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

        public void bonusSquare(boolean bonusSquare) {
            this.bonusSquare = bonusSquare;
        }

        public void chanceSquare(boolean chanceSquare) {
            this.chanceSquare = chanceSquare;
        }

        public void restSquare(boolean restSquare) {
            this.restSquare = restSquare;
        }

        public void otherCards(boolean otherCards) {
            this.otherCards = otherCards;
        }


        public Board build() {
            Board board = new Board(this);

            if (otherCards && !chanceSquare) { // Controllo su otherCards
                throw new IllegalArgumentException("Cannot put a No Stopping Card if there is no Chance Square is false");
            }
            board.grid = new HashMap<>();
            int key = 1; // Inizia dalla casella 1
            for (int i = 0; i < board.nRows; i++) {
                for (int j = 0; j < board.nColumns; j++) {
                    board.grid.put(key, new Square(key, new DoNothingEffect())); // Associa la chiave alla casella
                    key++;
                }
            }
            return board;
        } //Builder
    }

        private Board(Builder builder) {

            this.nColumns = builder.nColumns;
            this.nRows = builder.nRows;
            this.nLadders = builder.nLadders;
            this.nSnakes = builder.nSnakes;
            this.bonusSquare = builder.bonusSquare;
            this.chanceSquare = builder.chanceSquare;
            this.restSquare = builder.restSquare;
            this.otherCards = builder.otherCards;
        }

    public boolean isOtherCards() {
        return otherCards;
    }

    public int getNumSquares() {
            return (nColumns * nRows);
        }
        public Square getSquareFromNumber(int newPosition) {
            return grid.get(newPosition);
        }

}



