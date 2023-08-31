package combat;

import main.GamePanel;
import javax.swing.*;
import java.awt.*;

import static main.Main.window;

public class GeometryDoor extends Combat {

    int i = 0;
    boolean ok = false;

    public GeometryDoor(GamePanel gp) {
        super(gp);
    }


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
        containerB.add(questionLabel);
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

        text = GamePanel.switchLanguage("Wrong answer!", "Nem talált!", "Mauvaise réponse!");
        solutionLabel = new JLabel(text);
        solutionLabel.setForeground(Color.black);
        solutionLabel.setBackground(Color.white);

        try {
            answerInt = Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            answerInt = 0;
        }

        if(answerInt == solution) {

            text = GamePanel.switchLanguage("Correct!", "Talált!", "Correct!");
            solutionLabel = new JLabel(text);
            solutionLabel.setForeground(Color.black);
            solutionLabel.setBackground(Color.green);
            ok = true;
        } else {
            text = GamePanel.switchLanguage("Wrong answer!", "Nem talált!", "Mauvaise réponse!");
            solutionLabel = new JLabel(text);
            solutionLabel.setForeground(Color.black);
            solutionLabel.setBackground(Color.red);
        }
        solutionLabel.setOpaque(true);
        solutionLabel.setBounds(50, 510, 700, 50);
        solutionLabel.setFont(font);
        containerB.add(solutionLabel);
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
            text = GamePanel.switchLanguage(
                    "The length of the side of a square: " + a + ". How much is its circumference?",
                    "Négyzet oldalának hossza: " + a + ". Mennyi a kerülete?",
                    "La longueur de côtés d'un carré: " + a + ". Combien est sa circonférence?");
            showQuestion(text);

            return 4 * a;
        } else if (i == 2) {

            text = GamePanel.switchLanguage(
                    "The length of the side of a square: " + a + ". How much is its area?",
                    "Négyzet oldalának hossza: " + a + ". Mennyi a területe?",
                    "La longueur de côtés d'un carré: " + a + ". Combien est sa superficie?"
            );
            showQuestion(text);

            return a * a;
        } else if (i == 3) {

            text = GamePanel.switchLanguage(
                    "The side lengths of a rectangle: " + a + ", "+ b + ". How much is its circumference?",
                    "Téglalap oldalainak hossza: " + a + ", "+ b + ". Mennyi a kerülete?",
                    "La longueur de côtés d'un rectangle: " + a + ", "+ b + ". Combien est sa circonférence?"
            );
            showQuestion(text);

            return (2*a)+(2*b);
        } else if (i == 4) {

            text = GamePanel.switchLanguage(
                    "The side lengths of a rectangle: " + a + ", "+ b + ". How much is its area?",
                    "Téglalap oldalainak hossza: " + a + ", "+ b + ". Mennyi a területe?",
                    "La longueur de côtés d'un rectangle: " + a + ", "+ b + ". Combien est sa superficie?"
                    );
            showQuestion(text);

            return a*b;
        }

        return 0;
    }

}
