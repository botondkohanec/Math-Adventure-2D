package combat;

import main.GamePanel;
import javax.swing.*;
import java.awt.*;
import static main.GamePanel.*;
import static main.Main.window;

public class CombatWolfMan extends Combat implements Runnable {


    public CombatWolfMan(GamePanel gp) {

        super(gp);
        enemyHP = 125;
        enemyAttack = 0;
    }

    @Override
    public void createObject() {

        JLabel objectLabel = new JLabel();
        objectLabel.setBounds(0, 100, 200, 353);

        ImageIcon objectIcon = new ImageIcon(getClass().getClassLoader().getResource("wolfman.png"));
        objectLabel.setIcon(objectIcon);

        panel.add(objectLabel);

        addKnight();
    }

    @Override
    public String play() {

        enemyHP = 125;
        this.startGameThread();

        int a = 0;
        int b = 0;
        int product = 0;
        int r;
        int solution = 0;
        int szamlalo = 1;

        while (true) {
            showEnemyHP();
            szamlalo = 100;
            a = random.nextInt(szamlalo) + 1;
            b = random.nextInt(szamlalo) + 1;
            product = a * b;
            r = random.nextInt(3);
            if (szamlalo > 10) r = random.nextInt(6);
            solution = getSolution(a, b, product, r);
            while (!inputHandler.next && entity.Player.hp > 0) {
                System.out.println(inputHandler.next);
            }
            showSolution(inputHandler.text, solution);
            inputHandler.next = false;
            if (finale()) {
                break;
            }
        }

        return endText;
    }

    @Override
    public void run() {

        while(gameThread != null) {

            if (entity.Player.hp > 0 && enemyHP > 0) {

                playSE(12);
                for(int j = 1; j <= 5; j++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    showPlayerHP();
                    if(entity.Player.hp <= 0 || enemyHP <= 0) {
                        gameThread = null;
                        break;
                    }
                }

                enemyAttack = (random.nextInt(50)+145) / entity.Player.armor;
                entity.Player.hp -= enemyAttack;
                showPlayerHP();
                if(entity.Player.hp <= 0 || enemyHP <= 0) {
                    gameThread = null;
                    break;
                }

                for(int i = 10; i >= 1; i--) {
                    showCountDown(i);
                    for(int j = 1; j <= 10; j++) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        showPlayerHP();
                        if(entity.Player.hp <= 0 || enemyHP <= 0) {
                            gameThread = null;
                            break;
                        }
                    }
                }
                showCountDown(0);
            } else {
                gp.setVisible(true);
                gameThread = null;
                break;
            }

        }

    }

    public int getSolution(int a, int b, int product, int r) {

        switch (r) {

            case 1: showQuestion(a + " * ? = " + product);
                return b;
            case 2: showQuestion("? * " + b + " = " + product);
                return a;
            case 3: showQuestion(product + " / " + a + " = ?");
                return b;
            case 4: showQuestion(product + " / ? = " + b);
                return a;
            case 5: showQuestion("? / " + a + " = " + b);
                return product;
            default: showQuestion(a + " * " + b + " = ?");
                return product;
        }

    }

    @Override
    public boolean finale() {

        String text = "";
        if(enemyHP <=0 ) {

            Combat.playerVictory = true;
            text = GamePanel.switchLanguage("Victory!", "Győzelem!", "Victoire!");
            showQuestion(text);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            panel.setVisible(false);
            inputPanel.setVisible(false);
            enemyHPLabel.setVisible(false);
            playerHPLabel.setVisible(false);
            questionLabel.setVisible(false);
            solutionLabel.setVisible(false);
            bGLabel.setVisible(false);
            countdownLabel.setVisible(false);
            text = GamePanel.switchLanguage("VICTORY! You defeated the Beast!",
                    "GYŐZELEM! Legyőzted a Szörnyeteget!",
                    "VICTOIRE! Tu as vaincu le Monstre!");
            endText = text;
            playSE(13);

            return true;
        } else if (entity.Player.hp <= 0) {

            Combat.playerVictory = false;
            text = GamePanel.switchLanguage("You lost!", "Vesztettél!", "Tu as perdu!");
            showQuestion(text);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            panel.setVisible(false);
            inputPanel.setVisible(false);
            enemyHPLabel.setVisible(false);
            playerHPLabel.setVisible(false);
            questionLabel.setVisible(false);
            solutionLabel.setVisible(false);
            bGLabel.setVisible(false);
            countdownLabel.setVisible(false);
            text = GamePanel.switchLanguage("THE WOLFMAN DEFEATED YOU!",
                    "LEGYŐZÖTT A FARKASEMBER!",
                    "LE LOUP-GAROU T'A VAINCU!");
            endText = text;

            return  true;
        }
        return false;
    }

    @Override
    public void showEnemyHP() {

        String text = "";
        if(enemyHPLabel != null) enemyHPLabel.setVisible(false);
        text = GamePanel.switchLanguage("Wolfman: ", "Farkasember: ", "Loup-garou: ");
        enemyHPLabel = new JLabel( text + enemyHP);
        enemyHPLabel.setBackground(Color.gray);
        enemyHPLabel.setForeground(Color.red);
        enemyHPLabel.setBounds(150, 0, 350, 50);
        enemyHPLabel.setFont(font);
        containerB.add(enemyHPLabel);
        window.invalidate();
        window.validate();
        window.repaint();
    }

}