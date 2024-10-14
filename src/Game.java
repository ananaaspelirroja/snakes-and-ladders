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



    public Game(int nPlayers, LinkedList<String> nicknames, Dice dice, Board board){
        this.board = board;
        this.nPlayers = nPlayers;
        for(int i = 0; i < nicknames.size(); i++){
            Player player = new Player((i+1), nicknames.get(i), this.board=board);
            players.addLast(player);
        }
        this.dice = dice;
    }

    public void start(){
        boolean terminated = false;
        while(!terminated){
            Player player = getCurrentPlayer();
            int currentPosition = player.getCurrentPosition();
            int number = dice.roll(currentPosition);
            int newPosition = player.advanceOf(number);
            if(newPosition == board.getNumSquares()){
                terminated = true;
                System.out.println("components.Player " + player.getNickname() + " won!");
            }
            else {
                Square square = board.getSquareFromNumber(newPosition);
                square.getEffect(player);
            }
        }
    }

    private Player getCurrentPlayer(){
        Player currentPlayer = players.removeFirst();
        players.addLast(currentPlayer);
        return currentPlayer;
    }

}
