package components;

import board.Board;
import board.Square;

import java.util.LinkedList;

public class Player {

    private int id;
    private String nickname;
    private int turnsToWait = 0;
    private boolean hasANoStoppingCard = false;
    private LinkedList<Square> squaresCrossed = new LinkedList<>();
    private LinkedList<Integer> diceRolls = new LinkedList<>();
    private Board board;

    public Player(int id, String nickname, Board board) {
        this.id = id;
        this.nickname = nickname;
        this.board = board;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getTurnsToWait() {
        return turnsToWait;
    }

    public void setTurnsToWait(int turnsToWait) {
        this.turnsToWait = turnsToWait;
    }

    public boolean hasTurnsToWait(){return turnsToWait > 0;}

    public boolean getHasANoStoppingCard() {
        return hasANoStoppingCard;
    }

    public void setHasANoStoppingCard(boolean hasANoStoppingCard) {
        this.hasANoStoppingCard = hasANoStoppingCard;
    }

    public LinkedList<Square> getSquaresCrossed() {
        return squaresCrossed;
    }

    public void setSquaresCrossed(LinkedList<Square> squaresCrossed) {
        this.squaresCrossed = squaresCrossed;
    }

    public LinkedList<Integer> getDiceRolls() {
        return diceRolls;
    }

    public int lastDiceRoll(){
        return this.diceRolls.getLast();
    }

    public void setDiceRolls(LinkedList<Integer> diceRolls) {
        this.diceRolls = diceRolls;
    }

    public int getCurrentPosition(){
        int pos = 0;
        if(squaresCrossed.isEmpty()){
            return pos;
        } else {
            pos = squaresCrossed.getLast().getNumber();
        }
        return pos;
    }

    public int advanceOf(int number) {
        int currentPosition = this.getCurrentPosition();
        int newPosition = currentPosition + number;
        Square currentSquare = board.getSquareFromNumber(newPosition);
        return newPosition;
    }
    
    public void setCurrentPosition(Square destination){
        squaresCrossed.addLast(destination);
        System.out.println(squaresCrossed);
    } //for snakes and ladders


}
