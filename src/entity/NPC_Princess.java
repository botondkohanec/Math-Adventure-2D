package entity;

import main.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NPC_Princess extends Entity{


    public NPC_Princess(GamePanel gp) {

        super(gp);
        direction = "down";
        speed = 1;
        getImage();
    }

    @Override
    public void getImage() {
        down1 = setUp("/npc/princess");
    }

    @Override
    public void draw(Graphics2D g2) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        BufferedImage image = down1;
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

}
