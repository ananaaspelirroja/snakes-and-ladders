package main.game;

import main.board.Board;
import main.board.Square;
import main.components.Player;
import main.dice.Dice;

import java.util.LinkedList;

public class Game {

    private final int nPlayers;
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
/*
    public void start() throws InterruptedException {
        while (!terminated) {
            Player player = getCurrentPlayer();  // Get the current player
            int currentPosition = player.getCurrentPosition();
            if (!player.hasTurnsToWait()) {  // Play only if the player does not have to wait
                int number = dice.roll(currentPosition);  // Roll the main.dice
                turn(number, player);  // Execute the turn
            }
            System.out.println(player.getSquaresCrossed());

            // Add a delay to better visualize the messages
            Thread.sleep(2000);  // 2 seconds pause between turns

            // Move the player to the end of the list (end of the turn)
            players.addLast(players.removeFirst());
        }
    }
    */

    public Player getCurrentPlayer() throws InterruptedException {
        Player player = players.getFirst();  // Get the first player
        while (player.hasTurnsToWait()) {  // If they need to wait, reduce the waiting turns
            player.setTurnsToWait(player.getTurnsToWait() - 1);
            System.out.println("Player " + player.getNickname() + " has to wait " + player.getTurnsToWait() + " more turns.");

            // Add a delay to visualize the message
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

        // Check if the new position exceeds the number of squares
        if (newPosition > totalSquares) {
            // Calculate the difference and make the player go back
            int excess = newPosition - totalSquares;
            newPosition = totalSquares - excess;
            System.out.println("Player " + player.getNickname() + " has exceeded the end! Goes back " + excess + " squares.");
        }

        Square square = board.getSquareFromNumber(newPosition);
        player.getSquaresCrossed().addLast(square);
        if (newPosition == totalSquares) {
            this.terminated = true;
            System.out.println("Player " + player.getNickname() + " has won!");
        } else {
            System.out.println("Player " + player.getNickname() + " is on square number " + newPosition);
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
}