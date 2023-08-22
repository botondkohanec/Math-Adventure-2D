package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class OBJ_MathCoin extends SuperObject{

    GamePanel gp;


    public OBJ_MathCoin(int index, GamePanel gp) {

        super(index);
        this.gp = gp;

        name = "Math_Coin";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/math_coin.png"));
            uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
