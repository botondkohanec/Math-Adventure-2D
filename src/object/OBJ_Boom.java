package object;

import main.GamePanel;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class OBJ_Boom extends SuperObject{

    GamePanel gp;


    public OBJ_Boom(int index, GamePanel gp) {

        super(index);
        this.gp = gp;

        solidArea = new Rectangle(0,0,96,96);
        name = "Boom";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/boom_B.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
