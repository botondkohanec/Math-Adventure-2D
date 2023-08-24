package combat;

import main.GamePanel;
import javax.swing.*;
import java.awt.*;

public class GeometryDoor extends Combat {

    int i = 0;
    boolean ok = false;


    @Override
    public String play() {

        int a = 0;
        int b = 0;

        int solution = 0;
        int szamlalo = 1;

        szamlalo = 100;
        a = random.nextInt(szamlalo) + 1;
        b = random.nextInt(szamlalo) + 1;
        solution = getSolution(a, b);
        while (!inputHandler.next && entity.Player.hp > 0) {
            System.out.println(inputHandler.next);
        }
        showSolution(inputHandler.text, solution);
        inputHandler.next = false;

        if (ok) {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            endText = "open";
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        window.setVisible(false);
        return endText;
    }

    @Override
    public void showQuestion(String question) {

        if(questionLabel != null) questionLabel.setVisible(false);
        questionLabel = new JLabel(question);
        questionLabel.setBackground(Color.getHSBColor(50,200,200));
        questionLabel.setForeground(Color.black);
        questionLabel.setOpaque(true);
        questionLabel.setBounds(50, 410, 700, 50);
        questionLabel.setFont(new Font("Times New Roman", Font.PLAIN, 23));
        container.add(questionLabel);
        window.invalidate();
        window.validate();
        window.repaint();
    }

    @Override
    public void run() { /* The door doesn't attack. */ }

    @Override
    public void showSolution(String answer, int solution) {

        String text = "";
        answer = answer.replaceAll(" ", "");

        if(solutionLabel != null) {
            solutionLabel.setVisible(false);
        }

        switch (GamePanel.language) {

            case ENG: text = "Wrong answer!";
                break;
            case HUN: text = "Nem talált!";
                break;
            case FR: text = "Mauvaise réponse!";
                break;
            default: text = "Wrong answer!";
                break;
        }
        solutionLabel = new JLabel(text);
        solutionLabel.setForeground(Color.black);
        solutionLabel.setBackground(Color.white);

        try {
            answerInt = Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            answerInt = 0;
        }

        if(answerInt == solution) {

            switch (GamePanel.language) {

                case ENG: text = "Correct!";
                    break;
                case HUN: text = "Talált!";
                    break;
                case FR: text = "Correct!";
                    break;
                default: text = "Correct!";
                    break;
            }
            solutionLabel = new JLabel(text);
            solutionLabel.setForeground(Color.black);
            solutionLabel.setBackground(Color.green);
            ok = true;
        } else {
            switch (GamePanel.language) {
                case ENG: text = "Wrong answer!";
                    break;
                case HUN: text = "Nem talált!";
                    break;
                case FR: text = "Mauvaise réponse!";
                    break;
                default: text = "Wrong answer!";
                    break;
            }
            solutionLabel = new JLabel(text);
            solutionLabel.setForeground(Color.black);
            solutionLabel.setBackground(Color.red);
        }
        solutionLabel.setOpaque(true);
        solutionLabel.setBounds(50, 510, 700, 50);
        solutionLabel.setFont(font);
        container.add(solutionLabel);
        window.invalidate();
        window.validate();
        window.repaint();
    }

    @Override
    public void createObject() {

        JLabel objectLabel = new JLabel();
        objectLabel.setBounds(0, 0, 700, 350);

        ImageIcon objectIcon = new ImageIcon(getClass().getClassLoader().getResource("gate.png"));
        objectLabel.setIcon(objectIcon);

        panel.add(objectLabel);
    }

    public int getSolution(int a, int b) {

        i = random.nextInt(4) + 1;
        String text = "";

        if(i == 1) {
            switch (GamePanel.language) {

                case ENG: text = "The length of the side of a square: " + a + ". How much is its circumference?";
                    break;
                case HUN: text = "Négyzet oldalának hossza: " + a + ". Mennyi a kerülete?";
                    break;
                case FR: text = "La longueur de côtés d'un carré: " + a + ". Combien est sa circonférence?";
                    break;
                default: text = "The length of the side of a square: " + a + ". How much is its circumference?";
                    break;
            }
            showQuestion(text);

            return 4 * a;
        } else if (i == 2) {

            switch (GamePanel.language) {

                case ENG: text = "The length of the side of a square: " + a + ". How much is its area?";
                    break;
                case HUN: text = "Négyzet oldalának hossza: " + a + ". Mennyi a területe?";
                    break;
                case FR: text = "La longueur de côtés d'un carré: " + a + ". Combien est sa superficie?";
                    break;
                default: text = "The length of the side of a square: " + a + ". How much is its area?";
                    break;
            }
            showQuestion(text);

            return a * a;
        } else if (i == 3) {

            switch (GamePanel.language) {
                case ENG: text = "The side lengths of a rectangle: " + a + ", "+ b + ". How much is its circumference?";
                    break;
                case HUN: text = "Téglalap oldalainak hossza: " + a + ", "+ b + ". Mennyi a kerülete?";
                    break;
                case FR: text = "La longueur de côtés d'un rectangle: " + a + ", "+ b + ". Combien est sa circonférence?";
                    break;
                default: text = "The side lengths of a rectangle: " + a + ", "+ b + ". How much is its circumference?";
                    break;
            }
            showQuestion(text);

            return (2*a)+(2*b);
        } else if (i == 4) {

            switch (GamePanel.language) {
                case ENG: text = "The side lengths of a rectangle: " + a + ", "+ b + ". How much is its area?";
                    break;
                case HUN: text = "Téglalap oldalainak hossza: " + a + ", "+ b + ". Mennyi a területe?";
                    break;
                case FR: text = "La longueur de côtés d'un rectangle: " + a + ", "+ b + ". Combien est sa superficie?";
                    break;
                default: text = "The side lengths of a rectangle: " + a + ", "+ b + ". How much is its area?";
                    break;
            }
            showQuestion(text);

            return a*b;
        }

        return 0;
    }

}
