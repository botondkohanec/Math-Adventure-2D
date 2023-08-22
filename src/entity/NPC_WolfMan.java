package entity;

import main.GamePanel;

public class NPC_WolfMan extends Entity{


    public NPC_WolfMan(GamePanel gp) {

        super(gp);
        direction = "down";
        speed = 6;
        getImage();
    }

    public void getImage() {

        up1 = setUp("/npc/wolfman_up_1");
        up2 = setUp("/npc/wolfman_up_2");
        down1 = setUp("/npc/wolfman_down_1");
        down2 = setUp("/npc/wolfman_down_2");
        left1 = setUp("/npc/wolfman_left_1");
        left2 = setUp("/npc/wolfman_left_2");
        right1 = setUp("/npc/wolfman_right_1");
        right2 = setUp("/npc/wolfman_right_2");
    }

    @Override
    public void setAction() {

        actionLockCounter ++;
        if(collisionOn || actionLockCounter == 50) {

            int i = random.nextInt(100)+1;
            if(i <= 25)                 direction = "up";
            else if(i > 25 && i <= 50)  direction = "down";
            else if(i > 50 && i <= 75)  direction = "left";
            else/*(i > 75 && i <= 100)*/direction = "right";
            actionLockCounter = 0;
        }
    }

    @Override
    public void update() {

        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkPlayer(index);

        // IF COLLISION IS FALSE, PLAYER CAN MOVE
        if(collisionOn == false) {

            switch(direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
                default: break;
            }
        }

        spriteCounter++;
        if(spriteCounter > 8) {
            if(spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

}