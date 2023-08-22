package object;

import main.GamePanel;
import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Bomb extends SuperObject{

    GamePanel gp;


    public OBJ_Bomb(int index, GamePanel gp) {

        super(index);
        this.gp = gp;
        collision = true;

        name = "Bomb";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/bomb_B.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
