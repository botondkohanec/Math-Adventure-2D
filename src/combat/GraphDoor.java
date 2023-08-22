package combat;

import main.GamePanel;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

public class GraphDoor extends Combat {

    int i = 0;
    boolean ok = false;

    int verticeOne;
    int verticeTwo;
    int verticeThree;
    int verticeFour;
    int verticeFive;

    public GraphDoor() {
        createMainField();
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

            case GamePanel.eng: text = "Wrong answer!";
                break;
            case GamePanel.hun: text = "Nem talált!";
                break;
            case GamePanel.fr: text = "Mauvaise réponse!";
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

                case GamePanel.eng: text = "Correct!";
                    break;
                case GamePanel.hun: text = "Talált!";
                    break;
                case GamePanel.fr: text = "Correct!";
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
                case GamePanel.eng: text = "Wrong answer!";
                    break;
                case GamePanel.hun: text = "Nem talált!";
                    break;
                case GamePanel.fr: text = "Mauvaise réponse!";
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

        window.getContentPane().add(new DrawMyCercle());
        window.invalidate();
        window.validate();
        window.repaint();
    }

    @Override
    public void createMainField() {

        JFrame f = new JFrame("Draw a line");
        f.getContentPane().add(new DrawMyCercle());
        f.setSize(250, 250);
        f.setVisible(true);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void createBackGround() {

    }

    @Override
    public void createPanel() {

    }

    public int getSolution(int a, int b) {

        i = random.nextInt(4) + 1;
        String text = "";

        if(i == 1) {
            switch (GamePanel.language) {

                case GamePanel.eng: text = "The length of the side of a square: " + a + ". How much is its circumference?";
                    break;
                case GamePanel.hun: text = "Négyzet oldalának hossza: " + a + ". Mennyi a kerülete?";
                    break;
                case GamePanel.fr: text = "La longueur de côtés d'un carré: " + a + ". Combien est sa circonférence?";
                    break;
                default: text = "The length of the side of a square: " + a + ". How much is its circumference?";
                    break;
            }
            showQuestion(text);

            return 4 * a;
        } else if (i == 2) {

            switch (GamePanel.language) {

                case GamePanel.eng: text = "The length of the side of a square: " + a + ". How much is its area?";
                    break;
                case GamePanel.hun: text = "Négyzet oldalának hossza: " + a + ". Mennyi a területe?";
                    break;
                case GamePanel.fr: text = "La longueur de côtés d'un carré: " + a + ". Combien est sa superficie?";
                    break;
                default: text = "The length of the side of a square: " + a + ". How much is its area?";
                    break;
            }
            showQuestion(text);

            return a * a;
        } else if (i == 3) {

            switch (GamePanel.language) {
                case GamePanel.eng: text = "The side lengths of a rectangle: " + a + ", "+ b + ". How much is its circumference?";
                    break;
                case GamePanel.hun: text = "Téglalap oldalainak hossza: " + a + ", "+ b + ". Mennyi a kerülete?";
                    break;
                case GamePanel.fr: text = "La longueur de côtés d'un rectangle: " + a + ", "+ b + ". Combien est sa circonférence?";
                    break;
                default: text = "The side lengths of a rectangle: " + a + ", "+ b + ". How much is its circumference?";
                    break;
            }
            showQuestion(text);

            return (2*a)+(2*b);
        } else if (i == 4) {

            switch (GamePanel.language) {
                case GamePanel.eng: text = "The side lengths of a rectangle: " + a + ", "+ b + ". How much is its area?";
                    break;
                case GamePanel.hun: text = "Téglalap oldalainak hossza: " + a + ", "+ b + ". Mennyi a területe?";
                    break;
                case GamePanel.fr: text = "La longueur de côtés d'un rectangle: " + a + ", "+ b + ". Combien est sa superficie?";
                    break;
                default: text = "The side lengths of a rectangle: " + a + ", "+ b + ". How much is its area?";
                    break;
            }
            showQuestion(text);

            return a*b;
        }

        return 0;
    }


    public class DrawMyCercle extends JPanel {
        public void paint(Graphics g) {

            g.setColor(Color.red);
            g.drawOval(50, 40, 50, 50);
        }
    }

}
