package main;

import combat.Combat;
import combat.CombatOrc;
import combat.CombatBoss;
import combat.CombatWolfMan;
import entity.Entity;
import entity.Player;
import object.OBJ_Apple;
import object.OBJ_Key;
import object.OBJ_MathCoin;
import object.SuperObject;
import tile.TileManager;
import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    final int originalTileSize = 16;//16; // 16 x 16 tile
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48 x 48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWith = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // WORLD SETTINGS
    public int maxWorldCol = 35;
    public int maxWorldRow = 43;
    public int mapNum = 2;

    // FPS
    int FPS = 60;

    // SYSTEM
    TileManager tileM;
    KeyHandler keyH = new KeyHandler(this);
    public static Sound music = new Sound();
    public static Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Thread gameThread;

    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public SuperObject[] obj = new SuperObject[100];
    public Entity[] npc = new Entity[100];

    // GAME STATE
    public static int gameState;
    public static final int titleState = 0;
    public static final int languageState = 1;
    public static final int difficultyState = 2;
    public static final int playState = 3;
    public static final int pauseState = 4;
    public static final int dialogueState = 5;
    public static final int combatState = 6;
    public static final int gameOverState = 7;
    public static final int resourcesState = 8;
    public static final int statusState = 9;
    public static final int menuState = 10;

    public static int combatNPC;
    public static final int orc = 1;
    public static final int wolf = 2;
    public static final int boss = 3;

    public static int language;
    public static final int eng = 1;
    public static final int hun = 2;
    public static final int fr = 3;

    public static int difficulty;
    public static final int easy = 1;
    public static final int medium = 2;
    public static final int hard = 3;
    public static final int mathematician = 4;

    int stop = 0;
    int coinIndex = 90;


    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWith,screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        setFocusable(true);
    }

    public void setUpGame() {

        gameState = languageState;
        ui.commandNum = 0;
        if(mapNum == 1) {

            maxWorldCol = 35;
            maxWorldRow = 43;
        } else if (mapNum == 2){

            maxWorldCol = 50;
            maxWorldRow = 30;
        }
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        while(gameThread != null) {

            double drawInterval = 1000000000/FPS; // 0.01666 seconds
            double nextDrawTime = System.nanoTime() + drawInterval;

            update();
            repaint();

            try {
                // erre van egy delta method is
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime/1000000;

                if(remainingTime < 0) { remainingTime = 0; }

                Thread.sleep((long)remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update() {

        for(int i = 0; i < se.openedClips.size(); i++) {
            if(se.openedClips.get(i) != null && !se.openedClips.get(i).isRunning() &&
                !se.openedClips.get(i).isActive()) {
                se.openedClips.get(i).close();
                se.openedClips.remove(i);
            }
        }
        if(gameState == playState) {

            stop = 0;

            // PLAYER
            player.update();
            // NPC
            for(int i = 0; i < npc.length; i++) {
                if(npc[i] != null) {
                    npc[i].update();
                }
            }
        }
        if(gameState == statusState) {
            stop = 0;
        }
        if(gameState == pauseState) {
            stop = 0;
        }
        if(gameState == combatState) {

            stop ++;
            if(stop <= 1) {
                if(coinIndex > 98) coinIndex = 90;
                String s = "";
                if (combatNPC == orc) {

                    CombatOrc combatOrc = new CombatOrc();
                    s = combatOrc.play();
                    if(Combat.playerVictory) {
                        if(Combat.perfect) {
                            obj[Combat.NPCcombatIndex] = new OBJ_Apple(Combat.NPCcombatIndex, this);
                            obj[Combat.NPCcombatIndex].worldX = npc[Combat.NPCcombatIndex].worldX;
                            obj[Combat.NPCcombatIndex].worldY = npc[Combat.NPCcombatIndex].worldY;
                        }
                        obj[coinIndex] = new OBJ_MathCoin(coinIndex, this);
                        obj[coinIndex].worldX = npc[Combat.NPCcombatIndex].worldX-10;
                        obj[coinIndex].worldY = npc[Combat.NPCcombatIndex].worldY-10;
                        coinIndex++;
                        npc[Combat.NPCcombatIndex] = null;
                    } else {
                        npc[Combat.NPCcombatIndex].solidArea = new Rectangle(0, 0, 0, 0);
                    }
                }
                else if(combatNPC == wolf) {

                    CombatWolfMan combatWolfMan = new CombatWolfMan();
                    s = combatWolfMan.play();
                    if(Combat.playerVictory) {
                        if(Combat.perfect) {
                            obj[Combat.NPCcombatIndex] = new OBJ_Apple(Combat.NPCcombatIndex, this);
                            obj[Combat.NPCcombatIndex].worldX = npc[Combat.NPCcombatIndex].worldX;
                            obj[Combat.NPCcombatIndex].worldY = npc[Combat.NPCcombatIndex].worldY;
                        }
                        obj[coinIndex] = new OBJ_MathCoin(coinIndex, this);
                        obj[coinIndex].worldX = npc[Combat.NPCcombatIndex].worldX-10;
                        obj[coinIndex].worldY = npc[Combat.NPCcombatIndex].worldY-10;
                        coinIndex++;
                        if(Combat.NPCcombatIndex == 2 || Combat.NPCcombatIndex == 3) {
                            obj[99] = new OBJ_Key(99, this);
                            obj[99].worldX = npc[Combat.NPCcombatIndex].worldX+5;
                            obj[99].worldY = npc[Combat.NPCcombatIndex].worldY+5;
                        }
                        npc[Combat.NPCcombatIndex] = null;
                    } else {
                        npc[Combat.NPCcombatIndex].solidArea = new Rectangle(0, 0, 0, 0);
                    }
                }
                else if(combatNPC == boss) {

                    CombatBoss combatBoss = new CombatBoss();
                    s = combatBoss.play();

                    if(Combat.playerVictory) {
                        obj[coinIndex] = new OBJ_MathCoin(coinIndex, this);
                        obj[coinIndex].worldX = npc[2].worldX-10;
                        obj[coinIndex].worldY = npc[2].worldY-10;
                        coinIndex++;
                        npc[2] = null;
                    } else {
                        npc[2].solidArea = new Rectangle(0, 0, 0, 0);
                    }
                    stopMusic();
                }

                ui.addMessage(s);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if(stop > 10000) stop = 1;

            player.keyH.downPressed = false;
            player.keyH.upPressed = false;
            player.keyH.leftPressed = false;
            player.keyH.rightPressed = false;
            player.keyH.speakPressed = false;
        }
        if(gameState == statusState) {
            if(player.items[keyH.i+keyH.j*6] instanceof OBJ_Apple && ui.enterPressed) {
                String text = "";
                playSE(10);
                player.items[keyH.i+keyH.j*6] = null;
                int x = 0;
                if(GamePanel.difficulty == GamePanel.easy) x = 100;
                else x = 50;
                entity.Player.hp += x;
                if(entity.Player.hp > entity.Player.maxHP) entity.Player.hp = entity.Player.maxHP;
                switch (GamePanel.language) {
                    case GamePanel.eng: text = "You got " +  x + " HP!";
                        break;
                    case GamePanel.hun: text = "Szereztél " + x + " HP-t!";
                        break;
                    case GamePanel.fr: text = "Tu as obtenu "+ x + " HP!";
                        break;
                    default: text = "You got " + x + " HP!";
                        break;
                }
                ui.addMessage(text);
            }
        }
        if(gameState == menuState) {
            stop = 0;
        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // DEBUG
        long drawStart = 0;
        if (keyH.checkDrawTime) { drawStart = System.nanoTime(); }

        // RESOURCES SCREEN
        if (gameState == resourcesState) { ui.draw(g2); }

        // TITLE & LANGUAGE SCREEN
        if (gameState == titleState || gameState == languageState || gameState == difficultyState) {
            ui.draw(g2);
        }

        // OTHERS
        else {
            // TILE
            tileM.draw(g2);

            // OBJECT
            for (int i = 0; i < obj.length; i++) {
                if (obj[i] != null) { obj[i].draw(g2, this); }
            }

            // NPC
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) { npc[i].draw(g2); }
            }

            // PLAYER
            player.draw(g2);

            // UI
            ui.draw(g2);
        }

        // DEBUG
        if(keyH.checkDrawTime) {

            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: "+passed, 10, 400);
            System.out.println("Draw Time: "+passed);
        }

        g2.dispose();
    }

    public static void playMusic(int i) {

        music.setFile(i);
        music.play();
        music.loop();
    }

    public static void stopMusic() {
        music.stop();
    }

    public static void playSE(int i) {

        se.setFile(i);
        se.play();
    }

}
