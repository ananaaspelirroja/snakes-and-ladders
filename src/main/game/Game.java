package main.game;

import main.board.Board;
import main.board.Square;
import main.components.Player;
import main.dice.Dice;

import java.util.LinkedList;

public class Game {

    private int nPlayers;
    private LinkedList<Player> players = new LinkedList<>();
    private Dice dice;
    private Board board;
    private boolean terminated = false;
    private AdvanceStrategy strategy;

    public Game(int nPlayers, LinkedList<String> nicknames, Dice dice, Board board, AdvanceStrategy strategy) {
        this.board = board;
        this.nPlayers = nPlayers;
        for (int i = 0; i < nicknames.size(); i++) {
            Player player = new Player((i + 1), nicknames.get(i), this.board);
            players.addLast(player);
        }
        this.dice = dice;
        this.strategy = strategy;
    }

    public void start() throws InterruptedException {
        while (!terminated) {
            strategy.advance(this);
        }
    }


    public Player getCurrentPlayer() throws InterruptedException {
        Player player = players.getFirst();  // Get the first player
        while (player.hasTurnsToWait()) {  // If they need to wait, reduce the waiting turns
            player.setTurnsToWait(player.getTurnsToWait() - 1);
            if (player.getTurnsToWait() > 0) {
                System.out.println("Player " + player.getNickname() + " has to wait " + player.getTurnsToWait() + " more turns. \n");
            } else {
                System.out.println("Player " + player.getNickname() + " will play on the next turn! \n");
            }

            Thread.sleep(2000);  // 2 seconds pause to give time to read the message

            // Move the player to the end of the list to wait for the next turn
            players.addLast(players.removeFirst());
            player = players.getFirst();  // Get the next player
        }
        return player;
    }


    public void turn(int number, Player player) {
        int newPosition = player.advanceOf(number);
        int totalSquares = board.getNumSquares();

        if (newPosition > totalSquares) {
            // Calculate the difference and make the player go back
            int excess = newPosition - totalSquares;
            newPosition = totalSquares - excess;
            System.out.println("Player " + player.getNickname() + " has exceeded the end! Goes back " + excess + " squares. \n");
        }

        Square square = board.getSquareFromNumber(newPosition);
        player.getSquaresCrossed().addLast(square);
        if (newPosition == totalSquares) {
            this.terminated = true;
            System.out.println("Player " + player.getNickname() + " has won! \n");
        } else {
            //System.out.println("Player " + player.getNickname() + " is on square number " + newPosition); - DEBUG
            square.applyEffect(this, player);
        }
    }

    public Dice getDice() {
        return this.dice;
    }

    public void terminate() {
        this.terminated = true;
    }

    public void movePlayerToEnd() {
        players.addLast(players.removeFirst());
    }

    //METODI PER MEMENTO

    // Create and return the memento
    public GameMemento getMemento() {
        return new GameMemento();
    }

    // Restore the game state from the memento
    public void setMemento(GameMemento m) {
        if (this != m.getOriginator()) {
            throw new IllegalArgumentException("Memento does not belong to this game");
        }

        this.nPlayers = nPlayers;
        this.players = new LinkedList<>(m.players);
        this.terminated = m.terminated;
        this.strategy = m.strategy;

        System.out.println("Game state restored!");
    }

    // Inner Memento class
    private class GameMemento {

        private final int nPlayers;
        private final LinkedList<Player> players;
        private final boolean terminated;
        private final AdvanceStrategy strategy;


        GameMemento() {
            this.nPlayers = Game.this.nPlayers;
            this.players = new LinkedList<>(Game.this.players);  // Save a copy of the players
            this.terminated = Game.this.terminated;
            this.strategy = Game.this.strategy;
        }

        // Get the originator (Game instance)
        Game getOriginator() {
            return Game.this;
        }
    }
}