package game;

import board.Board;
import board.Square;
import components.Player;
import dice.Dice;

import java.util.LinkedList;

public class Game {

    private final int nPlayers;
    private LinkedList<Player> players = new LinkedList<>();
    private Dice dice;
    private Board board;
    private boolean terminated = false;

    public Game(int nPlayers, LinkedList<String> nicknames, Dice dice, Board board) {
        this.board = board;
        this.nPlayers = nPlayers;
        for (int i = 0; i < nicknames.size(); i++) {
            Player player = new Player((i + 1), nicknames.get(i), this.board);
            players.addLast(player);
        }
        this.dice = dice;
    }

    public void start() throws InterruptedException {
        while (!terminated) {
            Player player = getCurrentPlayer();  // Ottieni il giocatore corrente
            int currentPosition = player.getCurrentPosition();
            if (!player.hasTurnsToWait()) {  // Gioca solo se il giocatore non deve aspettare
                int number = dice.roll(currentPosition);  // Tira i dadi
                turn(number, player);  // Esegui il turno
            }
            System.out.println(player.getSquaresCrossed());

            // Aggiungi un ritardo per visualizzare meglio i messaggi
            Thread.sleep(2000);  // Pausa di 2 secondi tra i turni

            // Sposta il giocatore in fondo alla lista (fine del turno)
            players.addLast(players.removeFirst());
        }
    }

    private Player getCurrentPlayer() throws InterruptedException {
        Player player = players.getFirst();  // Ottieni il primo giocatore
        while (player.hasTurnsToWait()) {  // Se deve aspettare, diminuisci i turni di attesa
            player.setTurnsToWait(player.getTurnsToWait() - 1);
            System.out.println("Player " + player.getNickname() + " deve aspettare ancora " + player.getTurnsToWait() + " turni.");

            // Aggiungi un ritardo per visualizzare il messaggio
            Thread.sleep(2000);  // Pausa di 2 secondi per dare tempo di leggere il messaggio

            // Sposta il giocatore in fondo alla lista per aspettare il turno successivo
            players.addLast(players.removeFirst());
            player = players.getFirst();  // Ottieni il prossimo giocatore
        }
        return player;
    }

    public void turn(int number, Player player) {
        int newPosition = player.advanceOf(number);
        int totalSquares = board.getNumSquares();

        // Controlla se la nuova posizione supera il numero di caselle
        if (newPosition > totalSquares) {
            // Calcola la differenza e fai tornare indietro il giocatore
            int excess = newPosition - totalSquares;
            newPosition = totalSquares - excess;
            System.out.println("Il giocatore " + player.getNickname() + " ha superato la fine! Torna indietro di " + excess + " caselle.");
        }

        Square square = board.getSquareFromNumber(newPosition);
        player.getSquaresCrossed().addLast(square);
        if (newPosition == totalSquares) {
            this.terminated = true;
            System.out.println("Player " + player.getNickname() + " ha vinto!");
        } else {
            System.out.println("Player " + player.getNickname() + " è sulla casella numero " + newPosition);
            square.applyEffect(this, player);
        }
    }

    public Dice getDice() {
        return this.dice;
    }
}

