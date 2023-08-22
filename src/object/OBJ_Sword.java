package object;


import main.GamePanel;
import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Sword extends SuperObject{

    GamePanel gp;


    public OBJ_Sword(int index, GamePanel gp) {

        super(index);
        this.gp = gp;

        name = "Sword";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/key_B.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}

