package main.strategy;

import main.components.Player;
import main.memento.Game;

import java.io.Serializable;
import java.util.Scanner;

public class ManualAdvanceStrategy implements AdvanceStrategy{
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void advance(Game game) throws InterruptedException {
        Player player = game.getCurrentPlayer();
        if (!player.hasTurnsToWait()) {
            System.out.println("It's " + player.getNickname() + "'s turn. Press 'r' to roll the dice or 'q' to quit. \n");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("q")) {
                System.out.println("Exiting the game.");
                game.terminate();
                return;
            } else if (input.equalsIgnoreCase("r")) {
                int currentPosition = player.getCurrentPosition();
                int number = game.getDice().roll(currentPosition);
                player.updateLastDiceRoll(number);
                game.turn(number, player);
            } else {
                System.out.println("Invalid command. Try again.");
                return;
            }
        }

        System.out.println(player.getSquaresCrossed());

        game.movePlayerToEnd();
    }

}
