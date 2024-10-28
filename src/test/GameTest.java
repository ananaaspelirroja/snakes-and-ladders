package test;

import static org.junit.jupiter.api.Assertions.*;

import main.components.Card;
import main.components.Deck;
import main.components.Square;
import main.components.effects.BenchEffect;
import main.components.effects.ChanceEffect;
import main.components.effects.MoveEffect;
import main.components.effects.DrawACardEffect;
import main.components.effects.NoStoppingEffect;
import main.dice.Dice;
import main.dice.DoubleSixDecorator;
import main.dice.SingleDieDecorator;
import main.dice.StandardDice;
import main.gui.ApplicationGUI;
import main.memento.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.memento.Board;
import main.components.Player;

import java.util.LinkedList;

public class GameTest {

    private Board board;
    private Player player;
    private Game game;
    private ApplicationGUI gui;

    @BeforeEach
    public void setUp() {
        gui = new ApplicationGUI();
        LinkedList<String> playerNames = new LinkedList<>();
        playerNames.add("Anastasia");
        board = new Board.Builder().build();
        player = new Player(1, "Anastasia", board);
        game = new Game(1, playerNames, new StandardDice(1), board, true, gui);
    }

    @Test
    public void testPlayerInitialization() {
        assertEquals(1, player.getId());
        assertEquals("Anastasia", player.getNickname());
        assertEquals(0, player.getTurnsToWait());
        assertFalse(player.getHasANoStoppingCard());
    }

    @Test
    public void testAdvanceOf() {
        int initialPosition = player.getCurrentPosition();
        int newPosition = player.advanceOf(3);

        assertEquals(initialPosition + 3, newPosition);
    }


    @Test
    public void testSetCurrentPosition() {
        Square destination = board.getSquareFromNumber(5);
        player.setCurrentPosition(destination);
        assertEquals(5, player.getCurrentPosition());
    }

    @Test
    void testRollWithDoubleSix() {
        Dice baseDice = new StandardDice(2);
        Dice doubleSixDice = new DoubleSixDecorator(baseDice);

        int result = doubleSixDice.roll(0);
        assertTrue(result >= 2 && result <= 12, "Il risultato del lancio deve essere tra 2 e 12");

        if (result == 12) {
            int secondResult = doubleSixDice.roll(0);
            assertTrue(secondResult >= 2 && secondResult <= 12, "Dovrebbe lanciare nuovamente dopo un doppio sei");
        }
    }

    @Test
    void testSingleDieDecorator() {
        Dice baseDice = new StandardDice(2);
        Dice singleDieAtEndDice = new SingleDieDecorator(baseDice, board);

        int result = singleDieAtEndDice.roll(95);
        assertTrue(result >= 1 && result <= 6, "Quando il giocatore è quasi alla fine, deve lanciare un solo dado");

        int normalResult = singleDieAtEndDice.roll(10);
        assertTrue(normalResult >= 2 && normalResult <= 12, "Per le altre posizioni, il giocatore deve usare due dadi");
    }

    @Test
    void testAutoAdvanceStrategy() throws InterruptedException {
        LinkedList<String> playerNames = new LinkedList<>();
        playerNames.add("Anastasia");
        Game game = new Game(1, playerNames, new StandardDice(1), board, true, null);

        game.getStrategy().advance(game, new ApplicationGUI());
        Thread.sleep(5000);
        assertNotEquals(0, game.getPlayers().getFirst().getCurrentPosition(), "Il giocatore deve avanzare automaticamente");
    }

    @Test
    void testManualAdvanceStrategy() throws InterruptedException {
        LinkedList<String> playerNames = new LinkedList<>();
        playerNames.add("Anastasia");
        Game game = new Game(1, playerNames, new StandardDice(1), board, false, null);

        game.getStrategy().advance(game, null);
        assertEquals(0, game.getPlayers().getFirst().getCurrentPosition(), "In modalità manuale, il giocatore non deve avanzare automaticamente");
    }

    @Test
    public void testBenchEffect() {
        Square benchSquare = new Square(5, new BenchEffect());
        benchSquare.applyEffect(game, player);
        assertEquals(1, player.getTurnsToWait(), "Il giocatore dovrebbe attendere un turno.");
    }

    @Test
    public void testChanceEffect() {
        Square chanceSquare = new Square(7, new ChanceEffect());
        int initialPosition = player.getCurrentPosition();
        chanceSquare.applyEffect(game, player);
        assertNotEquals(initialPosition, player.getCurrentPosition(), "Il giocatore dovrebbe muoversi a causa dell'effetto Chance.");
    }

    @Test
    public void testDrawACardEffect() {
        Deck.getInstance(true);
        Square drawCardSquare = new Square(8, new DrawACardEffect());
        drawCardSquare.applyEffect(game, player);
        assertTrue(player.getHasANoStoppingCard() || player.getTurnsToWait() > 0, "Il giocatore dovrebbe ricevere un effetto dal mazzo.");
    }

    @Test
    public void testMoveEffect() {
        Square destinationSquare = board.getSquareFromNumber(10);
        Square moveSquare = new Square(3, new MoveEffect(destinationSquare));
        moveSquare.applyEffect(game, player);
        assertEquals(10, player.getCurrentPosition(), "Il giocatore dovrebbe essere spostato alla casella 10.");
    }

    @Test
    public void testNoStoppingEffect() {
        Card noStoppingCard = new Card(4, new NoStoppingEffect());
        noStoppingCard.applyEffect(game, player);
        assertTrue(player.getHasANoStoppingCard(), "Il giocatore dovrebbe avere una No Stopping card.");
    }

}


