package entity;

import combat.Combat;
import combat.GeometryDoor;
import main.GamePanel;
import main.KeyHandler;
import object.OBJ_Apple;
import object.OBJ_Boom;
import object.OBJ_Key;
import object.SuperObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity{

    public KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    public int hasKey = 0;

    public static int level = 1;
    public static int exp = 0;
    public static int nextLevel = 0;
    public static int coin = 0;
    public static final int SWORD = 0;
    public static int weapon = SWORD;
    public static final int maxHP = 1000;
    public static int hp = maxHP;
    public static int armor = 10;
    public static int attack = 0;

    public SuperObject[] items = new SuperObject[36];
    public int itemsNumber = 0;


    public Player(GamePanel gp, KeyHandler keyH) {

        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWith/2 - gp.tileSize/2;
        screenY = gp.screenHeight/2 - gp.tileSize/2;

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getImage();
    }

    public void setDefaultValues() {

        if(gp.mapNum == 1) {
            worldX = gp.tileSize * 17;
            worldY = gp.tileSize * 41;
        }
        else if(gp.mapNum == 2) {
            worldX = gp.tileSize * 30;
            worldY = gp.tileSize * 10;
        }
        direction = "down";
        for(int i = 0; i < items.length; i++) {
            items[i] = null;
        }
        hp = maxHP;
        hasKey = 0;
        itemsNumber = 0;
        coin = 0;
        speed = 4;
    }

    @Override
    public void getImage() {

        up1 = setUp("/player/player_up_1");
        up2 = setUp("/player/player_up_2");
        down1 = setUp("/player/player_down_1");
        down2 = setUp("/player/player_down_2");
        left1 = setUp("/player/player_left_1");
        left2 = setUp("/player/player_left_2");
        right1 = setUp("/player/player_right_1");
        right2 = setUp("/player/player_right_2");
    }

    @Override
    public void update() {

        if(entity.Player.hp <= 0) GamePanel.gameState = GamePanel.GameState.GAME_OVER_STATE;

        // SPEAK
        speakCounter++;
        if(speakCounter > 1000) speakCounter = 0; // for safe
        if(keyH.speakPressed && speakCounter > 100) {
            gp.playSE(5);
            speakCounter = 0;
        }

        if(keyH.upPressed || keyH.downPressed ||
           keyH.leftPressed || keyH.rightPressed) {

            if(keyH.upPressed)          direction = "up";
            else if(keyH.downPressed)   direction = "down";
            else if(keyH.leftPressed)   direction = "left";
            else if(keyH.rightPressed)  direction = "right";

            // CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // CHECK OBJECT COLLISION
            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            // CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interractNPC(npcIndex);

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if(!collisionOn) {

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

    public void pickUpObject(int i) {

        if(i != 999) {

            String objectName = gp.obj[i].name;

            switch(objectName) {
                case "Key":
                    pickUpKey(i);
                    break;
                case "DoorGeometry":
                    tryOpenGDoor(i);
                    break;
                case "DoorKey":
                    tryOpenKDoor(i);
                    break;
                case "Apple":
                    pickUpApple(i);
                    break;
                case "GApple":
                    pickUpGApple(i);
                    break;
                case "Boots":
                    pickUpBoots(i);
                    break;
                case "Bomb":
                    boom(i);
                    break;
                case "Math_Coin":
                    pickUpMath_Coin(i);
                    break;
                default: break;
            }
        }
    }

    private void boom(int i) {

        entity.Player.hp = 0;

        gp.obj[0] = new OBJ_Boom(0, gp);
        gp.obj[0].worldX = gp.obj[i].worldX;
        gp.obj[0].worldY = gp.obj[i].worldY;

        gp.obj[1] = new OBJ_Boom(1, gp);
        gp.obj[1].worldX = worldX;
        gp.obj[1].worldY = worldY;

        gp.ui.addMessage("Boom!!");
        gp.playSE(11);
    }
    private void pickUpBoots(int i) {

        String text = "";
        gp.playSE(16);
        speed += 1;
        gp.obj[i] = null;
        text = GamePanel.switchLanguage("Speed up!", "Gyorsabb lettél!", "Tu t'as accéléré!");
        gp.ui.addMessage(text);
    }
    private void pickUpGApple(int i) {
        String text = "";
        gp.playSE(10);
        gp.obj[i] = null;
        entity.Player.hp = entity.Player.maxHP;
        text = GamePanel.switchLanguage("You are completely healed!",
                "Teljesen meggyógyultál!",
                "Tu t'es complètement rétabli!");
        gp.ui.addMessage(text);
    }
    private void pickUpApple(int i) {

        if(itemsNumber >= 36) return;
        gp.obj[i] = null;
        boolean enough = false;
        for(int l = 0; l < 6; l++) {
            for(int j = 0; j < 6; j++) {
                if(gp.player.items[l*6+j] == null) {
                    items[l*6+j] = new OBJ_Apple(i, gp);
                    enough = true;
                    break;
                }
            }
            if(enough) break;
        }
        itemsNumber++;

        String text;
        text = GamePanel.switchLanguage("You got an apple!", "Szereztél egy almát!", "Francia!");
        gp.ui.addMessage(text);
    }
    private void tryOpenKDoor(int i) {

        String text = "";
        if(hasKey > 0) {
            gp.playSE(17);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            gp.obj[i] = null;
            hasKey--;
            text = GamePanel.switchLanguage("You opened the door!", "Kinyitottad az ajtót!",
                    "Tu as ouvert la porte!");
            gp.ui.addMessage(text);

            for(int l = 5; l >= 0; l--) {
                for(int j = 5; j >= 0; j--) {
                    if(gp.player.items[l*6+j] != null && gp.player.items[l*6+j] instanceof OBJ_Key) {
                        gp.player.items[l*6+j] = null;
                        itemsNumber--;
                        return;
                    }
                }
            }
        }
        else {
            gp.playSE(7);
            text = GamePanel.switchLanguage("You need a key!", "Szükséged van egy kulcsra!",
                    "Tu as besoin d'une clé!");
            gp.ui.addMessage(text);
        }
    }
    private void tryOpenGDoor(int i) {

        String text = "";
        gp.playSE(3);
        if(true) {
            GamePanel.gameState = GamePanel.GameState.PAUSE_STATE;
            keyH.upPressed = false;
            keyH.downPressed = false;
            keyH.leftPressed = false;
            keyH.rightPressed = false;
            Combat openTheDoor = new GeometryDoor();
            String s = openTheDoor.play();
            if(worldY > gp.obj[i].worldY) worldY ++;
            else worldY --;
            if(s.equals("open")) {
                gp.playSE(18);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                gp.obj[i] = null;
                text = GamePanel.switchLanguage("You opened the door!", "Kinyitottad az ajtót!",
                        "Tu as ouvert la porte!");
                gp.ui.addMessage(text);
            } else gp.playSE(7);

            GamePanel.gameState = GamePanel.GameState.PLAY_STATE;
        }
    }
    private void pickUpKey(int i) {

        if(itemsNumber >= 36) return;
        boolean enough = false;
        for(int l = 0; l < 6; l++) {
            for(int j = 0; j < 6; j++) {
                if(gp.player.items[l*6+j] == null) {
                    items[l*6+j] = new OBJ_Key(i, gp);
                    enough = true;
                    break;
                }
            }
            if(enough) break;
        }
        itemsNumber++;

        String text = "";
        gp.playSE(1);
        hasKey++;
        gp.obj[i] = null;
        text = GamePanel.switchLanguage("You got a key!", "Szereztél egy kulcsot!",
                "Tu as obtenu une clé!");
        gp.ui.addMessage(text);
    }
    private void pickUpMath_Coin(int i) {

        String text = "";
        gp.playSE(19);
        entity.Player.coin++;
        gp.obj[i] = null;
        text = GamePanel.switchLanguage("You got a mathcoin!", "Szereztél egy matérmét!", "Francia!");
        gp.ui.addMessage(text);
    }

    public void interractNPC(int i) {

        if(i != 999) {

            if(gp.npc[i] instanceof NPC_King) {

                GamePanel.gameState = GamePanel.GameState.DIALOGUE_STATE;
                gp.npc[i].speak();
            }
            else if(gp.npc[i] instanceof NPC_Princess) {

                gp.stopMusic();
                gp.playSE(4);
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                GamePanel.gameState = GamePanel.GameState.RESOURCES_STATE_END;
            }
            else if(gp.npc[i] instanceof NPC_Boss) {

                GamePanel.gameState = GamePanel.GameState.COMBAT_STATE;
                GamePanel.combatNPC = GamePanel.CombatNPC.BOSS;
                GamePanel.stopMusic();
                GamePanel.playSE(14);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                GamePanel.playMusic(15);
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        BufferedImage image = null;

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
            default: break;
        }
        g2.drawImage(image, screenX, screenY, null);
    }

}
