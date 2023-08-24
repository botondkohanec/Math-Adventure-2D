package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Apple extends SuperObject{

    GamePanel gp;


    public OBJ_Apple(int index, GamePanel gp) {

        super(index);
        this.gp = gp;

        name = "Apple";
        int x;
        if(GamePanel.difficulty == GamePanel.Difficulty.EASY) x = 100;
        else x = 50;
        switch (GamePanel.language) {
            case ENG: description = "It gives " +  x + " HP";
                break;
            case HUN: description = "Ad " + x + " HP-t";
                break;
            case FR: description = "Francia "+ x + " HP";
                break;
            default: description = "You got " + x + " HP";
                break;
        }
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/apple_B.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
