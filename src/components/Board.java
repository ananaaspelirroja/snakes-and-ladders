package components;

import squares.Square;

public class Board {

    private final int nColumns;
    private final int nRows;
    private final int nLadders;
    private final int nSnakes;
    private final boolean restSquare;
    private final boolean bonusSquare;
    private final boolean chanceSquare;

    private Square[][] grid;

    public static class Builder {

        private int nColumns = 10;
        private int nRows = 10;
        private int nLadders = 5;
        private int nSnakes = 5;
        private boolean bonusSquare = false;
        private boolean chanceSquare = false;

        private boolean restSquare = false;

        public Builder() {}

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


        public Board build(){
            Board board = new Board(this);
            board.grid = new Square[board.nRows][board.nColumns];
            return board;
        }
    } //Builder

    private Board(Builder builder){

        this.nColumns = builder.nColumns;
        this.nRows = builder.nRows;
        this.nLadders = builder.nLadders;
        this.nSnakes = builder.nSnakes;
        this.bonusSquare = builder.bonusSquare;
        this.chanceSquare = builder.chanceSquare;
        this.restSquare = builder.restSquare;
    }

    public int getNumSquares(){
        return (nColumns * nRows);
    }
}
