package main.strategy;

import main.components.Player;
import main.gui.ApplicationGUI;
import main.memento.Game;

import javax.swing.*;

public class ManualAdvanceStrategy implements AdvanceStrategy {

    @Override
    public void advance(Game game, ApplicationGUI gui) throws InterruptedException {
        Player player = game.getCurrentPlayer();
        if (!player.hasTurnsToWait()) {
            // Usa un JOptionPane per richiedere l'input
            String[] options = {"Roll the Dice", "Quit"};
            int choice = JOptionPane.showOptionDialog(gui,
                    "It's " + player.getNickname() + "'s turn. What would you like to do?",
                    "Player's Turn",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 1) { // Quit option
                JOptionPane.showMessageDialog(gui, "Exiting the game.");
                game.terminate();
                return;
            } else if (choice == 0) { // Roll the dice option
                int currentPosition = player.getCurrentPosition();
                int number = game.getDice().roll(currentPosition);
                gui.updateGameInfo("Player " + player.getNickname() + " rolled a " + number + "\n");
                player.updateLastDiceRoll(number);
                game.turn(number, player);
            } else {
                JOptionPane.showMessageDialog(gui, "Invalid choice. Try again.");
                return;
            }
        }

        // Stampa le caselle attraversate dal giocatore (solo per debug)
        System.out.println(player.getSquaresCrossed());

        // Muovi il giocatore in fondo alla lista (prossimo giocatore)
        game.movePlayerToEnd();
    }
}
