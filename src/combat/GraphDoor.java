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

    int verticeSize = 30;

    int verticeOne;
    int verticeTwo;
    int verticeThree;
    int verticeFour;
    int verticeFive;

    public GraphDoor() {
        createMainField();
    }

    @Override
    public void run() { /* The door doesn't attack. */ }
    @Override
    public void createMainField() {

        JFrame f = new JFrame("Draw a line");
        f.getContentPane().add(new DrawMyCercle());
        f.setSize(800, 600);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void createObject() {

    }

    @Override
    public String play() {
        return null;
    }

    public class DrawMyCercle extends JPanel {
        @Override
        public void paint(Graphics g) {
            g.setColor(Color.red);

            g.fillOval(50, 350, verticeSize, verticeSize);
            g.fillOval(125, 300, verticeSize, verticeSize);
            g.fillOval(200, 225, verticeSize, verticeSize);
            g.fillOval(275, 125, verticeSize, verticeSize);
            g.fillOval(350, 150, verticeSize, verticeSize);
            g.fillOval(550, 100, verticeSize, verticeSize);
            g.fillOval(650, 350, verticeSize, verticeSize);
            g.fillOval(550, 450, verticeSize, verticeSize);
            g.fillOval(450, 550, verticeSize, verticeSize);
            g.fillOval(350, 450, verticeSize, verticeSize);

        }
    }

}
