package object;

import main.AssetSetter;
import main.GamePanel;
import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Door extends SuperObject{

    GamePanel gp;
    String type;


    public OBJ_Door(int index, GamePanel gp, String type) {

        super(index);
        this.gp = gp;

        name = "Door"+type;

        try{
            if(type.equals(AssetSetter.KEY)) {
                image = ImageIO.read(getClass().getResourceAsStream("/objects/door_K.png"));
            } else {
                image = ImageIO.read(getClass().getResourceAsStream("/objects/door_G.png"));
            }
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch(IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }

}
