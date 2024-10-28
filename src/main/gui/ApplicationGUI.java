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
    private JPanel mainPanel;


    public ApplicationGUI() {
        caretaker = new Caretaker();
        setTitle("Snakes and Ladders - Application");
        setSize(800, 600);
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

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        gameInfoArea = new JTextArea();
        gameInfoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(gameInfoArea);
        scrollPane.setPreferredSize(new Dimension(200, 600));

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
            DiceConfiguration[] restoredDiceConfig = new DiceConfiguration[1];

            Game tempGame = new Game(0, new LinkedList<>(), null, null, false, this);


            caretaker.undo(tempGame, restoredBoard, restoredDiceConfig);

            // Controlla che board e diceConfig siano correttamente ripristinati
            if (restoredBoard[0] != null && restoredDiceConfig[0] != null && tempGame.getPlayers() != null) {
                JOptionPane.showMessageDialog(this, "Configuration loaded successfully!");


                boolean manualAdvance = !tempGame.isAutoAdvance();  // Inverti per ottenere il valore manuale
                LinkedList<String> playerNames = tempGame.getPlayers().stream()
                        .map(Player::getNickname)
                        .collect(Collectors.toCollection(LinkedList::new));


                startGame(restoredBoard[0], restoredDiceConfig[0], manualAdvance, playerNames);

                System.out.println("Restored Board: " + restoredBoard[0]);
                System.out.println("Restored Dice Configuration: " + restoredDiceConfig[0]);
                System.out.println("Manual Advance Restored: " + manualAdvance);

                revalidate();
                repaint();
            } else {
                throw new Exception("Failed to restore the game configuration: Board or DiceConfiguration is null.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load configuration: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    private void createNewConfiguration() {
        JFrame configFrame = new JFrame("Create New Configuration");
        configFrame.setSize(600, 600);
        configFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        configFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(17, 2));

        JLabel rowsLabel = new JLabel("Number of Rows:");
        JTextField rowsField = new JTextField();
        JLabel columnsLabel = new JLabel("Number of Columns:");
        JTextField columnsField = new JTextField();
        JLabel laddersLabel = new JLabel("Number of Ladders:");
        JTextField laddersField = new JTextField();
        JLabel snakesLabel = new JLabel("Number of Snakes:");
        JTextField snakesField = new JTextField();
        JLabel bonusSquaresLabel = new JLabel("Number of Bonus Squares (optional):");
        JTextField bonusSquaresField = new JTextField();
        JLabel restSquaresLabel = new JLabel("Number of Rest Squares (optional):");
        JTextField restSquaresField = new JTextField();
        JLabel drawCardSquaresLabel = new JLabel("Number of Draw Card Squares (optional):");
        JTextField drawCardSquaresField = new JTextField();

        JLabel diceLabel = new JLabel("Number of Dice:");
        JTextField diceField = new JTextField();


        JCheckBox doubleSixCheckbox = new JCheckBox("Enable Double Six Rule");
        JCheckBox oneDiceAtEndCheckbox = new JCheckBox("Enable One Dice At The End Rule");
        JCheckBox otherCardsCheckbox = new JCheckBox("Enable Other Cards");


        doubleSixCheckbox.setEnabled(false);
        oneDiceAtEndCheckbox.setEnabled(false);
        otherCardsCheckbox.setEnabled(false);

        // Listener per abilitare/disabilitare le opzioni avanzate in base ai valori
        diceField.addActionListener(e -> {
            try {
                int dice = Integer.parseInt(diceField.getText());
                boolean enableSpecialDiceRules = dice > 1;

                // Abilita o disabilita le checkbox delle regole dei dadi in base al numero di dadi
                doubleSixCheckbox.setEnabled(enableSpecialDiceRules);
                oneDiceAtEndCheckbox.setEnabled(enableSpecialDiceRules);
            } catch (NumberFormatException ex) {
                // Disabilita le opzioni se il valore non è valido
                doubleSixCheckbox.setEnabled(false);
                oneDiceAtEndCheckbox.setEnabled(false);
            }
        });

        // Listener per abilitare/disabilitare l'opzione "Enable Other Cards" in base alle Draw a Card Squares
        drawCardSquaresField.addActionListener(e -> {
            try {
                int drawCardSquares = Integer.parseInt(drawCardSquaresField.getText());
                otherCardsCheckbox.setEnabled(drawCardSquares > 0);
            } catch (NumberFormatException ex) {
                otherCardsCheckbox.setEnabled(false);
            }
        });

        JLabel playersLabel = new JLabel("Number of Players:");
        JTextField playersField = new JTextField();

        JPanel nicknamesPanel = new JPanel(new GridLayout(0, 2));

        playersField.addActionListener(e -> {
            try {
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

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(configFrame, "Enter a valid number of players.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JCheckBox manualAdvanceCheckbox = new JCheckBox("Manual Advance");

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            try {
                int rows = Integer.parseInt(rowsField.getText());
                int columns = Integer.parseInt(columnsField.getText());
                int ladders = Integer.parseInt(laddersField.getText());
                int snakes = Integer.parseInt(snakesField.getText());

                // Parametri opzionali con valori di default
                int bonusSquares = bonusSquaresField.getText().isEmpty() ? 0 : Integer.parseInt(bonusSquaresField.getText());
                int restSquares = restSquaresField.getText().isEmpty() ? 0 : Integer.parseInt(restSquaresField.getText());
                int drawCardSquares = drawCardSquaresField.getText().isEmpty() ? 0 : Integer.parseInt(drawCardSquaresField.getText());
                boolean otherCards = otherCardsCheckbox.isSelected();
                int dice = diceField.getText().isEmpty() ? 1 : Integer.parseInt(diceField.getText());
                boolean doubleSix = doubleSixCheckbox.isEnabled() && doubleSixCheckbox.isSelected();
                boolean oneDiceAtEnd = oneDiceAtEndCheckbox.isEnabled() && oneDiceAtEndCheckbox.isSelected();
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

                if (playerNames.size() != numPlayers) {
                    JOptionPane.showMessageDialog(configFrame, "Please fill out all player names.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (numPlayers < 2 || numPlayers > 7) {
                    JOptionPane.showMessageDialog(configFrame, "The number of players must be between 2 and 7.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (rows * columns < 9 || rows * columns > 144) {
                    JOptionPane.showMessageDialog(configFrame, "The board must have between 9 and 144 squares.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int maxLaddersAndSnakes = (rows * columns) / 2;
                if (ladders + snakes > maxLaddersAndSnakes) {
                    JOptionPane.showMessageDialog(configFrame, "The total number of ladders and snakes exceeds the limit.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int totalSpecialSquares = bonusSquares + restSquares + drawCardSquares + 1;
                int availableSpaces = rows * columns - (ladders * 2 + snakes * 2);
                if (totalSpecialSquares > availableSpaces) {
                    JOptionPane.showMessageDialog(configFrame, "The number of special squares exceeds the available space.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (otherCards && drawCardSquares == 0) {
                    JOptionPane.showMessageDialog(configFrame, "Cannot enable other cards without Draw Card squares.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

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
                JOptionPane.showMessageDialog(configFrame, "Please fill out all fields correctly.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(rowsLabel); panel.add(rowsField);
        panel.add(columnsLabel); panel.add(columnsField);
        panel.add(laddersLabel); panel.add(laddersField);
        panel.add(snakesLabel); panel.add(snakesField);
        panel.add(bonusSquaresLabel); panel.add(bonusSquaresField);
        panel.add(restSquaresLabel); panel.add(restSquaresField);
        panel.add(drawCardSquaresLabel); panel.add(drawCardSquaresField);
        panel.add(diceLabel); panel.add(diceField);
        panel.add(new JLabel("Special Dice Rules")); panel.add(doubleSixCheckbox);
        panel.add(new JLabel("")); panel.add(oneDiceAtEndCheckbox);
        panel.add(new JLabel("Card Options")); panel.add(otherCardsCheckbox);
        panel.add(playersLabel); panel.add(playersField);
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


}
