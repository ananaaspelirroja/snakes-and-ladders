package main.game;

import main.components.Player;


public class AutoAdvanceStrategy implements AdvanceStrategy {

    @Override
    public void advance(Game game) throws InterruptedException {
        Player player = game.getCurrentPlayer();
        if (!player.hasTurnsToWait()) {
            int currentPosition = player.getCurrentPosition();
            int number = game.getDice().roll(currentPosition);
            player.updateLastDiceRoll(number);
            game.turn(number, player);
        }

        System.out.println(player.getSquaresCrossed());

        Thread.sleep(2000);  // just to visualize
        // Put the player at the end of the list to maintain the order of the turns
        game.movePlayerToEnd();
    }

}
