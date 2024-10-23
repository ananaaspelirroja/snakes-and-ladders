package main.gui;

import main.components.Player;
import main.memento.Board;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BoardPanel extends JPanel {

    private Board board;
    private List<Player> players; // Lista di giocatori e le loro posizioni sul tabellone

    public BoardPanel(Board board, List<Player> players) {
        this.board = board;
        this.players = players;
        setPreferredSize(new Dimension(500, 500));
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

        // Disegna le caselle del tabellone
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int x = col * cellWidth;
                int y = row * cellHeight;
                g.drawRect(x, y, cellWidth, cellHeight);
            }
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

            int x = col * cellWidth;
            int y = row * cellHeight;

            // Disegna il giocatore come un cerchio
            g.setColor(Color.RED); // Cambia colore per ogni giocatore se necessario
            g.fillOval(x + cellWidth / 4, y + cellHeight / 4, cellWidth / 2, cellHeight / 2);
        }
    }

    // Metodo per aggiornare la posizione dei giocatori e ridisegnare il tabellone
    public void updatePlayerPositions(List<Player> players) {
        this.players = players;
        repaint();
    }
}
