package test;

import static org.junit.jupiter.api.Assertions.*;

import main.components.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.memento.Board;
import main.components.Player;

public class PlayerTest {

    private Board board;
    private Player player;

    @BeforeEach
    public void setUp() {
        board = new Board.Builder().build();
        player = new Player(1, "Anastasia", board);
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
}


