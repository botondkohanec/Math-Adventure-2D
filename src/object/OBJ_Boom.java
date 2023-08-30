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
        setName("Boom");
        setImage("/objects/boom_B.png");
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
