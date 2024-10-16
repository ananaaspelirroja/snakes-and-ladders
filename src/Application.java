import board.Board;
import board.Square;
import dice.Dice;
import dice.SingleDieDecorator;
import dice.StandardDice;
import dice.DoubleSixDecorator;
import game.Game;

import java.util.LinkedList;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) throws InterruptedException {

        Scanner scanner = new Scanner(System.in);

        // BOARD
        // Ask the user for the number of rows
        System.out.print("Enter the number of rows (min 3, max 12): ");
        int nRows = scanner.nextInt();
        while (nRows < 3 || nRows > 12) {
            System.out.print("Invalid number of rows. Enter a number between 3 and 12: ");
            nRows = scanner.nextInt();
        }

        // Ask the user for the number of columns
        System.out.print("Enter the number of columns (min 3, max 12): ");
        int nColumns = scanner.nextInt();
        while (nColumns < 3 || nColumns > 12) {
            System.out.print("Invalid number of columns. Enter a number between 3 and 12: ");
            nColumns = scanner.nextInt();
        }

        // Ask the user for the number of ladders
        System.out.print("Enter the number of ladders: ");
        int nLadders = scanner.nextInt();

        // Ask the user for the number of snakes
        System.out.print("Enter the number of snakes: ");
        int nSnakes = scanner.nextInt();

        // Ask the user for the number of bonus squares
        System.out.print("Enter the number of bonus squares (ChanceEffect and SpringEffect): ");
        int nBonusSquares = scanner.nextInt();

        // Ask the user for the number of rest squares
        System.out.print("Enter the number of rest squares (GuestEffect and BenchEffect): ");
        int nRestSquares = scanner.nextInt();

        // Ask the user for the number of DrawACardEffect squares
        System.out.print("Enter the number of DrawACardEffect squares: ");
        int nDrawCardSquares = scanner.nextInt();
        boolean otherCards = false;
        if (nDrawCardSquares > 0) {
            // Ask the user if they want to enable other cards (true/false)
            System.out.print("Do you want to enable other cards (true/false)? ");
            otherCards = scanner.nextBoolean();
        }

        // Build the Board using the parameters provided by the user
        Board board = new Board.Builder()
                .nRows(nRows)
                .nColumns(nColumns)
                .nLadders(nLadders)
                .nSnakes(nSnakes)
                .nBonusSquares(nBonusSquares)
                .nRestSquares(nRestSquares)
                .nDrawCardSquares(nDrawCardSquares)
                .otherCards(otherCards)
                .build();

        // Print the total number of squares
        System.out.println("The board has been built with " + board.getNumSquares() + " squares.");

        // Print a specific square to show an example
        System.out.print("Enter a square number to see the details: ");
        int squareNumber = scanner.nextInt();
        Square square = board.getSquareFromNumber(squareNumber);
        System.out.println("Square details: " + square);

        // DICE
        System.out.print("Do you want to play with 1 or 2 dice? ");
        int numDice = scanner.nextInt();

        Dice dice = null;

        if (numDice == 1) {
            System.out.println("You chose to play with 1 die.");
            dice = new StandardDice(1);
        } else if (numDice == 2) {
            dice = new StandardDice(2);
            System.out.println("You chose to play with 2 dice.");

            // Ask if they want to enable "Double Six" mode or "One Dice At The End"
            System.out.print("Do you want to enable 'Double Six' mode (rolling a double six lets you roll again)? (y/n) ");
            String doubleSixChoice = scanner.next();

            if (doubleSixChoice.equalsIgnoreCase("y")) {
                dice = new DoubleSixDecorator(dice);
                System.out.println("You have enabled 'Double Six' mode.");
            }

            System.out.print("Do you want to enable 'One Dice At The End' mode (1 die for the last 6 spaces)? (y/n) ");
            String oneDiceAtEndChoice = scanner.next();

            if (oneDiceAtEndChoice.equalsIgnoreCase("y")) {
                dice = new SingleDieDecorator(dice, board);
                System.out.println("You have enabled 'One Dice At The End' mode.");
            }
        }

        // GAME
        System.out.print("Enter the number of players: ");
        int nPlayers = scanner.nextInt();
        scanner.nextLine(); // Consume the newline

        // Enter the nicknames of the players
        LinkedList<String> nicknames = new LinkedList<>();
        for (int i = 0; i < nPlayers; i++) {
            System.out.print("Enter the nickname of player " + (i + 1) + ": ");
            String nickname = scanner.nextLine();
            nicknames.add(nickname);
        }

        Game game = new Game(nPlayers, nicknames, dice, board);

        // Start the game
        System.out.println("The game begins!");
        game.start();

        scanner.close();
    }
}
