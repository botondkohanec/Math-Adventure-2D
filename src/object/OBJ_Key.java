package object;

import main.GamePanel;
import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Key extends SuperObject{

    GamePanel gp;


    public OBJ_Key(int index, GamePanel gp) {
        super(index);
        this.gp = gp;

        setName("Key");
        setImage("/objects/key_B.png");
        description = GamePanel.switchLanguage("Key - It's open a door",
                "Kulcs - Kinyit egy\najtót", "Francia");
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
