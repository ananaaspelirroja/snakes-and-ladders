package main.strategy;

import main.gui.ApplicationGUI;
import main.memento.Game;

public interface AdvanceStrategy {
    void advance(Game game, ApplicationGUI gui) throws InterruptedException;
}
