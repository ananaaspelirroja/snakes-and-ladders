package main.memento;

import main.components.Deck;
import main.components.Square;
import main.components.effects.DoNothingEffect;
import main.components.effects.MoveEffect;
import main.components.effects.ChanceEffect;
import main.components.effects.SpringEffect;
import main.components.effects.GuestEffect;
import main.components.effects.BenchEffect;
import main.components.effects.DrawACardEffect;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Board implements Serializable{

    private final int nColumns;
    private final int nRows;
    private final int nLadders;
    private final int nSnakes;
    private final int nBonusSquares;
    private final int nRestSquares;
    private final int nDrawCardSquares;  // New parameter for DrawACardEffect squares
    private final boolean otherCards;
    @Serial
    private static final long serialVersionUID = 6456379889023508602L;

    private Map<Integer, Square> grid;

    // Method that returns a random empty square
    private static int getRandomEmptySquare(Board board, Map<Integer, Boolean> reservedDestinations) {
        Random rand = new Random();
        int position = 0;
        int attempts = 0;
        int maxAttempts = board.getNumSquares(); // Limit the number of attempts

        // Genera una posizione casuale finché non trovi una casella vuota o raggiungi il massimo tentativi
        while (attempts < maxAttempts) {
            position = rand.nextInt(board.getNumSquares() - 1) + 1;  // Ottieni una posizione casuale
            attempts++;

            // Se la posizione è adatta, esci dal ciclo
            if (isSuitable(board, position) && !reservedDestinations.containsKey(position)) {
                break;
            }
        }

        // Se non viene trovata una casella vuota entro il numero massimo di tentativi, lancia un'eccezione
        if (attempts >= maxAttempts) {
            throw new IllegalStateException("Unable to find an empty square after " + maxAttempts + " attempts.");
        }

        return position;
    }


    private static boolean isSuitable(Board board, int position) {
        Square square = board.getSquareFromNumber(position);  // Access square through the getter method
        // Check if the square has DoNothingEffect and is not a destination of a ladder or snake
        return ((square.getEffect() instanceof DoNothingEffect) && (!square.isADestination()));
    }

    public int getnRows() {
        return this.nRows;
    }

    public int getnColumns() {
        return this.nColumns;
    }

    public static class Builder {

        Map<Integer, Boolean> reservedDestinations = new HashMap<>();

        private int nColumns = 10;
        private int nRows = 10;
        private int nLadders = 5;
        private int nSnakes = 5;
        private int nBonusSquares = 0;  // Number of bonus squares (ChanceEffect, SpringEffect)
        private int nRestSquares = 0;   // Number of rest squares (GuestEffect, BenchEffect)
        private int nDrawCardSquares = 0; // Number of squares with DrawACardEffect
        private boolean otherCards = false;

        public Builder() {}

        public Builder nColumns(int nColumns) {
            this.nColumns = nColumns;
            return this;
        }

        public Builder nRows(int nRows) {
            this.nRows = nRows;
            return this;
        }

        public Builder nLadders(int nLadders) {
            this.nLadders = nLadders;
            return this;
        }

        public Builder nSnakes(int nSnakes) {
            this.nSnakes = nSnakes;
            return this;
        }

        public Builder nBonusSquares(int nBonusSquares) {
            this.nBonusSquares = nBonusSquares;
            return this;
        }

        public Builder nRestSquares(int nRestSquares) {
            this.nRestSquares = nRestSquares;
            return this;
        }

        public Builder nDrawCardSquares(int nDrawCardSquares) {
            this.nDrawCardSquares = nDrawCardSquares;
            return this;
        }

        public Builder otherCards(boolean otherCards) {
            this.otherCards = otherCards;
            return this;
        }

        public Board build() {
            // Verify the size of the main.board
            if (nRows * nColumns < 9 || nRows * nColumns > 144) {
                throw new IllegalArgumentException("The board must have between 9 and 144 squares.");
            }

            // Verify that there is enough space for ladders, snakes, bonus squares, rest squares, and draw card squares
            int maxLaddersAndSnakes = (nRows * nColumns) / 2; // Each ladder/snake uses 2 squares
            if (nLadders + nSnakes > maxLaddersAndSnakes) {
                throw new IllegalArgumentException("The total number of ladders and snakes exceeds the available limit.");
            }

            // Verify that the number of special squares (bonus, rest, draw card) does not exceed the available space
            int totalSpecialSquares = nBonusSquares + nRestSquares + nDrawCardSquares + 1; // the final square
            int availableSpaces = nRows * nColumns - (nLadders * 2 + nSnakes * 2);
            if (totalSpecialSquares > availableSpaces) {
                throw new IllegalArgumentException("The number of special squares (bonus, rest, and draw card) exceeds the available space.");
            }

            if (otherCards && (nDrawCardSquares == 0)) {
                throw new IllegalArgumentException("Cannot enable other cards if there are no DrawACardSquares.");
            }

            Board board = new Board(this);

            board.grid = new HashMap<>();
            int key = 1; // Start from square 1

            // Build the main.board with regular squares
            for (int i = 0; i < board.nRows; i++) {
                for (int j = 0; j < board.nColumns; j++) {
                    Square square = new Square(key, new DoNothingEffect());
                    board.grid.put(key, square);

                    key++;
                }
            }

            // Aggiungi scale
            for (int i = 0; i < nLadders; i++) {
                int start = getRandomEmptySquare(board, reservedDestinations);  // Get an empty square
                int end = getRandomEmptySquare(board, reservedDestinations);    // Get a second empty square for the ladder end
                while (end <= start) {                    // The end square must be after the start square
                    end = getRandomEmptySquare(board, reservedDestinations);
                }
                Square endSquare = new Square(end, new DoNothingEffect());
                endSquare.setIsADestination(true);
                board.grid.put(start, new Square(start, new MoveEffect(endSquare)));

                // Aggiungi la destinazione alle caselle riservate
                reservedDestinations.put(end, true);

                System.out.println("Ladder from square " + start + " to " + end);
            }

            // Aggiungi serpenti
            for (int i = 0; i < nSnakes; i++) {
                int start = getRandomEmptySquare(board, reservedDestinations);  // Get an empty square
                int end = getRandomEmptySquare(board, reservedDestinations);    // Get a second empty square for the snake end
                while (end >= start) {                    // The end square must be before the start square
                    end = getRandomEmptySquare(board, reservedDestinations);
                }
                Square endSquare = new Square(end, new DoNothingEffect());
                endSquare.setIsADestination(true);
                board.grid.put(start, new Square(start, new MoveEffect(endSquare)));

                // Aggiungi la destinazione alle caselle riservate
                reservedDestinations.put(end, true);

                System.out.println("Snake from square " + start + " to " + end);
            }

            // Aggiungi caselle bonus, riposo, ecc.
            for (int i = 0; i < nBonusSquares; i++) {
                int position = getRandomEmptySquare(board, reservedDestinations);
                if (i % 2 == 0) {
                    board.grid.put(position, new Square(position, new ChanceEffect()));
                    System.out.println("Bonus square (ChanceEffect) at position " + position);
                } else {
                    board.grid.put(position, new Square(position, new SpringEffect()));
                    System.out.println("Bonus square (SpringEffect) at position " + position);
                }
            }

            // Add rest squares (GuestEffect, BenchEffect)
            for (int i = 0; i < nRestSquares; i++) {
                int position = getRandomEmptySquare(board, reservedDestinations);
                // Alternate between GuestEffect and BenchEffect
                if (i % 2 == 0) {
                    board.grid.put(position, new Square(position, new GuestEffect()));
                    // Linea di debug per mostrare le caselle di riposo
                    System.out.println("Rest square (GuestEffect) at position " + position);
                } else {
                    board.grid.put(position, new Square(position, new BenchEffect()));
                    // Linea di debug per mostrare le caselle di riposo
                    System.out.println("Rest square (BenchEffect) at position " + position);
                }
            }

            // Add DrawACardEffect squares
            if(nDrawCardSquares > 0){
                Deck deck = Deck.getInstance(otherCards);
                System.out.println("Deck created with setting " + deck.getNoStoppingCard() + "\n");
                for (int i = 0; i < nDrawCardSquares; i++) {
                    int position = getRandomEmptySquare(board, reservedDestinations);
                    board.grid.put(position, new Square(position, new DrawACardEffect()));
                    // Linea di debug per mostrare le caselle con DrawACardEffect
                    System.out.println("Draw Card square at position " + position);
                }
            }

            System.out.println("Board created! \n");
            return board;
        }
    }

    private Board(Builder builder) {
        this.nColumns = builder.nColumns;
        this.nRows = builder.nRows;
        this.nLadders = builder.nLadders;
        this.nSnakes = builder.nSnakes;
        this.nBonusSquares = builder.nBonusSquares;
        this.nRestSquares = builder.nRestSquares;
        this.nDrawCardSquares = builder.nDrawCardSquares;
        this.otherCards = builder.otherCards;
    }

    public boolean isOtherCards() {
        return otherCards;
    }

    public int getNumSquares() {
        return nColumns * nRows;
    }

    public Square getSquareFromNumber(int number) {
        if (grid.containsKey(number)) {
            return grid.get(number);  // Returns the corresponding square if it exists
        } else {
            System.out.println("No square found for position: " + number);
            return null;  // Returns null if the square does not exist
        }
    }

    //MEMENTO

    public BoardMemento save() {
        return new BoardMemento();
    }


    public void restore(BoardMemento m) {

        Builder builder = new Builder()
                .nColumns(m.nColumns)
                .nRows(m.nRows)
                .nLadders(m.nLadders)
                .nSnakes(m.nSnakes)
                .nBonusSquares(m.nBonusSquares)
                .nRestSquares(m.nRestSquares)
                .nDrawCardSquares(m.nDrawCardSquares)
                .otherCards(m.otherCards);

        Board restoredBoard = builder.build();
        System.out.println("Board state restored!");
    }

    // Classe Memento interna per salvare lo stato della board
    class BoardMemento implements Memento, Serializable {
        @Serial
        private static final long serialVersionUID = 987654321L;
        private final int nColumns;
        private final int nRows;
        private final int nLadders;
        private final int nSnakes;
        private final int nBonusSquares;
        private final int nRestSquares;
        private final int nDrawCardSquares;
        private final boolean otherCards;

        // Costruttore del Memento che salva lo stato corrente della board
        private BoardMemento() {
            this.nColumns = Board.this.nColumns;
            this.nRows = Board.this.nRows;
            this.nLadders = Board.this.nLadders;
            this.nSnakes = Board.this.nSnakes;
            this.nBonusSquares = Board.this.nBonusSquares;
            this.nRestSquares = Board.this.nRestSquares;
            this.nDrawCardSquares = Board.this.nDrawCardSquares;
            this.otherCards = Board.this.otherCards;
        }

        private Board getOriginator() {
            return Board.this;
        }
        public int getNColumns() {
            return nColumns;
        }

        public int getNRows() {
            return nRows;
        }

        public int getNLadders() {
            return nLadders;
        }

        public int getNSnakes() {
            return nSnakes;
        }

        public int getNBonusSquares() {
            return nBonusSquares;
        }

        public int getNRestSquares() {
            return nRestSquares;
        }

        public int getNDrawCardSquares() {
            return nDrawCardSquares;
        }

        public boolean isOtherCards() {
            return otherCards;
        }
    }

}
