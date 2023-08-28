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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GraphDoor extends Combat {

    int i = 0;
    boolean ok = false;

    int verticeSize = 20;
    Graph<Integer> graph = new Graph<>();

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
            Random rand = new Random();


            int e = 0;
            int v = 5+rand.nextInt(5);
            int x = 370;
            int y = 160;

            int r1 = 200;
            int r2 = 150;
            int t = 0;
            ArrayList<Integer> listX = new ArrayList<>();
            ArrayList<Integer> listY = new ArrayList<>();

            int first = 0, second = 0;

            g.setColor(Color.black);
            for(t = 1; t <= v; t++) {
                listX.add((int)(Math.cos(t) * r1) + x+10);
                listY.add((int)(Math.sin(t) * r2) + y+10);
                g.fillOval((int)(Math.cos(t) * r1) + x, (int)(Math.sin(t) * r2) + y, verticeSize, verticeSize);
            }
            g.setColor(Color.red);
            for(t = 1; t <= 2*v-rand.nextInt(v); t++) {
                g.setColor(new Color(50+rand.nextInt(205),50+rand.nextInt(205),50+rand.nextInt(205)));
                first = rand.nextInt(v);
                second = rand.nextInt(v);
                if(first != second) {
                    e++;
                }
                Graphics2D g2 = (Graphics2D)g;
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(listX.get(first), listY.get(first), listX.get(second), listY.get(second));
            }

            System.out.println("v = " + v);
            System.out.println("e = " + e);
        }
    }

}
