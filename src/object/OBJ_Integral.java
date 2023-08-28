package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Integral extends SuperObject{

    GamePanel gp;


    public OBJ_Integral(int index, GamePanel gp) {

        super(index);
        this.gp = gp;

        name = "Integral";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/integral.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch(IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }

}