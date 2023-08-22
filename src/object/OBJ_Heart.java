package object;

import main.GamePanel;
import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Heart extends SuperObject{

    GamePanel gp;


    public OBJ_Heart(int index, GamePanel gp) {
        super(index);
        this.gp = gp;

        name = "Boots";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/heart.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}