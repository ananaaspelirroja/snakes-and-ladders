package main.memento;

import main.dice.Dice;
import main.dice.DoubleSixDecorator;
import main.dice.SingleDieDecorator;
import main.dice.StandardDice;

import java.io.*;

public class Caretaker {

    private final String path = "C:\\Users\\Anastasia\\Desktop\\SnakesAndLadders\\SnakesAndLadders\\configurations\\last_config";

    public Caretaker() {
    }

    public void makeBackup(Game game, Board board, DiceConfiguration diceConfig) {
        Game.GameMemento gm = game.save();
        System.out.println(gm);
        Board.BoardMemento bm = board.save();
        DiceConfiguration.DiceMemento dm = diceConfig.save();

        try (FileOutputStream fileOut = new FileOutputStream(path);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(gm);
            out.writeObject(bm);
            out.writeObject(dm);

            System.out.println("Backup completed with success!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void undo(Game game, Board board, DiceConfiguration diceConfig) {
        try (FileInputStream fileIn = new FileInputStream(path);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            // Leggi i memento
            Game.GameMemento gameMemento = (Game.GameMemento) in.readObject();
            Board.BoardMemento boardMemento = (Board.BoardMemento) in.readObject();
            DiceConfiguration.DiceMemento diceMemento = (DiceConfiguration.DiceMemento) in.readObject();

            // Ripristina la board e la configurazione dei dadi
            Board restoredBoard = new Board.Builder()
                    .nColumns(boardMemento.getNColumns())
                    .nRows(boardMemento.getNRows())
                    .nLadders(boardMemento.getNLadders())
                    .nSnakes(boardMemento.getNSnakes())
                    .nBonusSquares(boardMemento.getNBonusSquares())
                    .nRestSquares(boardMemento.getNRestSquares())
                    .nDrawCardSquares(boardMemento.getNDrawCardSquares())
                    .otherCards(boardMemento.isOtherCards())
                    .build();

            DiceConfiguration restoredDiceConfig = new DiceConfiguration(
                    diceMemento.getNumDice(),
                    diceMemento.isDoubleSixEnabled(),
                    diceMemento.isOneDiceAtEndEnabled()
            );

            Dice restoredDice = null;
            if(diceMemento.getNumDice() == 1){
                restoredDice = new StandardDice(1);
            } else {
                restoredDice = new StandardDice(1);
                if(diceMemento.isDoubleSixEnabled()){
                    restoredDice = new DoubleSixDecorator(restoredDice);
                }
                if(diceMemento.isOneDiceAtEndEnabled()){
                    restoredDice = new SingleDieDecorator(restoredDice, restoredBoard);
                }
            }

            // Ripristina il gioco utilizzando il memento e gli oggetti ricostruiti
            game.restore(gameMemento, restoredBoard, restoredDice);

            System.out.println("Restore completed with success!");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
