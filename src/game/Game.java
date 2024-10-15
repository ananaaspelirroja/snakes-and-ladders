package game;

import board.Board;
import board.Square;
import components.Player;
import dice.Dice;

import java.util.LinkedList;

public class Game {

    private final int nPlayers;
    private LinkedList<Player> players = new LinkedList<>();
    private Dice dice;
    private Board board;
    private boolean terminated = false;


    public Game(int nPlayers, LinkedList<String> nicknames, Dice dice, Board board) {
        this.board = board;
        this.nPlayers = nPlayers;
        for (int i = 0; i < nicknames.size(); i++) {
            Player player = new Player((i + 1), nicknames.get(i), this.board = board);
            players.addLast(player);
        }
        this.dice = dice;
    }

    public void start() throws InterruptedException {
        while (!terminated) {
            Player player = getCurrentPlayer();
            int currentPosition = player.getCurrentPosition();
            int number = dice.roll(currentPosition);
            turn(number, player);
            System.out.println(player.getSquaresCrossed());
            players.removeFirst();
        }
    }

    private Player getCurrentPlayer() throws InterruptedException {
        Player player = players.getFirst();
        players.addLast(player);
        if (player.hasTurnsToWait()) {
            player.setTurnsToWait((player.getTurnsToWait() - 1));
            getCurrentPlayer();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return player;
    }

    public void turn(int number, Player player) {
        int newPosition = player.advanceOf(number);
        int totalSquares = board.getNumSquares();

        // Controlla se la nuova posizione supera il numero di caselle
        if (newPosition > totalSquares) {
            // Calcola la differenza e fai tornare indietro il giocatore
            int excess = newPosition - totalSquares;
            newPosition = totalSquares - excess;
            System.out.println("Il giocatore " + player.getNickname() + " ha superato la fine! Torna indietro di " + excess + " caselle.");
        }

        if (newPosition == totalSquares) {
            this.terminated = true;
            System.out.println("Player " + player.getNickname() + " won!");
        } else {
            System.out.println("Player " + player.getNickname() + " is on square number " + newPosition);
            Square square = board.getSquareFromNumber(newPosition);

            player.getSquaresCrossed().addLast(square);
            square.applyEffect(this, player);
        }
    }



    public Dice getDice() {
        return this.dice;
    }

}
