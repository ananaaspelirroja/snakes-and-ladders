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
    private final int nDrawCardSquares;  // Nuovo parametro per le caselle DrawACardEffect
    private final boolean otherCards;

    private Map<Integer, Square> grid;

    // Metodo che restituisce una casella vuota casuale
    private static int getRandomEmptySquare(Board board) {
        Random rand = new Random();
        int position;
        do {
            position = rand.nextInt(board.getNumSquares() -1 ) + 1;  // Ottieni una posizione casuale tra 1 e il numero massimo di caselle -1
        } while (!isSuitable(board, position)); // Ripeti finché trovi una casella con effetto DoNothingEffect (cioè una casella vuota)
        return position;
    }

    private static boolean isSuitable(Board board, int position){
        Square square = board.grid.get(position);
        return ((square.getEffect() instanceof DoNothingEffect) && (!square.isADestination));
    }

    public static class Builder {

        private int nColumns = 10;
        private int nRows = 10;
        private int nLadders = 5;
        private int nSnakes = 5;
        private int nBonusSquares = 0;  // Numero di caselle bonus (ChanceEffect, SpringEffect)
        private int nRestSquares = 0;   // Numero di caselle di riposo (GuestEffect, BenchEffect)
        private int nDrawCardSquares = 0; // Numero di caselle con DrawACardEffect
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
            // Verifica delle dimensioni della board
            if (nRows * nColumns < 9 || nRows * nColumns > 144) {
                throw new IllegalArgumentException("La board deve avere tra 9 e 144 caselle.");
            }

            // Verifica che ci sia spazio sufficiente per scale, serpenti, caselle bonus, riposo e draw card
            int maxLaddersAndSnakes = (nRows * nColumns) / 2; // Ogni scala/serpente usa 2 caselle
            if (nLadders + nSnakes > maxLaddersAndSnakes) {
                throw new IllegalArgumentException("Il numero totale di scale e serpenti supera il limite disponibile.");
            }

            // Verifica che il numero di caselle speciali (bonus, rest, draw card) non superi lo spazio disponibile
            int totalSpecialSquares = nBonusSquares + nRestSquares + nDrawCardSquares;
            int availableSpaces = nRows * nColumns - (nLadders * 2 + nSnakes * 2);
            if (totalSpecialSquares > availableSpaces) {
                throw new IllegalArgumentException("Il numero di caselle speciali (bonus, rest e draw card) supera lo spazio disponibile.");
            }

            if (otherCards && (nDrawCardSquares == 0)) {
                throw new IllegalArgumentException("Cannot enable other cards if there are no DrawACardSquare is false.");
            }

            Board board = new Board(this);

            board.grid = new HashMap<>();
            int key = 1; // Inizia dalla casella 1

            // Costruisci la board con caselle normali
            for (int i = 0; i < board.nRows; i++) {
                for (int j = 0; j < board.nColumns; j++) {
                    board.grid.put(key, new Square(key, new DoNothingEffect()));
                    key++;
                }
            }

            // Aggiungi scale
            for (int i = 0; i < nLadders; i++) {
                int start = getRandomEmptySquare(board);  // Ottieni una casella libera
                int end = getRandomEmptySquare(board);    // Ottieni una seconda casella libera per la fine della scala
                while (end <= start) {                    // La casella finale deve essere dopo la casella iniziale
                    end = getRandomEmptySquare(board);
                }
                Square endSquare = new Square(end, new DoNothingEffect());
                endSquare.setIsADestination(true);
                board.grid.put(start, new Square(start, new MoveEffect(endSquare)));
            }

            // Aggiungi serpenti
            for (int i = 0; i < nSnakes; i++) {
                int start = getRandomEmptySquare(board);  // Ottieni una casella libera
                int end = getRandomEmptySquare(board);    // Ottieni una seconda casella libera per la fine del serpente
                while (end >= start) {                    // La casella finale deve essere prima della casella iniziale
                    end = getRandomEmptySquare(board);
                }
                Square endSquare = new Square(end, new DoNothingEffect());
                endSquare.setIsADestination(true);
                board.grid.put(start, new Square(start, new MoveEffect(endSquare)));
            }

            // Aggiungi caselle bonus (ChanceEffect, SpringEffect)
            for (int i = 0; i < nBonusSquares; i++) {
                int position = getRandomEmptySquare(board);
                // Alterna tra ChanceEffect e SpringEffect
                if (i % 2 == 0) {
                    board.grid.put(position, new Square(position, new ChanceEffect()));
                } else {
                    board.grid.put(position, new Square(position, new SpringEffect()));
                }
            }

            // Aggiungi caselle di riposo (GuestEffect, BenchEffect)
            for (int i = 0; i < nRestSquares; i++) {
                int position = getRandomEmptySquare(board);
                // Alterna tra GuestEffect e BenchEffect
                if (i % 2 == 0) {
                    board.grid.put(position, new Square(position, new GuestEffect()));
                } else {
                    board.grid.put(position, new Square(position, new BenchEffect()));
                }
            }

            // Aggiungi caselle DrawACardEffect
            for (int i = 0; i < nDrawCardSquares; i++) {
                int position = getRandomEmptySquare(board);
                board.grid.put(position, new Square(position, new DrawACardEffect()));
            }

            Deck deck = Deck.getInstance(otherCards);

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

    public Square getSquareFromNumber(int newPosition) {
        return grid.get(newPosition);
    }
}
