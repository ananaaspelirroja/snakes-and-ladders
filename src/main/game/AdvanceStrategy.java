package main.game;

public interface AdvanceStrategy {
    void advance(Game game) throws InterruptedException;
}
