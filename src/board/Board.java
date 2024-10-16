package board;

import board.effects.DoNothingEffect;
import board.effects.MoveEffect;
import board.effects.ChanceEffect;
import board.effects.SpringEffect;
import board.effects.GuestEffect;
import board.effects.BenchEffect;
import board.effects.DrawACardEffect;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Board {

    private final int nColumns;
    private final int nRows;
    private final int nLadders;
    private final int nSnakes;
    private final int nBonusSquares;
    private final int nRestSquares;
    private final int nDrawCardSquares;  // New parameter for DrawACardEffect squares
    private final boolean otherCards;

    private Map<Integer, Square> grid;

    // Method that returns a random empty square
    private static int getRandomEmptySquare(Board board) {
        Random rand = new Random();
        int position;
        int attempts = 0;
        int maxAttempts = board.getNumSquares(); // Limit the number of attempts
        System.out.println("Maximum number of attempts to find an empty square: " + maxAttempts);
        do {
            position = rand.nextInt(board.getNumSquares() - 1) + 1;  // Get a random position between 1 and the maximum number of squares
            attempts++;
            if (attempts > maxAttempts) {
                throw new IllegalStateException("Unable to find an empty square after " + maxAttempts + " attempts.");
            }
            System.out.println("Attempt " + attempts + ": square " + position);
        } while (!isSuitable(board, position)); // Repeat until finding a square with DoNothingEffect (i.e., an empty square)
        System.out.println("Empty square found at position: " + position);
        return position;
    }

    private static boolean isSuitable(Board board, int position) {
        Square square = board.getSquareFromNumber(position);  // Access square through the getter method
        // Check if the square has DoNothingEffect and is not a destination of a ladder or snake
        boolean isSuitable = (square.getEffect() instanceof DoNothingEffect) && (!square.isADestination());
        System.out.println("Is square " + position + " suitable? " + isSuitable);
        return isSuitable;
    }

    public static class Builder {

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
            // Verify the size of the board
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

            // Build the board with regular squares
            for (int i = 0; i < board.nRows; i++) {
                for (int j = 0; j < board.nColumns; j++) {
                    Square square = new Square(key, new DoNothingEffect());
                    board.grid.put(key, square);

                    if (square.getEffect() != null) {
                        System.out.println(square.getEffect().toString());  // Print the effect if not null
                    } else {
                        System.out.println("Null effect for square: " + key);
                    }

                    key++;
                }
            }

            // Add ladders
            for (int i = 0; i < nLadders; i++) {
                int start = getRandomEmptySquare(board);  // Get an empty square
                int end = getRandomEmptySquare(board);    // Get a second empty square for the ladder end
                while (end <= start) {                    // The end square must be after the start square
                    end = getRandomEmptySquare(board);
                }
                Square endSquare = new Square(end, new DoNothingEffect());
                endSquare.setIsADestination(true);
                board.grid.put(start, new Square(start, new MoveEffect(endSquare)));
            }

            // Add snakes
            for (int i = 0; i < nSnakes; i++) {
                int start = getRandomEmptySquare(board);  // Get an empty square
                int end = getRandomEmptySquare(board);    // Get a second empty square for the snake end
                while (end >= start) {                    // The end square must be before the start square
                    end = getRandomEmptySquare(board);
                }
                Square endSquare = new Square(end, new DoNothingEffect());
                endSquare.setIsADestination(true);
                board.grid.put(start, new Square(start, new MoveEffect(endSquare)));
            }

            // Add bonus squares (ChanceEffect, SpringEffect)
            for (int i = 0; i < nBonusSquares; i++) {
                int position = getRandomEmptySquare(board);
                // Alternate between ChanceEffect and SpringEffect
                if (i % 2 == 0) {
                    board.grid.put(position, new Square(position, new ChanceEffect()));
                } else {
                    board.grid.put(position, new Square(position, new SpringEffect()));
                }
            }

            // Add rest squares (GuestEffect, BenchEffect)
            for (int i = 0; i < nRestSquares; i++) {
                int position = getRandomEmptySquare(board);
                // Alternate between GuestEffect and BenchEffect
                if (i % 2 == 0) {
                    board.grid.put(position, new Square(position, new GuestEffect()));
                } else {
                    board.grid.put(position, new Square(position, new BenchEffect()));
                }
            }

            // Add DrawACardEffect squares
            if(nDrawCardSquares > 0){
                Deck deck = Deck.getInstance(otherCards);
                System.out.println("Deck created with setting " + deck.getNoStoppingCard());
                for (int i = 0; i < nDrawCardSquares; i++) {
                    int position = getRandomEmptySquare(board);
                    board.grid.put(position, new Square(position, new DrawACardEffect()));
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

}
