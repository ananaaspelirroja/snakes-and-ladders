package main.strategy;

import main.components.Player;
import main.gui.ApplicationGUI;
import main.memento.Game;

import java.io.Serializable;


public class AutoAdvanceStrategy implements AdvanceStrategy, Serializable {



    @Override
    public void advance(Game game, ApplicationGUI gui) throws InterruptedException {
        Player player = game.getCurrentPlayer();
        if (!player.hasTurnsToWait()) {
            int currentPosition = player.getCurrentPosition();
            int number = game.getDice().roll(currentPosition);
            gui.updateGameInfo("Player " + player.getNickname() + " rolled a " + number + "\n");
            player.updateLastDiceRoll(number);
            game.turn(number, player);
        }

        System.out.println(player.getSquaresCrossed());

        Thread.sleep(2000);  // just to visualize
        // Put the player at the end of the list to maintain the order of the turns
        game.movePlayerToEnd();
    }

}
