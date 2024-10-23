package main;

import main.gui.ApplicationGUI;
import main.memento.Board;
import main.dice.*;
import main.memento.Caretaker;
import main.memento.DiceConfiguration;
import main.memento.Game;

import java.util.LinkedList;

public class Application {

    public static void main(String[] args) throws InterruptedException {

        LinkedList<String> playerNicknames = new LinkedList<>();
        playerNicknames.add("Hola mi amor");
        playerNicknames.add("Ciao");
        playerNicknames.add("Anas");

        Dice dice = new StandardDice(1);

        Board board = new Board.Builder().nColumns(12).nDrawCardSquares(10).nLadders(5).nLadders(3).otherCards(true).build();
        DiceConfiguration diceConfig = new DiceConfiguration(2, false, false, board);
        ApplicationGUI gui = new ApplicationGUI();
        Game game = new Game(3, playerNicknames, dice, board, false, gui);

        Caretaker caretaker = new Caretaker();

        //TO SET NEW GAME
        //caretaker.makeBackup(game, board, diceConfig);
        //System.out.println("Configuration saved \n");

        //TO RESTORE OLD CONFIG
        //caretaker.undo(game, board, diceConfig);
        //(System.out.println("Configuration saved \n");


        System.out.println("The game begins! \n");
        game.start();
    }
}



