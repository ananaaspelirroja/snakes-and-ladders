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

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        //BOARD
        // Chiedi all'utente il numero di righe
        System.out.print("Inserisci il numero di righe (min 3, max 12): ");
        int nRows = scanner.nextInt();
        while (nRows < 3 || nRows > 12) {
            System.out.print("Numero di righe non valido. Inserisci un numero tra 3 e 12: ");
            nRows = scanner.nextInt();
        }

        // Chiedi all'utente il numero di colonne
        System.out.print("Inserisci il numero di colonne (min 3, max 12): ");
        int nColumns = scanner.nextInt();
        while (nColumns < 3 || nColumns > 12) {
            System.out.print("Numero di colonne non valido. Inserisci un numero tra 3 e 12: ");
            nColumns = scanner.nextInt();
        }

        // Chiedi all'utente il numero di scale
        System.out.print("Inserisci il numero di scale: ");
        int nLadders = scanner.nextInt();

        // Chiedi all'utente il numero di serpenti
        System.out.print("Inserisci il numero di serpenti: ");
        int nSnakes = scanner.nextInt();

        // Chiedi all'utente il numero di caselle bonus
        System.out.print("Inserisci il numero di caselle bonus (ChanceEffect e SpringEffect): ");
        int nBonusSquares = scanner.nextInt();

        // Chiedi all'utente il numero di caselle di riposo
        System.out.print("Inserisci il numero di caselle di riposo (GuestEffect e BenchEffect): ");
        int nRestSquares = scanner.nextInt();

        // Chiedi all'utente il numero di caselle DrawACardEffect
        System.out.print("Inserisci il numero di caselle DrawACardEffect: ");
        int nDrawCardSquares = scanner.nextInt();
        boolean otherCards = false;
        if(nDrawCardSquares > 0){
            // Chiedi all'utente se vuole abilitare altre carte (true/false)
            System.out.print("Vuoi abilitare altre carte (true/false)? ");
            otherCards = scanner.nextBoolean();
        }



        // Costruisci la Board utilizzando i parametri inseriti dall'utente
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

        // Stampa il numero totale di caselle
        System.out.println("La board è stata costruita con " + board.getNumSquares() + " caselle.");

        // Stampa una casella specifica per mostrare un esempio
        System.out.print("Inserisci un numero di casella per vedere i dettagli: ");
        int squareNumber = scanner.nextInt();
        Square square = board.getSquareFromNumber(squareNumber);
        System.out.println("Dettagli della casella: " + square);

        //DICE
        System.out.print("Vuoi giocare con 1 o 2 dadi? ");
        int numDice = scanner.nextInt();

        Dice dice;

        if (numDice == 1) {
            System.out.println("Hai scelto di giocare con 1 dado.");
        } else if (numDice == 2) {
            dice = new StandardDice(2);
            System.out.println("Hai scelto di giocare con 2 dadi.");

            // Chiedi se vogliono abilitare la modalità "Double Six" o "One Dice At The End"
            System.out.print("Vuoi abilitare la modalità 'Double Six' (doppio sei ti permette di tirare di nuovo)? (s/n) ");
            String doubleSixChoice = scanner.next();

            if (doubleSixChoice.equalsIgnoreCase("s")) {
                dice = new DoubleSixDecorator(dice);
                System.out.println("Hai abilitato la modalità 'Double Six'.");
            }

            System.out.print("Vuoi abilitare la modalità 'One Dice At The End' (1 dado negli ultimi 6 spazi)? (s/n) ");
            String oneDiceAtEndChoice = scanner.next();

            if (oneDiceAtEndChoice.equalsIgnoreCase("s")) {
                dice = new SingleDieDecorator(dice, board);
                System.out.println("Hai abilitato la modalità 'One Dice At The End'.");
            }

            //GAME
            System.out.print("Inserisci il numero di giocatori: ");
            int nPlayers = scanner.nextInt();
            scanner.nextLine(); // Consuma il newline

            // Inserisci i nickname dei giocatori
            LinkedList<String> nicknames = new LinkedList<>();
            for (int i = 0; i < nPlayers; i++) {
                System.out.print("Inserisci il nickname del giocatore " + (i + 1) + ": ");
                String nickname = scanner.nextLine();
                nicknames.add(nickname);
            }

            Game game = new Game(nPlayers, nicknames, dice, board);

            // Avvia il gioco
            System.out.println("Il gioco inizia!");
            game.start();


            scanner.close();
        }


    }
}
