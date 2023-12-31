package entity;

import main.GamePanel;
import main.UtilityTool;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public abstract class Entity {

    GamePanel gp;

    public int worldX, worldY;
    public int speed = 0;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;
    public int spriteCounter = 0;
    public int speakCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea = new Rectangle(0, 0, 40, 40);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public int actionLockCounter = 0;
    String dialogues[] = new String[20];
    int dialogueIndex = 0;
    public int index;

    Random random = new Random();


    protected Entity(GamePanel gp) {
        this.gp = gp;
    }

    public abstract void getImage();

    public void setAction() {
        // He is not moving
    }

    public void speak() {}
    public void update() {}
    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
           worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
           worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
           worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {

            image = chooseImage(image);

            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }

    public BufferedImage chooseImage(BufferedImage image) {

        switch(direction) {
            case "up" :
                if(spriteNum == 1) image = up1;
                if(spriteNum == 2) image = up2;
                break;
            case "down" :
                if(spriteNum == 1) image = down1;
                if(spriteNum == 2) image = down2;
                break;
            case "left" :
                if(spriteNum == 1) image = left1;
                if(spriteNum == 2) image = left2;
                break;
            case "right" :
                if(spriteNum == 1) image = right1;
                if(spriteNum == 2) image = right2;
                break;
            default: image = down1;
        }

        return image;
    }

    public BufferedImage setUp(String imagePath) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath +".png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);

        } catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}
