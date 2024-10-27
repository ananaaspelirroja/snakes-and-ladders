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
    private JPanel mainPanel;  // Nuovo pannello principale che contiene il tabellone e le informazioni


    public ApplicationGUI() {
        caretaker = new Caretaker();
        setTitle("Snakes and Ladders - Application");
        setSize(800, 600);  // Maggiore spazio per includere il tabellone e le informazioni
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
                loadConfiguration();
            }
        });

        // Button to create a new configuration
        newConfigButton = new JButton("Create New Configuration");
        newConfigButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewConfiguration();
            }
        });

        buttonPanel.add(loadConfigButton);
        buttonPanel.add(newConfigButton);

        add(buttonPanel, BorderLayout.NORTH);

        // Pannello principale che contiene il tabellone e la finestra delle informazioni
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Area di testo per le informazioni di gioco
        gameInfoArea = new JTextArea();
        gameInfoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(gameInfoArea);
        scrollPane.setPreferredSize(new Dimension(200, 600));  // Dimensione preferita per il pannello delle informazioni

        // Aggiungi la finestra delle informazioni sul lato destro del pannello principale
        mainPanel.add(scrollPane, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);  // Aggiungi il pannello principale al centro della finestra
    }

    // Method to update the game info area with new information
    public void updateGameInfo(String message) {
        gameInfoArea.append(message + "\n");
        gameInfoArea.setCaretPosition(gameInfoArea.getDocument().getLength()); // Scroll to the bottom
    }

    private void loadConfiguration() {
        try {
            Board[] restoredBoard = new Board[1];  // Usa un array per passare per riferimento
            DiceConfiguration[] restoredDiceConfig = new DiceConfiguration[1];  // Usa un array per passare per riferimento

            Game tempGame = new Game(0, new LinkedList<>(), null, null, false, this);

            // Prova a ripristinare la configurazione
            caretaker.undo(tempGame, restoredBoard, restoredDiceConfig);  // Passa gli array per riferimento

            // Controlla che board e diceConfig siano correttamente ripristinati
            if (restoredBoard[0] != null && restoredDiceConfig[0] != null && tempGame.getPlayers() != null) {
                JOptionPane.showMessageDialog(this, "Configuration loaded successfully!");

                boolean manualAdvance = false;
                LinkedList<String> playerNames = tempGame.getPlayers().stream()
                        .map(Player::getNickname)
                        .collect(Collectors.toCollection(LinkedList::new));

                // Avvia il gioco con i valori restaurati
                startGame(restoredBoard[0], restoredDiceConfig[0], manualAdvance, playerNames);

                System.out.println("Restored Board: " + restoredBoard[0]);
                System.out.println("Restored Dice Configuration: " + restoredDiceConfig[0]);

                revalidate();
                repaint();
            } else {
                throw new Exception("Failed to restore the game configuration: Board or DiceConfiguration is null.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();  // Stampa l'errore sulla console
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

        JLabel diceLabel = new JLabel("Number of Dice:");
        JTextField diceField = new JTextField();

        // Creiamo i checkbox ma non li aggiungiamo inizialmente al pannello
        JCheckBox doubleSixCheckbox = new JCheckBox("Enable Double Six Rule");
        JCheckBox oneDiceAtEndCheckbox = new JCheckBox("Enable One Dice At The End Rule");
        JCheckBox otherCardsCheckbox = new JCheckBox("Enable Other Cards");

        // Listener per abilitare/disabilitare le opzioni dei dadi
        diceField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int dice = Integer.parseInt(diceField.getText());
                    boolean enableSpecialDiceRules = dice > 1;

                    // Aggiunge o rimuove le opzioni in base al valore del dado
                    if (enableSpecialDiceRules) {
                        if (!panel.isAncestorOf(doubleSixCheckbox)) {
                            panel.add(doubleSixCheckbox);
                            panel.add(oneDiceAtEndCheckbox);
                        }
                    } else {
                        panel.remove(doubleSixCheckbox);
                        panel.remove(oneDiceAtEndCheckbox);
                    }

                    configFrame.revalidate();
                    configFrame.repaint();

                } catch (NumberFormatException ex) {
                    // In caso di valore non valido rimuove le opzioni
                    panel.remove(doubleSixCheckbox);
                    panel.remove(oneDiceAtEndCheckbox);
                    configFrame.revalidate();
                    configFrame.repaint();
                }
            }
        });

        // Listener per abilitare/disabilitare l'opzione "Enable Other Cards"
        drawCardSquaresField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int drawCardSquares = Integer.parseInt(drawCardSquaresField.getText());
                    boolean enableOtherCards = drawCardSquares > 0;

                    // Aggiunge o rimuove l'opzione delle altre carte
                    if (enableOtherCards) {
                        if (!panel.isAncestorOf(otherCardsCheckbox)) {
                            panel.add(otherCardsCheckbox);
                        }
                    } else {
                        panel.remove(otherCardsCheckbox);
                    }

                    configFrame.revalidate();
                    configFrame.repaint();

                } catch (NumberFormatException ex) {
                    // In caso di valore non valido rimuove l'opzione
                    panel.remove(otherCardsCheckbox);
                    configFrame.revalidate();
                    configFrame.repaint();
                }
            }
        });


        JLabel playersLabel = new JLabel("Number of Players:");
        JTextField playersField = new JTextField();

        // Pannello per i nickname dei giocatori
        JPanel nicknamesPanel = new JPanel(new GridLayout(0, 2)); // Pannello per i nickname

        playersField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numPlayers = Integer.parseInt(playersField.getText());
                nicknamesPanel.removeAll();

                for (int i = 1; i <= numPlayers; i++) {
                    nicknamesPanel.add(new JLabel("Player " + i + " Name:"));
                    JTextField playerNameField = new JTextField();
                    nicknamesPanel.add(playerNameField);
                }

                nicknamesPanel.revalidate();
                nicknamesPanel.repaint();
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

                    if (numPlayers < 2) {
                        JOptionPane.showMessageDialog(configFrame, "The players should be at least 2.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (numPlayers > 7) {
                        JOptionPane.showMessageDialog(configFrame, "The players should be at most 6.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Controllo sugli input
                    if (rows * columns < 9 || rows * columns > 144) {
                        JOptionPane.showMessageDialog(configFrame, "The board must have between 9 and 144 squares.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int maxLaddersAndSnakes = (rows * columns) / 2;
                    if (ladders + snakes > maxLaddersAndSnakes) {
                        JOptionPane.showMessageDialog(configFrame, "The total number of ladders and snakes exceeds the available limit.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int totalSpecialSquares = bonusSquares + restSquares + drawCardSquares + 1;  // The final square is always a special square
                    int availableSpaces = rows * columns - (ladders * 2 + snakes * 2);
                    if (totalSpecialSquares > availableSpaces) {
                        JOptionPane.showMessageDialog(configFrame, "The number of special squares exceeds the available space.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (otherCards && drawCardSquares == 0) {
                        JOptionPane.showMessageDialog(configFrame, "Cannot enable other cards if there are no DrawACard squares.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Crea la board e il dice configuration
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

                    startGame(board, diceConfig, manualAdvance, playerNames);
                    configFrame.dispose();
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
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
        panel.add(diceLabel);
        panel.add(diceField);
        panel.add(playersLabel);
        panel.add(playersField);
        panel.add(manualAdvanceCheckbox);
        panel.add(submitButton);

        configFrame.add(panel, BorderLayout.NORTH);
        configFrame.add(new JScrollPane(nicknamesPanel), BorderLayout.CENTER);
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

        game = new Game(playerNames.size(), playerNames, diceConfig.createDice(), board, !manualAdvance, this);
        caretaker.makeBackup(game, board, diceConfig);

        // Crea il pannello del tabellone e aggiungilo al pannello principale
        boardPanel = new BoardPanel(board, game.getPlayers());
        mainPanel.add(boardPanel, BorderLayout.CENTER);  // Aggiungi il pannello del tabellone

        revalidate();
        repaint();

        if (manualAdvance) {
            JButton advanceButton = new JButton("Avanza Turno");
            advanceButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new Thread(() -> {
                        try {
                            game.getStrategy().advance(game, ApplicationGUI.this);
                            updateBoard();
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
            boardPanel.updatePlayerPositions(game.getPlayers());
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
