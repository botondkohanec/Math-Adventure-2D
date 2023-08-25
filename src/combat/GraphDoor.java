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

    int verticeSize = 20;

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

        JFrame f = new JFrame("Draw a graph");
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

            int x = 370;
            int y = 160;

            int r1 = 200;
            int r2 = 150;
            int t = 0;

            for(t = 1; t <= 15; t++)
            g.fillOval((int)(Math.cos(t)*r1)+x, (int)(Math.sin(t)*r2)+y, verticeSize, verticeSize);

        }
    }

}
