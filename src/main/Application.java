package main;

import main.gui.ApplicationGUI;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ApplicationGUI().setVisible(true);
            }
        });
    }
}
