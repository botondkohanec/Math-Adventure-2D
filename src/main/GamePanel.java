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
import java.awt.image.BufferedImage;
import java.io.*;

import static main.Main.container;
import static main.Main.window;

public class GamePanel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    final int originalTileSize = 16;//16; // 16 x 16 tile
    final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 48 x 48 tile
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWith = tileSize * maxScreenCol; // 960 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // WORLD SETTINGS
    public int maxWorldCol = 35;
    public int maxWorldRow = 43;
    public int mapNum = 2;

    // FOR FULL SCREEN
    public int screenWith2 = screenWith;
    public int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    public boolean fullScreen = false;

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

    public enum GameState {
        TITLE_STATE,
        LANGUAGE_STATE,
        DIFFICULTY_STATE,
        PLAY_STATE,
        PAUSE_STATE,
        DIALOGUE_STATE,
        COMBAT_STATE,
        GAME_OVER_STATE,
        RESOURCES_STATE_END,
        RESOURCES_STATE_TITLESCREEN,
        STATUS_STATE,
        MENU_STATE,
        CONTROL_STATE;
    }
    public static GameState gameState;

    public enum CombatNPC {
        ORC,
        WOLF,
        BOSS;
    }
    public static CombatNPC combatNPC;

    public enum Language {
        ENG,
        HUN,
        FR,
        DEFAULT;
    }
    public static Language language = Language.DEFAULT;
    public static String switchLanguage(String eng, String hun, String fr) {
        switch (GamePanel.language) {
            case HUN: return hun;
            case FR: return fr;
            default: return eng;
        }
    }

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD,
        MATHEMATICIAN;

        @Override
        public String toString() {
            switch (this) {
                case MEDIUM -> {
                    return "MEDIUM";
                }
                case HARD -> {
                    return "HARD";
                }
                case MATHEMATICIAN -> {
                    return "MATHEMATICIAN";
                }
                default -> {
                    return "EASY";
                }
            }
        }
    }
    public static Difficulty difficulty = Difficulty.EASY;

    int stop = 0;
    int stop2 = 0;
    int coinIndex = 90;


    public GamePanel() {

        window.setPreferredSize(new Dimension(screenWith2,screenHeight2));
//        this.setPreferredSize(new Dimension(screenWith2,screenHeight2));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        setFocusable(true);
    }

    public void setUpGame() {

        ui.commandNum = 0;
        if(mapNum == 1) {

            maxWorldCol = 35;
            maxWorldRow = 43;
        } else if (mapNum == 2){

            maxWorldCol = 68;
            maxWorldRow = 40;
        }
        tempScreen = new BufferedImage(screenWith, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D)tempScreen.getGraphics();
        try {
            loadConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(language == Language.DEFAULT) {
            gameState = GameState.LANGUAGE_STATE;
        } else {
            gameState = GameState.TITLE_STATE;
        }
    }

    public void setFullScreen() {


        // GET LOCAL SCREEN DEVICE
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(window);

        // GET FULL SCREEN WIDTH & HEIGHT
        screenWith2 = window.getWidth();
        screenHeight2 = window.getHeight();

        this.setSize(new Dimension(screenWith2,screenHeight2));

        System.out.println(this.getWidth());
        System.out.println(this.getHeight());
    }

    public void setNormalScreen() {

        this.setSize(new Dimension(screenWith2,screenHeight2));

        // GET LOCAL SCREEN DEVICE
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(null);

        // GET FULL SCREEN WIDTH & HEIGHT
        screenWith2 = screenWith;
        screenHeight2 = screenHeight;

        System.out.println(this.getWidth());
        System.out.println(this.getHeight());
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
            drawToTempScreen(); // draw everything to the buffered image
            drawToScreen(); // draw the buffered image to the screen

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
        if(gameState == GameState.PLAY_STATE) {
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
        if(gameState == GameState.STATUS_STATE) {
            stop = 0;
        }
        if(gameState == GameState.PAUSE_STATE) {
            stop = 0;
        }
        if(gameState == GameState.COMBAT_STATE) {

            stop ++;
            if(stop <= 1) {
                if(coinIndex > 98) coinIndex = 90;
                String s = "";
                if (combatNPC == CombatNPC.ORC) {

                    CombatOrc combatOrc = new CombatOrc(this);
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
                else if(combatNPC == CombatNPC.WOLF) {

                    CombatWolfMan combatWolfMan = new CombatWolfMan(this);
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
                else if(combatNPC == CombatNPC.BOSS) {

                    CombatBoss combatBoss = new CombatBoss(this);
                    s = combatBoss.play();

                    if(Combat.playerVictory) {
                        obj[coinIndex] = new OBJ_MathCoin(coinIndex, this);
                        obj[coinIndex].worldX = npc[2].worldX-10;
                        obj[coinIndex].worldY = npc[2].worldY-10;
                        coinIndex++;

                        obj[99] = new OBJ_Key(99, this);
                        obj[99].worldX = npc[2].worldX+5;
                        obj[99].worldY = npc[2].worldY+5;

                        npc[2] = null;
                    } else {
                        npc[2].solidArea = new Rectangle(0, 0, 0, 0);
                    }
                    stopMusic();
                }
                System.out.println(this.getWidth());
                System.out.println(this.getHeight());
                this.setVisible(true);
                this.requestFocus();

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
        if(gameState == GameState.STATUS_STATE) {
            if(player.items[keyH.i+keyH.j*6] instanceof OBJ_Apple && ui.enterPressed) {
                String text = "";
                playSE(10);
                player.items[keyH.i+keyH.j*6] = null;
                int x = 0;
                if(GamePanel.difficulty == Difficulty.EASY) x = 100;
                else x = 50;
                entity.Player.hp += x;
                if(entity.Player.hp > entity.Player.maxHP) entity.Player.hp = entity.Player.maxHP;
                text = switchLanguage("You got " +  x + " HP!",
                        "Szereztél " + x + " HP-t!",
                        "Tu as obtenu "+ x + " HP!");
                ui.addMessage(text);
            }
        }
        if(gameState == GameState.MENU_STATE) {
            stop = 0;
        }
    }

    public void drawToTempScreen() {
        // DEBUG
        long drawStart = 0;
        if (keyH.checkDrawTime) { drawStart = System.nanoTime(); }

        // RESOURCES SCREEN
        if (gameState == GameState.RESOURCES_STATE_END) { ui.draw(g2); }

        // TITLE & LANGUAGE SCREEN
        if (gameState == GameState.TITLE_STATE || gameState == GameState.LANGUAGE_STATE ||
                gameState == GameState.DIFFICULTY_STATE ||
                gameState == GameState.RESOURCES_STATE_TITLESCREEN) {
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
    }
    public void drawToScreen() {

        Graphics g = getGraphics();

        g.drawImage(tempScreen, 0, 0, screenWith2, screenHeight2, null);
        g.dispose();
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

    public void load() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("save.data"));
        br.readLine();
        switch (Integer.parseInt(br.readLine())) {
            case 1 -> difficulty = Difficulty.MEDIUM;
            case 2 -> difficulty = Difficulty.HARD;
            case 3 -> difficulty = Difficulty.MATHEMATICIAN;
            default -> difficulty = Difficulty.EASY;
        }
        Player.hp = Integer.parseInt(br.readLine());
        Player.attack = Integer.parseInt(br.readLine());
        Player.armor = Integer.parseInt(br.readLine());
        player.speed = Integer.parseInt(br.readLine());
        Player.exp = Integer.parseInt(br.readLine());
        Player.nextLevel = Integer.parseInt(br.readLine());
        Player.coin = Integer.parseInt(br.readLine());
        Player.weapon = Integer.parseInt(br.readLine());
        player.hasKey = Integer.parseInt(br.readLine());
        player.itemsNumber = Integer.parseInt(br.readLine());
        for(int i = 0; i < player.items.length; i++) {
            String s = br.readLine();
            if(!s.equals("null")) {
                switch (s) {
                    case "Key" -> player.items[i] = new OBJ_Key(Integer.parseInt(br.readLine()), this);
                    default -> player.items[i] = new OBJ_Apple(Integer.parseInt(br.readLine()), this);
                }
            } else {
                player.items[i] = null;
            }
        }
        for(int i = 0; i < obj.length; i++) {
            String s = br.readLine();
            if(s.equals("null")) {
                obj[i] = null;
            }
        }
        for(int i = 0; i < npc.length; i++) {
            String s = br.readLine();
            if(s.equals("null")) {
                npc[i] = null;
            }
        }
    }

    public void save() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("save.data"));
        switch (difficulty) {
            case MEDIUM -> bufferedWriter.write("" + 1);
            case HARD -> bufferedWriter.write("" + 2);
            case MATHEMATICIAN -> bufferedWriter.write("" + 3);
            default -> bufferedWriter.write("" + 0);
        }
        bufferedWriter.newLine();
        bufferedWriter.write(""+Player.level);
        bufferedWriter.newLine();
        bufferedWriter.write(""+Player.hp);
        bufferedWriter.newLine();
        bufferedWriter.write(""+Player.attack);
        bufferedWriter.newLine();
        bufferedWriter.write(""+Player.armor);
        bufferedWriter.newLine();
        bufferedWriter.write(""+player.speed);
        bufferedWriter.newLine();
        bufferedWriter.write(""+Player.exp);
        bufferedWriter.newLine();
        bufferedWriter.write(""+Player.nextLevel);
        bufferedWriter.newLine();
        bufferedWriter.write(""+Player.coin);
        bufferedWriter.newLine();
        bufferedWriter.write(""+Player.weapon);
        bufferedWriter.newLine();
        bufferedWriter.write(""+player.hasKey);
        bufferedWriter.newLine();
        bufferedWriter.write(""+player.itemsNumber);
        for(int i = 0; i < player.items.length; i++) {
            bufferedWriter.newLine();
            if(player.items[i] != null) {
                bufferedWriter.write(player.items[i].name);
                bufferedWriter.newLine();
                bufferedWriter.write(player.items[i].index+"");
            } else {
                bufferedWriter.write("null");
            }
        }
        for(int i = 0; i < obj.length; i++) {
            bufferedWriter.newLine();
            if(obj[i] != null) {
                bufferedWriter.write("obj");
            } else {
                bufferedWriter.write("null");
            }
        }
        for(int i = 0; i < npc.length; i++) {
            bufferedWriter.newLine();
            if(npc[i] != null) {
                bufferedWriter.write("obj");
            } else {
                bufferedWriter.write("null");
            }
        }
        bufferedWriter.close();

    }

    public void loadConfig() throws IOException {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("config.data"));
        } catch (Exception e) {
            return;
        }
        int a = Integer.parseInt(br.readLine());
        switch (a) {
            case 1 -> language = language.HUN;
            case 2 -> language = language.FR;
            default -> language = language.ENG;
        }
        se.volumeScale = Integer.parseInt(br.readLine());
        music.volumeScale = Integer.parseInt(br.readLine());
        if(Integer.parseInt(br.readLine()) == 1) {
            setFullScreen();
            fullScreen = true;
        }
    }

    public void saveConfig() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("config.data"));
        switch (language) {
            case HUN -> bufferedWriter.write("" + 1);
            case FR -> bufferedWriter.write("" + 2);
            default -> bufferedWriter.write("" + 0);
        }
        bufferedWriter.newLine();
        bufferedWriter.write(""+se.volumeScale);
        bufferedWriter.newLine();
        bufferedWriter.write(""+music.volumeScale);
        bufferedWriter.newLine();
        if(fullScreen) {
            bufferedWriter.write("" + 1);
        } else {
            bufferedWriter.write("" + 0);
        }
        bufferedWriter.close();
    }

}
