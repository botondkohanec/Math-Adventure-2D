package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_GoldApple extends SuperObject{

    GamePanel gp;


    public OBJ_GoldApple(int index, GamePanel gp) {

        super(index);
        this.gp = gp;

        name = "GApple";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/goldapple_B.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
