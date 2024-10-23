package main.gui;

import main.memento.Board;
import main.memento.Caretaker;
import main.memento.DiceConfiguration;
import main.memento.Game;
import main.components.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class ApplicationGUI extends JFrame {

    private JButton loadConfigButton;
    private JButton newConfigButton;
    private Caretaker caretaker;
    private JTextArea gameInfoArea;  // Text area for displaying game information
    private BoardPanel boardPanel;
    private Game game;


    public ApplicationGUI() {
        caretaker = new Caretaker();
        setTitle("Snakes and Ladders - Application");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        // Button to load existing configuration
        loadConfigButton = new JButton("Load Existing Configuration");
        loadConfigButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the logic to load configuration using Caretaker
                loadConfiguration();
            }
        });

        // Button to create a new configuration
        newConfigButton = new JButton("Create New Configuration");
        newConfigButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Go to the new configuration setup panel
                createNewConfiguration();
            }
        });

        buttonPanel.add(loadConfigButton);
        buttonPanel.add(newConfigButton);

        // Add button panel to the frame
        add(buttonPanel, BorderLayout.NORTH);

        // Create and add the game information area (JTextArea)
        gameInfoArea = new JTextArea();
        gameInfoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(gameInfoArea);
        add(scrollPane, BorderLayout.CENTER);

        // Initialize boardPanel (empty initially)
        boardPanel = new BoardPanel(null, new LinkedList<>());
        add(boardPanel, BorderLayout.CENTER);
    }

    // Method to update the game info area with new information
    public void updateGameInfo(String message) {
        gameInfoArea.append(message + "\n");
        gameInfoArea.setCaretPosition(gameInfoArea.getDocument().getLength()); // Scroll to the bottom
    }

    private void loadConfiguration() {
        try {
            Board restoredBoard = null;
            DiceConfiguration restoredDiceConfig = null;

            Game tempGame = new Game(0, new LinkedList<>(), null, null, false, this);

            caretaker.undo(tempGame, restoredBoard, restoredDiceConfig);

            if (restoredBoard != null && restoredDiceConfig != null && tempGame.getPlayers() != null) {
                JOptionPane.showMessageDialog(this, "Configuration loaded successfully!");

                boolean manualAdvance = false;
                LinkedList<String> playerNames = tempGame.getPlayers().stream()
                        .map(Player::getNickname)
                        .collect(Collectors.toCollection(LinkedList::new));

                startGame(restoredBoard, restoredDiceConfig, manualAdvance, playerNames);

                // Forza l'aggiornamento del layout dopo il caricamento
                revalidate();
                repaint();
            } else {
                throw new Exception("Failed to restore the game configuration!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load configuration: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createNewConfiguration() {
        JFrame configFrame = new JFrame("Create New Configuration");
        configFrame.setSize(500, 500);
        configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        configFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(15, 2));

        JLabel rowsLabel = new JLabel("Number of Rows:");
        JTextField rowsField = new JTextField();
        JLabel columnsLabel = new JLabel("Number of Columns:");
        JTextField columnsField = new JTextField();
        JLabel laddersLabel = new JLabel("Number of Ladders:");
        JTextField laddersField = new JTextField();
        JLabel snakesLabel = new JLabel("Number of Snakes:");
        JTextField snakesField = new JTextField();
        JLabel bonusSquaresLabel = new JLabel("Number of Bonus Squares:");
        JTextField bonusSquaresField = new JTextField();
        JLabel restSquaresLabel = new JLabel("Number of Rest Squares:");
        JTextField restSquaresField = new JTextField();
        JLabel drawCardSquaresLabel = new JLabel("Number of Draw Card Squares:");
        JTextField drawCardSquaresField = new JTextField();
        JCheckBox otherCardsCheckbox = new JCheckBox("Enable Other Cards");

        JLabel diceLabel = new JLabel("Number of Dice:");
        JTextField diceField = new JTextField();

        JCheckBox doubleSixCheckbox = new JCheckBox("Enable Double Six Rule");
        JCheckBox oneDiceAtEndCheckbox = new JCheckBox("Enable One Dice At The End Rule");

        JLabel playersLabel = new JLabel("Number of Players:");
        JTextField playersField = new JTextField();

        // Pannello per i nickname dei giocatori
        JPanel nicknamesPanel = new JPanel(new GridLayout(0, 2)); // Pannello per i nickname

        // Quando l'utente inserisce il numero di giocatori, mostra i campi per i nickname
        playersField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numPlayers = Integer.parseInt(playersField.getText());
                nicknamesPanel.removeAll(); // Rimuovi eventuali campi precedenti

                // Aggiungi i nuovi campi per i nomi dei giocatori
                for (int i = 1; i <= numPlayers; i++) {
                    nicknamesPanel.add(new JLabel("Player " + i + " Name:"));
                    JTextField playerNameField = new JTextField();
                    nicknamesPanel.add(playerNameField);
                }

                // Ridisegna il pannello per aggiornare i campi di input
                nicknamesPanel.revalidate();
                nicknamesPanel.repaint();

                // Ridisegna anche la finestra principale
                configFrame.revalidate();
                configFrame.repaint();
            }
        });

        JCheckBox manualAdvanceCheckbox = new JCheckBox("Manual Advance");

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int rows = Integer.parseInt(rowsField.getText());
                    int columns = Integer.parseInt(columnsField.getText());
                    int ladders = Integer.parseInt(laddersField.getText());
                    int snakes = Integer.parseInt(snakesField.getText());
                    int bonusSquares = Integer.parseInt(bonusSquaresField.getText());
                    int restSquares = Integer.parseInt(restSquaresField.getText());
                    int drawCardSquares = Integer.parseInt(drawCardSquaresField.getText());
                    boolean otherCards = otherCardsCheckbox.isSelected();
                    int dice = Integer.parseInt(diceField.getText());
                    boolean doubleSix = doubleSixCheckbox.isSelected();
                    boolean oneDiceAtEnd = oneDiceAtEndCheckbox.isSelected();
                    boolean manualAdvance = manualAdvanceCheckbox.isSelected();

                    int numPlayers = Integer.parseInt(playersField.getText());
                    LinkedList<String> playerNames = new LinkedList<>();
                    for (Component component : nicknamesPanel.getComponents()) {
                        if (component instanceof JTextField) {
                            String playerName = ((JTextField) component).getText();
                            if (!playerName.trim().isEmpty()) {
                                playerNames.add(playerName);
                            }
                        }
                    }

                    // Verifica che tutti i nomi siano stati inseriti
                    if (playerNames.size() != numPlayers) {
                        JOptionPane.showMessageDialog(configFrame, "Please fill out all player names.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Crea il board e il dice configuration
                    Board board = new Board.Builder()
                            .nRows(rows)
                            .nColumns(columns)
                            .nLadders(ladders)
                            .nSnakes(snakes)
                            .nBonusSquares(bonusSquares)
                            .nRestSquares(restSquares)
                            .nDrawCardSquares(drawCardSquares)
                            .otherCards(otherCards)
                            .build();

                    DiceConfiguration diceConfig = new DiceConfiguration(dice, doubleSix, oneDiceAtEnd, board);

                    // Avvia il gioco con la configurazione
                    startGame(board, diceConfig, manualAdvance, playerNames);
                    configFrame.dispose();  // Chiudi la finestra di configurazione
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();  // Stampa l'errore sulla console
                    JOptionPane.showMessageDialog(configFrame, "Please fill out all fields correctly.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(rowsLabel);
        panel.add(rowsField);
        panel.add(columnsLabel);
        panel.add(columnsField);
        panel.add(laddersLabel);
        panel.add(laddersField);
        panel.add(snakesLabel);
        panel.add(snakesField);
        panel.add(bonusSquaresLabel);
        panel.add(bonusSquaresField);
        panel.add(restSquaresLabel);
        panel.add(restSquaresField);
        panel.add(drawCardSquaresLabel);
        panel.add(drawCardSquaresField);
        panel.add(otherCardsCheckbox);
        panel.add(diceLabel);
        panel.add(diceField);
        panel.add(doubleSixCheckbox);
        panel.add(oneDiceAtEndCheckbox);
        panel.add(playersLabel);
        panel.add(playersField);
        panel.add(manualAdvanceCheckbox);
        panel.add(submitButton);

        // Aggiungi il pannello dei nickname sotto il form principale
        configFrame.add(panel, BorderLayout.NORTH);
        configFrame.add(new JScrollPane(nicknamesPanel), BorderLayout.CENTER);  // Pannello per i nickname
        configFrame.setVisible(true);
    }


    private void startGame(Board board, DiceConfiguration diceConfig, boolean manualAdvance, LinkedList<String> playerNames) {
        System.out.println("Board: " + board);
        System.out.println("DiceConfiguration: " + diceConfig);
        System.out.println("PlayerNames: " + playerNames);
        System.out.println("Manual Advance: " + manualAdvance);

        if (board == null || diceConfig == null || playerNames == null || playerNames.size() < 2) {
            String errorMessage = "Error: The board, dice configuration, or player names are not initialized correctly.";
            updateGameInfo(errorMessage);
            System.err.println(errorMessage);
            return;
        }

        // Crea il gioco
        game = new Game(playerNames.size(), playerNames, diceConfig.createDice(), board, !manualAdvance, this);

        // Crea il pannello del tabellone
        boardPanel = new BoardPanel(board, game.getPlayers());

        // Rimuovi componenti precedenti e aggiungi il pannello del tabellone
        getContentPane().removeAll();
        add(boardPanel, BorderLayout.CENTER);
        revalidate();
        repaint();

        if (manualAdvance) {
            // Aggiungi pulsante per avanzamento manuale
            JButton advanceButton = new JButton("Avanza Turno");
            advanceButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Avanza solo un turno alla volta, non blocca il thread della GUI
                    new Thread(() -> {
                        try {
                            game.getStrategy().advance(game, ApplicationGUI.this);
                            updateBoard(); // Aggiorna il tabellone dopo il turno
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                }
            });

            getContentPane().add(advanceButton, BorderLayout.SOUTH);
            revalidate();
            repaint();
        } else {
            // Avvia il gioco automaticamente
            new Thread(() -> {
                try {
                    while (!game.isTerminated()) {
                        game.getStrategy().advance(game, ApplicationGUI.this);
                        updateBoard();
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }



    public void updateBoard() {
        if (boardPanel != null && game != null) {
            // Aggiorna le posizioni dei giocatori e ridisegna il tabellone
            boardPanel.updatePlayerPositions(game.getPlayers());

            // Forza l'aggiornamento del layout
            revalidate();
            repaint();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ApplicationGUI().setVisible(true);
            }
        });
    }
}
