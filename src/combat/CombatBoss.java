package combat;

import main.GamePanel;
import javax.swing.*;
import java.awt.*;
import static main.GamePanel.*;

public class CombatBoss extends Combat implements Runnable {


    public CombatBoss() {
        super();
    }

    @Override
    public void createObject() {

        JLabel objectLabel = new JLabel();
        objectLabel.setBounds(0, 15, 300, 323);

        ImageIcon objectIcon = new ImageIcon(getClass().getClassLoader().getResource("devil.png"));
        objectLabel.setIcon(objectIcon);

        panel.add(objectLabel);

        addKnight();
    }

    @Override
    public String play() {

        if(difficulty == easy) enemyHP = 350;
        else enemyHP = 500;
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
                showEnemyAttack(enemyAttack);
                entity.Player.hp -= enemyAttack;
                showPlayerHP();
                if(entity.Player.hp <= 0 || enemyHP <= 0) {
                    gameThread = null;
                    break;
                }

                int a = 8;
                if(difficulty == easy) a += 2;
                for(int i = a; i >= 1; i--) {
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
            switch (GamePanel.language) {
                case eng: text = "Victory!";
                    break;
                case hun: text = "Győzelem!";
                    break;
                case fr: text = "Victoire!";
                    break;
                default: text = "Victory!";
                    break;
            }
            showQuestion(text);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            window.setVisible(false);
            switch (GamePanel.language) {
                case eng: text = "VICTORY! You defeated the Beast!";
                    break;
                case hun: text = "GYŐZELEM! Legyőzted a Szörnyeteget!";
                    break;
                case fr: text = "VICTOIRE! Tu as vaincu le Monstre!";
                    break;
                default: text = "VICTORY! You defeated the Beast!";
                    break;
            }
            endText = text;
            playSE(13);

            return true;
        } else if (entity.Player.hp <= 0) {

            Combat.playerVictory = false;
            switch (GamePanel.language) {
                case eng: text = "You lost!";
                    break;
                case hun: text = "Vesztettél!";
                    break;
                case fr: text = "Tu as perdu!";
                    break;
                default: text = "You lost!";
                    break;
            }
            showQuestion(text);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            window.setVisible(false);
            switch (GamePanel.language) {
                case eng: text = "THE DEVIL DEFEATED YOU!";
                    break;
                case hun: text = "LEGYŐZÖTT AZ ÖRDÖG!";
                    break;
                case fr: text = "LE DIABLE T'A VAINCU!";
                    break;
                default: text = "THE DEVIL DEFEATED YOU!";
                    break;
            }
            endText = text;

            return  true;
        }
        return false;
    }

    @Override
    public void showEnemyHP() {
        String text = "";
        if(enemyHPLabel != null) enemyHPLabel.setVisible(false);
        switch (GamePanel.language) {
            case eng: text = "Devil: ";
                break;
            case hun: text = "Ördög: ";
                break;
            case fr: text = "Diable: ";
                break;
            default: text = "Devil: ";
                break;
        }
        enemyHPLabel = new JLabel( text + enemyHP);
        enemyHPLabel.setBackground(Color.gray);
        enemyHPLabel.setForeground(Color.red);
        enemyHPLabel.setBounds(50, 0, 350, 50);
        enemyHPLabel.setFont(font);
        container.add(enemyHPLabel);
        window.invalidate();
        window.validate();
        window.repaint();

    }

}