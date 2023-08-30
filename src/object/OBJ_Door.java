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

        setName("Door"+type);


        if(type.equals(AssetSetter.KEY)) {
            setImage("/objects/door_K.png");
        } else {
            setImage("/objects/door_G.png");
        }
        collision = true;
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
