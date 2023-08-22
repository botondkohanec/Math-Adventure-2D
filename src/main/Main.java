package main;

import javax.swing.*;

public class Main {


    public static void main(String[] args) {

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Math Adventure 2D");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack();

        ImageIcon icon = new ImageIcon(Main.class.getClassLoader().getResource("player_down_1.png"));

        window.setLocationRelativeTo(null);
        window.setIconImage(icon.getImage());
        window.setVisible(true);

        gamePanel.setUpGame();
        gamePanel.startGameThread();
    }
}
