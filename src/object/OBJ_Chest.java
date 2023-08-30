package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Chest extends SuperObject{

    GamePanel gp;


    public OBJ_Chest(int index, GamePanel gp) {

        super(index);
        this.gp = gp;

        setName("Chest");
        setImage("/objects/chest_B.png");
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public void setImage(String path) {
        try{
            image = ImageIO.read(getClass().getResourceAsStream(path));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
