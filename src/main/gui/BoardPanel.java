package main.gui;

import main.components.Player;
import main.components.Square;
import main.components.effects.*;
import main.memento.Board;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

public class BoardPanel extends JPanel {

    private Board board;
    private List<Player> players; // Lista di giocatori e le loro posizioni sul tabellone
    private Map<Player, Color> playerColorsMap;  // Mappa che associa i giocatori ai loro colori

    public BoardPanel(Board board, List<Player> players) {
        this.board = board;
        this.players = players;
        setPreferredSize(new Dimension(500, 500));

        // Inizializza la mappa di colori per i giocatori
        initializePlayerColors(players);
    }

    // Metodo per inizializzare i colori dei giocatori
    private void initializePlayerColors(List<Player> players) {
        Color[] playerColors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA};  // Più colori se necessario
        playerColorsMap = new HashMap<>();
        for (int i = 0; i < players.size(); i++) {
            playerColorsMap.put(players.get(i), playerColors[i % playerColors.length]);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (board != null) { // Controlla che il tabellone sia inizializzato
            drawBoard(g);
            if (players != null && !players.isEmpty()) {
                drawPlayers(g);
            }
        }
    }

    private void drawBoard(Graphics g) {
        int numRows = board.getnRows();
        int numCols = board.getnColumns();
        int cellWidth = getWidth() / numCols;
        int cellHeight = getHeight() / numRows;

        // Disegna le caselle del tabellone con i numeri
        for (int row = 0; row < numRows; row++) {
            int actualRow = numRows - 1 - row;  // Per invertire l'ordine delle righe

            for (int col = 0; col < numCols; col++) {
                int actualCol = (row % 2 == 0) ? col : (numCols - 1 - col);  // Per invertire l'ordine delle colonne in righe pari
                int x = actualCol * cellWidth;
                int y = actualRow * cellHeight;
                int squareNumber = row * numCols + col + 1;

                // Ottieni la casella corrispondente
                Square square = board.getSquareFromNumber(squareNumber);

                // Colore della casella in base al tipo di effetto
                g.setColor(getSquareColor(square));
                g.fillRect(x, y, cellWidth, cellHeight);

                // Disegna il bordo della casella
                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellWidth, cellHeight);

                // Disegna il numero della casella o il testo dell'effetto
                String squareText = getSquareText(square);
                g.setColor(Color.BLACK);
                g.drawString(squareText, x + cellWidth / 2 - 10, y + cellHeight / 2 + 5);
            }
        }

        // Disegna le destinazioni delle scale e dei serpenti
        for (int row = 0; row < numRows; row++) {
            int actualRow = numRows - 1 - row;  // Per invertire l'ordine delle righe

            for (int col = 0; col < numCols; col++) {
                int actualCol = (row % 2 == 0) ? col : (numCols - 1 - col);  // Per invertire l'ordine delle colonne in righe pari
                int x = actualCol * cellWidth;
                int y = actualRow * cellHeight;
                int squareNumber = row * numCols + col + 1;

                // Ottieni la casella corrispondente
                Square square = board.getSquareFromNumber(squareNumber);

                // Se l'effetto è un MoveEffect, evidenzia la destinazione
                if (square.getEffect() instanceof MoveEffect) {
                    Square destination = ((MoveEffect) square.getEffect()).getDestination();

                    // Scala: la destinazione è maggiore della casella corrente
                    if (destination.getNumber() > squareNumber) {
                        g.setColor(Color.ORANGE);  // Colore per la scala
                        g.fillRect(x, y, cellWidth, cellHeight);  // Riempi la casella di arancione

                        // Cambia il colore della scritta "Ladder"
                        g.setColor(Color.GREEN);  // Cambia il colore della scritta
                        g.drawString("Ladder", x + 10, y + 20);
                    }
                    // Serpente: la destinazione è minore della casella corrente
                    else if (destination.getNumber() < squareNumber) {
                        g.setColor(Color.RED);  // Colore per il serpente
                        g.fillRect(x, y, cellWidth, cellHeight);  // Riempi la casella di rosso

                        g.setColor(Color.BLACK);  // Cambia il colore della scritta
                        g.drawString("Snake", x + 10, y + 20);
                    }

                    // Evidenzia la destinazione (coda del serpente o cima della scala)
                    int destRow = (destination.getNumber() - 1) / numCols;
                    int destCol = (destination.getNumber() - 1) % numCols;
                    int actualDestRow = numRows - 1 - destRow;
                    int actualDestCol = (destRow % 2 == 0) ? destCol : (numCols - 1 - destCol);

                    int destX = actualDestCol * cellWidth;
                    int destY = actualDestRow * cellHeight;

                    // Scala: evidenzia la destinazione con un rettangolo verde solo contorno
                    if (destination.getNumber() > squareNumber) {
                        g.setColor(Color.GREEN);  // Verde per la cima della scala
                        g.drawRect(destX + cellWidth / 4, destY + cellHeight / 4, cellWidth / 2, cellHeight / 2);  // Rettangolo più piccolo, solo contorno
                    }
                    // Serpente: evidenzia la destinazione con un cerchio rosso
                    else if (destination.getNumber() < squareNumber) {
                        g.setColor(Color.RED);  // Rosso per la coda del serpente
                        g.drawOval(destX + cellWidth / 4, destY + cellHeight / 4, cellWidth / 2, cellHeight / 2);  // Disegna il cerchio
                    }
                }
            }
        }
    }


    private Color getSquareColor(Square square) {
        if (square.getEffect() instanceof MoveEffect) {
            return Color.ORANGE;
        } else if (square.getEffect() instanceof ChanceEffect) {
            return Color.YELLOW;
        } else if (square.getEffect() instanceof SpringEffect) {
            return Color.PINK;
        } else if (square.getEffect() instanceof GuestEffect) {
            return Color.GREEN;
        } else if (square.getEffect() instanceof BenchEffect) {
            return Color.CYAN;
        } else if (square.getEffect() instanceof DrawACardEffect) {
            return Color.MAGENTA;
        } else {
            return Color.LIGHT_GRAY;
        }
    }

    private String getSquareText(Square square) {
        if (square.getEffect() instanceof MoveEffect) {
            return "Move";
        } else if (square.getEffect() instanceof ChanceEffect) {
            return "Chance";
        } else if (square.getEffect() instanceof SpringEffect) {
            return "Spring";
        } else if (square.getEffect() instanceof GuestEffect) {
            return "Rest";
        } else if (square.getEffect() instanceof BenchEffect) {
            return "Bench";
        } else if (square.getEffect() instanceof DrawACardEffect) {
            return "Draw a Card";
        } else {
            return String.valueOf(square.getNumber());
        }
    }

    private void drawPlayers(Graphics g) {
        int numRows = board.getnRows();
        int numCols = board.getnColumns();
        int cellWidth = getWidth() / numCols;
        int cellHeight = getHeight() / numRows;

        // Disegna i giocatori nelle loro posizioni attuali
        for (Player player : players) {
            int playerPosition = player.getCurrentPosition();
            int row = (playerPosition - 1) / numCols;
            int col = (playerPosition - 1) % numCols;
            int actualRow = numRows - 1 - row;  // Inizia dalla riga inferiore
            int actualCol = (row % 2 == 0) ? col : (numCols - 1 - col);  // Inversione delle righe pari

            int x = actualCol * cellWidth;
            int y = actualRow * cellHeight;

            // Imposta il colore del giocatore dalla mappa
            g.setColor(playerColorsMap.get(player));

            // Disegna il giocatore come un cerchio
            g.fillOval(x + cellWidth / 4, y + cellHeight / 4, cellWidth / 2, cellHeight / 2);

            // Disegna il nome del giocatore sopra il cerchio
            g.setColor(Color.BLACK);  // Colore del testo
            g.drawString(player.getNickname(), x + cellWidth / 4, y + 10);  // Posiziona il nome leggermente sopra il cerchio
        }
    }

    public void updatePlayerPositions(List<Player> players) {
        this.players = players;
        repaint();
    }
}
