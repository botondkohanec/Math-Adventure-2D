package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_Apple extends SuperObject{

    GamePanel gp;


    public OBJ_Apple(int index, GamePanel gp) {

        super(index);
        this.gp = gp;

        setName("Apple");
        setImage("/objects/apple_B.png");
        int x;
        if(GamePanel.difficulty == GamePanel.Difficulty.EASY) x = 100;
        else x = 50;
        description = GamePanel.switchLanguage("It gives " +  x + " HP",
                "Ad " + x + " HP-t", "Francia "+ x + " HP");

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
