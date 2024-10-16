package main.game;

import main.components.Player;

import java.util.Scanner;

public class ManualAdvanceStrategy implements AdvanceStrategy {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void advance(Game game) throws InterruptedException {
        Player player = game.getCurrentPlayer();
        if (!player.hasTurnsToWait()) {
            System.out.println("È il turno di " + player.getNickname() + ". Premi 'r' per lanciare i dadi o 'q' per uscire.");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("q")) {
                System.out.println("Uscita dal gioco.");
                game.terminate();
                return;
            } else if (input.equalsIgnoreCase("r")) {
                int currentPosition = player.getCurrentPosition();
                int number = game.getDice().roll(currentPosition);
                game.turn(number, player);
            } else {
                System.out.println("Comando non valido. Prova di nuovo.");
                return;
            }
        }


        System.out.println(player.getSquaresCrossed());


        game.movePlayerToEnd();
        }

    }

