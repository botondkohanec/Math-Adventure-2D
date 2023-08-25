package object;

import main.GamePanel;
import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Key extends SuperObject{

    GamePanel gp;


    public OBJ_Key(int index, GamePanel gp) {
        super(index);
        this.gp = gp;

        name = "Key";
        description = GamePanel.switchLanguage("Key - It's open a door",
                "Kulcs - Kinyit egy\najtót", "Francia");
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/key_B.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
