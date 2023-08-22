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
        switch (GamePanel.language) {

            case GamePanel.eng: description = "Key - It's open a door";
                break;
            case GamePanel.hun: description = "Kulcs - Kinyit egy\najtót";
                break;
            case GamePanel.fr: description = "Francia";
                break;
            default: description = "Key - It's open a door";
                break;
        }
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/key_B.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
