package main;

import javax.swing.*;
import java.awt.*;

public final class Main {

    public static JFrame window;
    public static Container container;

    public static void main(String[] args) {

        window = new JFrame();
        window.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Math Adventure 2D");
        window.setUndecorated(true);

        container = window.getContentPane();

        GamePanel gamePanel = new GamePanel();
        container.add(gamePanel);

        window.pack();

        ImageIcon icon = new ImageIcon(Main.class.getClassLoader().getResource("player_down_1.png"));

        window.setLocationRelativeTo(null);
        window.setIconImage(icon.getImage());
        window.setVisible(true);

        gamePanel.setUpGame();
        gamePanel.startGameThread();
    }
}
