package Game;

import board.Board;
import components.Player;
import dice.Dice;
import board.Square;

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

    public void start() {
        while (!terminated) {
            Player player = getCurrentPlayer();
            int currentPosition = player.getCurrentPosition();
            int number = dice.roll(currentPosition);
            turn(number, player);
        }
    }

    private Player getCurrentPlayer() {
        Player player = players.removeFirst();
        players.addLast(player);
        if (player.hasTurnsToWait()) {
            player.setTurnsToWait((player.getTurnsToWait() - 1));
            getCurrentPlayer();
        }
        return player;
    }

    public void turn(int number, Player player) {
        int newPosition = player.advanceOf(number);
        if (newPosition == board.getNumSquares()) {
            this.terminated = true;
            System.out.println("Player " + player.getNickname() + " won!");
        } else {
            Square square = board.getSquareFromNumber(newPosition);
            square.applyEffect(this, player);
        }
    }


    public Dice getDice() {
        return this.dice;
    }

}
