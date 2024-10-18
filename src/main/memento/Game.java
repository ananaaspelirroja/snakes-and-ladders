package main.memento;

import main.components.Player;
import main.components.Square;
import main.dice.Dice;
import main.strategy.AdvanceStrategy;
import main.strategy.AutoAdvanceStrategy;
import main.strategy.ManualAdvanceStrategy;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;

public class Game implements Serializable {

    private int nPlayers;
    private LinkedList<Player> players = new LinkedList<>();
    private LinkedList<String> playersNames;
    private Dice dice;
    private Board board;
    private boolean terminated = false;
    private boolean autoAdvance;
    private transient AdvanceStrategy strategy;

    public Game(int nPlayers, LinkedList<String> nicknames, Dice dice, Board board, boolean autoAdvance) {
        this.board = board;
        this.nPlayers = nPlayers;
        this.playersNames = nicknames;
        for (int i = 0; i < nicknames.size(); i++) {
            Player player = new Player((i + 1), nicknames.get(i), this.board);
            players.addLast(player);
        }
        this.autoAdvance = autoAdvance;
        this.dice = dice;
        if(autoAdvance){
            this.strategy = new AutoAdvanceStrategy();
        } else {
            this.strategy = new ManualAdvanceStrategy();
        }

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
    public GameMemento save() {
        return new GameMemento();
    } //save

    // Restore the game state from the memento
    public void restore(GameMemento m, Board restoredBoard, Dice restoredDice) {
        this.nPlayers = m.nPlayers;
        this.playersNames = new LinkedList<>(m.playersNames);
        this.terminated = m.terminated;
        this.autoAdvance = m.autoAdvance;

        if (this.autoAdvance) {
            this.strategy = new AutoAdvanceStrategy();
        } else {
            this.strategy = new ManualAdvanceStrategy();
        }

        this.dice = restoredDice;
        this.board = restoredBoard;

        this.players.clear();
        for (int i = 0; i < playersNames.size(); i++) {
            Player player = new Player((i + 1), playersNames.get(i), this.board);
            players.addLast(player);
        }

        System.out.println("Game state restored!");
    }


    // Inner Memento class
    class GameMemento implements Memento, Serializable {
        @Serial
        private static final long serialVersionUID = 123456789L;
        private final int nPlayers;
        private final LinkedList<String> playersNames;
        private final boolean terminated;
        private final boolean autoAdvance;


        GameMemento() {
            this.nPlayers = Game.this.nPlayers;
            this.playersNames = new LinkedList<>(Game.this.playersNames);  // Save a copy of the players
            this.terminated = Game.this.terminated;
            this.autoAdvance = Game.this.autoAdvance;
        }

        // Get the originator (Game instance)
        Game getOriginator() {
            return Game.this;
        }

        @Override
        public String toString() {
            return "GameMemento{" +
                    "nPlayers=" + nPlayers +
                    ", playersNames=" + playersNames +
                    ", terminated=" + terminated +
                    ", auto advance strategy=" + autoAdvance +
                    '}';
        }
    }
}