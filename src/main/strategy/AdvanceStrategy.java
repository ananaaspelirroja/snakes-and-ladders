package main.strategy;

import main.memento.Game;

public interface AdvanceStrategy {
    void advance(Game game) throws InterruptedException;
}
