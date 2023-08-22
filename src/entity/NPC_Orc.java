package entity;

import main.GamePanel;

public class NPC_Orc extends Entity{


    public NPC_Orc(GamePanel gp) {

        super(gp);
        direction = "down";
        speed = 4;
        getImage();
    }

    @Override
    public void getImage() {

        up1 = setUp("/npc/orc_up_1_new");
        up2 = setUp("/npc/orc_up_2_new");
        down1 = setUp("/npc/orc_down_1_new");
        down2 = setUp("/npc/orc_down_2_new");
        left1 = setUp("/npc/orc_left_1_new");
        left2 = setUp("/npc/orc_left_2_new");
        right1 = setUp("/npc/orc_right_1_new");
        right2 = setUp("/npc/orc_right_2_new");
    }

    @Override
    public void setAction() {

        actionLockCounter ++;
        if(collisionOn || actionLockCounter == 75) {

            int i = random.nextInt(100)+1; // pick up a number from 1 to 100
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
        if(spriteCounter > 12) {
            if(spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

}
