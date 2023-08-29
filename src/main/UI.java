package main;

import entity.NPC_Princess;
import object.OBJ_Heart;
import object.OBJ_Key;
import tile.TileManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class UI {

    GamePanel gp;

    Graphics2D g2;
    Font arial40;
    Font arial80B;

    BufferedImage keyImage;
    BufferedImage heartImage;
    BufferedImage ryiSnowImage;
    BufferedImage princessImage;
    BufferedImage swordImage;
    BufferedImage swordImageUp;
    BufferedImage engImage;
    BufferedImage hunImage;
    BufferedImage frImage;
    BufferedImage tileImage;

    public boolean messageOn = false;
    public ArrayList<String> messageList = new ArrayList<>();
    public ArrayList<Integer> messageCounter = new ArrayList<>();

    public String currentDialogue = "";
    public int commandNum = 0;
    public static int i = 0;

    public boolean enterPressed = false;
    int stop = 0;
    long timeA = 0;
    long timeB = 0;

    double playTime;
    DecimalFormat dFormat = new DecimalFormat("#0.00");


    public UI(GamePanel gp) {

        this.gp = gp;

        arial40 = new Font("Yu Gothic UI", Font.PLAIN, 40);
        arial80B = new Font("Yu Gothic UI", Font.BOLD, 80);
        OBJ_Key key = new OBJ_Key(-1, gp);
        keyImage = key.image;
        OBJ_Heart heart = new OBJ_Heart(-1, gp);
        heartImage = heart.image;
        NPC_Princess princess = new NPC_Princess(gp);
        ryiSnowImage = princess.setUp("/player/RyiSnow");
        princessImage = princess.down1;
        swordImage = princess.setUp("/objects/sword");
        swordImageUp = princess.setUp("/objects/sword_up");
        engImage = princess.setUp("/objects/eng");
        hunImage = princess.setUp("/objects/hun");
        frImage = princess.setUp("/objects/fr");
        tileImage = princess.setUp("/tiles/grass");
    }

    public void addMessage(String text) {

        messageList.add(text);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2) {

        this.g2 = g2;

        g2.setFont(arial40);
        g2.setColor(Color.white);

        // LANGUAGE STATE
        if(GamePanel.gameState == GamePanel.GameState.LANGUAGE_STATE) {

            if(enterPressed) {

                enterPressed = false;
                GamePanel.gameState = GamePanel.GameState.TITLE_STATE;
                if(gp.ui.commandNum == 0) GamePanel.language = GamePanel.Language.ENG;
                if(gp.ui.commandNum == 1) GamePanel.language = GamePanel.Language.HUN;
                if(gp.ui.commandNum == 2) GamePanel.language = GamePanel.Language.FR;
            }

            drawLanguageScreen();
        }

        // TITLE STATE
        if(GamePanel.gameState == GamePanel.GameState.TITLE_STATE) {

            if(enterPressed) {

                gp.ui.enterPressed = false;
                if(gp.ui.commandNum == 0) GamePanel.gameState = GamePanel.GameState.DIFFICULTY_STATE;
                if(gp.ui.commandNum == 1) {
                    enterPressed = false;
                    gp.tileM = new TileManager(gp);
                    gp.player.setDefaultValues();
                    gp.aSetter.setObject();
                    gp.aSetter.setNPC();
                    GamePanel.gameState = GamePanel.GameState.PLAY_STATE;
                    gp.playMusic(0);
                    gp.ui.commandNum = 0;
                    messageList = new ArrayList<>();
                    messageCounter = new ArrayList<>();
                    try {
                            gp.load();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                }
                if(gp.ui.commandNum == 2) GamePanel.gameState = GamePanel.GameState.LANGUAGE_STATE;
                if(gp.ui.commandNum == 3) {
                    GamePanel.gameState = GamePanel.GameState.RESOURCES_STATE_TITLESCREEN;
                }
                if(gp.ui.commandNum == 4) System.exit(0);
            }

            drawTitleScreen();
        }

        // DIFFICULTY STATE
        if(GamePanel.gameState == GamePanel.GameState.DIFFICULTY_STATE) {

            if(enterPressed) {

                enterPressed = false;
                if(gp.ui.commandNum == 0) GamePanel.difficulty = GamePanel.Difficulty.EASY;
                if(gp.ui.commandNum == 1) GamePanel.difficulty = GamePanel.Difficulty.MEDIUM;
                if(gp.ui.commandNum == 2) GamePanel.difficulty = GamePanel.Difficulty.HARD;
                if(gp.ui.commandNum == 3) GamePanel.difficulty = GamePanel.Difficulty.MATHEMATICIAN;
                gp.tileM = new TileManager(gp);
                gp.player.setDefaultValues();
                gp.aSetter.setObject();
                gp.aSetter.setNPC();
                GamePanel.gameState = GamePanel.GameState.PLAY_STATE;
                gp.playMusic(0);
                gp.ui.commandNum = 0;
                messageList = new ArrayList<>();
                messageCounter = new ArrayList<>();
            }

            drawDifficultyScreen();
        }

        // PLAY STATE
        if(GamePanel.gameState == GamePanel.GameState.PLAY_STATE) {


            drawSubWindowBlack(gp.screenWith - gp.tileSize*4-20-12, gp.tileSize / 2-10, 200, gp.tileSize+20);
            g2.setColor(new Color(165, 42, 42));
            g2.fillRoundRect(gp.screenWith - gp.tileSize*4-20-12+5, gp.tileSize / 2-10+5,
                    200*gp.player.hp/gp.player.maxHP-10, gp.tileSize+20-10, 35, 35);
            g2.setFont(arial40);
            g2.setColor(Color.black);
            g2.drawImage(heartImage, gp.screenWith - gp.tileSize*2 + 10, gp.tileSize / 2, gp.tileSize, gp.tileSize, null);
//            g2.drawString(entity.Player.hp + " x", gp.screenWith - gp.tileSize*4-20, 65);

            g2.setColor(Color.white);
            // MESSAGE
            drawMessage();
        }

        // PAUSE STATE
        if(GamePanel.gameState == GamePanel.GameState.PAUSE_STATE) {
            drawPauseScreen();
        }

        // DIALOGUE STATE
        if(GamePanel.gameState == GamePanel.GameState.DIALOGUE_STATE) {
            drawDialogueScreen();
        }

        // COMBAT STATE
        if(GamePanel.gameState == GamePanel.GameState.COMBAT_STATE) {
            drawCombatScreen();
        }

        // GAME OVER STATE
        if(GamePanel.gameState == GamePanel.GameState.GAME_OVER_STATE) {

            gp.stopMusic();
            if(i == 0) gp.playSE(2);
            i++;

            if (enterPressed) {

                enterPressed = false;
                if (gp.ui.commandNum == 0) {

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }

                    gp.player.setDefaultValues();
                    entity.Player.hp = entity.Player.maxHP;
                    gp.aSetter.setObject();
                    gp.aSetter.setNPC();
                    gp.player.hasKey = 0;
                    UI.i = 0;
                    GamePanel.gameState = GamePanel.GameState.PLAY_STATE;
                    gp.playMusic(0);
                }
                if (gp.ui.commandNum == 1) System.exit(0);
            }

            drawGameOverScreen();
        }

        // RESOURCES STATE (END)
        if(GamePanel.gameState == GamePanel.GameState.RESOURCES_STATE_END) {


            if(commandNum == 0)         {
                drawVictoryScreen();
                changeAfter4seconds();
            }
            else if(commandNum == 1)    {
                drawResourcesCodeScreen();
                changeAfter4seconds();
            }
            else if(commandNum == 2)    {
                drawResourcesSoundScreen();
                changeAfter4seconds();
            }
            else if(commandNum == 3)    {
                drawResourcesPictureScreen();
                changeAfter4seconds();
            }
            else if(commandNum == 4)    {
                drawResourcesTranslationsScreen();
                changeAfter4seconds();
            }
            else if(commandNum == 5)    {
                drawKohi();
                changeAfter4seconds();
            }
            else if(commandNum == 6)    System.exit(0);
        }

        // RESOURCES STATE (TITLESCREEN)
        if(GamePanel.gameState == GamePanel.GameState.RESOURCES_STATE_TITLESCREEN) {

            if(commandNum == 0)    {
                drawResourcesCodeScreen();
                changeAfter4seconds();
            }
            else if(commandNum == 1)    {
                drawResourcesSoundScreen();
                changeAfter4seconds();
            }
            else if(commandNum == 2)    {
                drawResourcesPictureScreen();
                changeAfter4seconds();
            }
            else if(commandNum == 3)    {
                drawResourcesTranslationsScreen();
                changeAfter4seconds();
            }
            else if(commandNum == 4)    {
                drawKohi();
                changeAfter4seconds();
            }
            else if(commandNum == 5)    {
                commandNum = 0;
                GamePanel.gameState = GamePanel.GameState.TITLE_STATE;
            }
        }

        // STATUS STATE
        if(GamePanel.gameState == GamePanel.GameState.STATUS_STATE) {
            drawStatusState();
        }

        // MENU STATE
        if(GamePanel.gameState == GamePanel.GameState.MENU_STATE) {
            if(enterPressed) {

                gp.ui.enterPressed = false;
                if(gp.ui.commandNum == 0) ;
                if(gp.ui.commandNum == 1) ;
                if(gp.ui.commandNum == 2) ;
                if(gp.ui.commandNum == 3) {
                    commandNum = 0;
                    GamePanel.gameState = GamePanel.GameState.CONTROL_STATE;
                };
                if(gp.ui.commandNum == 4) {
                    gp.stopMusic();
                    GamePanel.gameState = GamePanel.GameState.TITLE_STATE;
                }
                if(gp.ui.commandNum == 5) GamePanel.gameState = GamePanel.GameState.PLAY_STATE;
            }
            drawMenuState();
        }

        // CONTROL STATE
        if(GamePanel.gameState == GamePanel.GameState.CONTROL_STATE) {
            drawControlState();
            if(enterPressed) {
                if (gp.ui.commandNum == 0) {
                    gp.ui.enterPressed = false;
                    GamePanel.gameState = GamePanel.GameState.MENU_STATE;
                }
            }
        }
    }

    private void changeAfter4seconds() {
        if(stop < 1) {
            timeA = System.currentTimeMillis();
        }
        stop ++;
        if(stop > 10000) stop = 1;

        timeB = System.currentTimeMillis();

        if(timeB-timeA > 4000) {
            commandNum++;
            stop = 0;
        }
    }
    public void drawMessage() {

        int messageX = gp.tileSize/2;
        int messageY = gp.tileSize*4;
        g2.setFont(g2.getFont().deriveFont(Font.ITALIC, 25F));

        for(int i = 0; i < messageList.size(); i++) {

            if(messageList.get(i) != null) {

                g2.setColor(Color.white);
                g2.drawString(messageList.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1;
                messageCounter.set(i, counter);
                messageY += 30;

                if(messageCounter.get(i) > 180) {
                    messageList.remove(i);
                    messageCounter.remove(i);
                }

            }
        }
    }

    public void drawLanguageScreen() {

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 7; j++)
                g2.drawImage(tileImage, 2*gp.tileSize*i, 2*gp.tileSize*j, gp.tileSize * 2, gp.tileSize * 2, null);
        }

        String text = "";
        int x;
        int y;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));

        text = "English";
        x = getXforCenteredText(text);
        y = gp.tileSize*3;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);

        if(commandNum == 0) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            g2.drawImage(engImage, x+gp.tileSize*3-6, y-gp.tileSize+10, gp.tileSize, gp.tileSize, null);
        }

        text = "Magyar";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            g2.drawImage(hunImage, x+gp.tileSize*3+4, y-gp.tileSize+10, gp.tileSize, gp.tileSize, null);
        }

        text = "Français";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);
        if(commandNum == 2) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            g2.drawImage(frImage, x+gp.tileSize*3+10, y-gp.tileSize+10, gp.tileSize, gp.tileSize, null);
        }
    }
    public void drawTitleScreen() {

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 7; j++)
                g2.drawImage(tileImage, 2*gp.tileSize*i, 2*gp.tileSize*j, gp.tileSize * 2, gp.tileSize * 2, null);
        }

        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60F));

        String text = "";
        text = GamePanel.switchLanguage("Math Adventure 2D", "Matek Kaland 2D",
                "Aventures de Maths en 2D");
        int x = getXforCenteredText(text);
        int y = 5*gp.tileSize/2;

        // SHADOW
        g2.setColor(Color.black);
        g2.drawString(text, x+3, y+3);
        // MAIN COLOR
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);

        // CHARACTER IMAGE
        x = gp.screenWith/2 - gp.tileSize;
        y += gp.tileSize;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);

        // MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));

        text = GamePanel.switchLanguage("NEW GAME", "ÚJ JÁTÉK", "NOUVEAU JEU");
        x = getXforCenteredText(text);
        y += gp.tileSize*3.5;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }

        text = GamePanel.switchLanguage("LOAD GAME", "FOLYTATÁS", "FRANCIA");
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }

        text = GamePanel.switchLanguage("LANGUAGE", "NYELV", "FRANCIA");
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);
        if(commandNum == 2) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }

        text = GamePanel.switchLanguage("RESOURCES", "FORRÁSOK", "FRANCIA");
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);
        if(commandNum == 3) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }

        text = GamePanel.switchLanguage("QUIT", "KILÉPÉS", "QUITTER");
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);
        if(commandNum == 4) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }
    }
    public void drawDifficultyScreen() {

        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 7; j++)
                g2.drawImage(tileImage, 2*gp.tileSize*i, 2*gp.tileSize*j, gp.tileSize * 2, gp.tileSize * 2, null);
        }

        String text = "";
        int x;
        int y;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));

        text = GamePanel.switchLanguage("Easy", "Könnyű", "Facile");
        x = getXforCenteredText(text);
        y = gp.tileSize*3;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }

        text = GamePanel.switchLanguage("Medium", "Közepes", "Moyen");
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }

        text = GamePanel.switchLanguage("Hard", "Nehéz", "Difficile");
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);
        if(commandNum == 2) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }

        text = GamePanel.switchLanguage("Mathematician", "Matematikus", "Mathématicien");
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);
        if(commandNum == 3) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }
    }
    public void drawPauseScreen() {

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "";
        text = GamePanel.switchLanguage("PAUSE", "SZÜNET", "PAUSE");
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text, x, y);
    }
    public void drawCombatScreen() {

        g2.setColor(Color.red);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80F));
        String text = "";
        text = GamePanel.switchLanguage("COMBAT", "HARC", "COMBAT");
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text, x, y);
    }
    public void drawGameOverScreen() {

        g2.setColor(new Color( 0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWith, gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 75F));

        text = GamePanel.switchLanguage("GAME OVER", "VÉGE A JÁTÉKNAK", "FIN DE JEU");
        // Shadow
        g2.setColor(Color.black);
        x = getXforCenteredText(text);
        y = gp.tileSize*4;
        g2.drawString(text, x, y);
        // Main
        g2.setColor(Color.red);
        g2.drawString(text, x-4, y-4);

        // Retry
        g2.setFont(g2.getFont().deriveFont(50f));

        text = GamePanel.switchLanguage("Retry", "Újra", "De Nouveau");
        x = getXforCenteredText(text);
        y += gp.tileSize*4;
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }

        // Back to the title screen
        text = GamePanel.switchLanguage("Quit", "Kilép", "Quitter");
        x = getXforCenteredText(text);
        y += 55;
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }
    }
    public void drawDialogueScreen() {

        // WINDOW
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width = gp.screenWith - (gp.tileSize*4);
        int height = gp.tileSize*5;

        drawSubWindow(x, y, width, height);
        drawSubWindow(x+450, y+130, 100, 50, "ENTER");

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        x += gp.tileSize;
        y += gp.tileSize;

        for(String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }
    public void drawStatusState() {

        // WINDOW
        int x = gp.tileSize/2;
        int y = 3*gp.tileSize/4;
        int width = gp.tileSize*7;
        int height = gp.screenHeight - 3*gp.tileSize/2;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        x += gp.tileSize/2;
        y += gp.tileSize;

        String text = "";
        text = GamePanel.switchLanguage("Level", "Szint", "Francia");
        g2.drawString(text, x, y);
        g2.drawString("" + entity.Player.level, x + 165, y);
        y += 50;

        text = GamePanel.switchLanguage("HP", "HP", "HP");
        g2.drawString(text, x, y);
        g2.drawString("" + entity.Player.hp+"/"+ entity.Player.maxHP, x + 165, y);
        y += 50;

        text = GamePanel.switchLanguage("Attack", "Sebzés", "Francia");
        g2.drawString(text, x, y);
        g2.drawString("46-55", x + 165, y);
        y += 50;

        text = GamePanel.switchLanguage("Armor","Páncélzat", "Francia");
        g2.drawString(text, x, y);
        g2.drawString("" + entity.Player.armor, x + 165, y);
        y += 50;

        text = GamePanel.switchLanguage("Speed","Sebesség", "Francia");
        g2.drawString(text, x, y);
        g2.drawString("" + gp.player.speed, x + 165, y);
        y += 50;

        text = GamePanel.switchLanguage("Experience","Tapasztalat", "Francia");
        g2.drawString(text, x, y);
        g2.drawString("" + entity.Player.exp, x + 165, y);
        y += 50;

        text = GamePanel.switchLanguage("Next Level","Köv. szint", "Francia");
        g2.drawString(text, x, y);
        g2.drawString("" + entity.Player.nextLevel, x + 165, y);
        y += 50;

        text = GamePanel.switchLanguage("Mathcoin","Matérme", "Francia");
        g2.drawString(text, x, y);
        g2.drawString("" + entity.Player.coin, x + 165, y);
        y += 60;

        text = GamePanel.switchLanguage("Weapon","Fegyver", "Francia");
        g2.drawString(text, x, y);
        g2.drawImage(swordImageUp, x + 136, y - 40, 3 * gp.tileSize / 2, 3 * gp.tileSize / 2, null);

        x = 9*gp.tileSize;
        y = 3*gp.tileSize/4;
        width = gp.tileSize*6;
        height = gp.tileSize*6;
        drawSubWindow(x, y, width, height);

        g2.drawRoundRect(x+8+3 + gp.keyH.i*(gp.tileSize-3), y+8+3 + gp.keyH.j*(gp.tileSize-3),
                gp.tileSize-6, gp.tileSize-6, 20, 20);

        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 6; j++) {
                if(gp.player.items[i+j*6] != null) {
                    g2.drawImage(gp.player.items[i+j*6].image, x+8+3 + i*(gp.tileSize-3),
                            y+8+3 + j*(gp.tileSize-3), gp.tileSize-6, gp.tileSize-6, null);
                }
            }
        }


        x = 9*gp.tileSize;
        y = 3*gp.tileSize/4+height+gp.tileSize/2;
        width = gp.tileSize*6;
        height = gp.tileSize*4;
        drawSubWindow(x, y, width, height);
        if(gp.player.items[gp.keyH.i+gp.keyH.j*6] != null) {
            for(String line : gp.player.items[gp.keyH.i+gp.keyH.j*6].description.split("\n")) {
                g2.drawString(line, x+10, y+35);
                y += 40;
            }
//            g2.drawString(gp.player.items[gp.keyH.i+gp.keyH.j*6].description, x+10, y+35);
        }
    }
    public void drawMenuState() {
        // WINDOW
        int x = 4*gp.tileSize;
        int y = 2*gp.tileSize;
        int width = gp.screenWith-8*gp.tileSize;
        int height = gp.screenHeight - 4*gp.tileSize;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        x += 2*gp.tileSize;
        y += gp.tileSize;

        g2.setStroke(new BasicStroke(3));
        String text = "";

        text = GamePanel.switchLanguage("Sound","Hang", "Francia");
        g2.drawString(text, x, y);
        g2.drawRect(x + 100, y-24, 150, 30);
        g2.fillRect(x + 100, y-24, 30*GamePanel.se.volumeScale, 30);
        if(commandNum == 0) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }
        y += 50;

        text = GamePanel.switchLanguage("Music","Zene", "Francia");
        g2.drawString(text, x, y);
        g2.drawRect(x + 100, y-24, 150, 30);
        g2.fillRect(x + 100, y-24, 30*GamePanel.music.volumeScale, 30);
        if(commandNum == 1) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }
        y += 50;

        text = GamePanel.switchLanguage("Full screen","Teljes képernyő", "Francia");
        g2.drawString(text, x, y);
        g2.drawRect(x + 220, y-24, 30, 30);
        if(commandNum == 2) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }
        y += 50;

        text = GamePanel.switchLanguage("Control","Irányítás", "Francia");
        g2.drawString(text, x, y);
        if(commandNum == 3) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }
        y += 75;

        text = GamePanel.switchLanguage("End game","Kilépés a játékból", "Francia");
        g2.drawString(text, x, y);
        if(commandNum == 4) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }
        y += 80;

        text = GamePanel.switchLanguage("Back","Vissza", "Francia");
        g2.drawString(text, x, y);
        if(commandNum == 5) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }
    }
    public void drawControlState() {
        // WINDOW
        int x = 4*gp.tileSize;
        int y = 2*gp.tileSize;
        int width = gp.screenWith-8*gp.tileSize;
        int height = gp.screenHeight - 4*gp.tileSize;
        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 28F));
        x += 2*gp.tileSize;
        y += gp.tileSize;

        String text = "";

        text = GamePanel.switchLanguage("Moving: ASDW, <-->..",
                "Mozgás: ASDW, nyilak", "Francia");
        g2.drawString(text, x, y);
        y += 50;

        text = GamePanel.switchLanguage("Speaking: E", "Beszéd: E", "Francia");
        g2.drawString(text, x, y);
        y += 50;

        text = GamePanel.switchLanguage("Status: C", "Státusz: C", "Francia");
        g2.drawString(text, x, y);
        y += 50;

        text = GamePanel.switchLanguage("Pause: P", "Játék megállítása: P", "Francia");
        g2.drawString(text, x, y);
        y += 50;

        text = GamePanel.switchLanguage("Menu: ESC", "Menü: ESC", "Francia");
        g2.drawString(text, x, y);
        y += 50;

        text = GamePanel.switchLanguage("Enter: ENTER, SPACE", "Enter: ENTER, SPACE", "Francia");
        g2.drawString(text, x, y);
        y += 60;

        text = GamePanel.switchLanguage("Back", "Vissza", "Francia");
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }
    }


    public void drawVictoryScreen() {

        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 75F));
        String text = "";

        text = GamePanel.switchLanguage("VICTORY!", "GYŐZELEM!", "VICTOIRE!");
        int x = getXforCenteredText(text);
        int y = gp.tileSize*3;

        // SHADOW
        g2.setColor(Color.black);
        g2.drawString(text, x+3, y+3);
        // MAIN COLOR
        g2.setColor(new Color(165, 124, 0));
        g2.drawString(text, x, y);

        // CHARACTER IMAGE
        x = gp.screenWith/2;
        y += gp.tileSize*2;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);

        // PRINCESS IMAGE
        x -= 2*gp.tileSize-10;
        g2.drawImage(princessImage, x, y, gp.tileSize*2, gp.tileSize*2, null);
    }
    public void drawResourcesCodeScreen() {

        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWith, gp.screenHeight);

        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60F));
        String text = "";
        text = GamePanel.switchLanguage("The game is based on", "A játék alapja",
                "Les jeux se fondent sur");
        int x = getXforCenteredText(text);
        int y = gp.tileSize*3;

        // SHADOW
        g2.setColor(Color.black);
        g2.drawString(text, x+3, y+3);
        // MAIN COLOR
        g2.setColor(new Color(165, 124, 0));
        g2.drawString(text, x, y);

        // CHARACTER IMAGE
        x = gp.screenWith/2 - gp.tileSize;
        y += gp.tileSize*2;
        g2.drawImage(ryiSnowImage, x, y, gp.tileSize*2, gp.tileSize*2, null);

        // RESOURCES
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));

        text = "RyiSnow - How to Make a 2D Game";
        x = getXforCenteredText(text);
        y += gp.tileSize*3.5;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        text = "in Java (Youtube tutorial)";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
    }
    public void drawResourcesSoundScreen() {

        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWith, gp.screenHeight);

        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 75F));
        String text = "";
        text = GamePanel.switchLanguage("Music & Audio", "Zene és Hang", "Musique et Son");
        int x = getXforCenteredText(text);
        int y = gp.tileSize*3;

        // SHADOW
        g2.setColor(Color.black);
        g2.drawString(text, x+3, y+3);
        // MAIN COLOR
        g2.setColor(new Color(165, 124, 0));
        g2.drawString(text, x, y);

        // RESOURCES
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));

        text = "The Britons - Kevin MacLeod";
        x = getXforCenteredText(text);
        y += gp.tileSize*3.5;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        text = "Honor and Sword - Zakhar Valaha";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        text = "Sound effects - Pixabay";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
    }
    public void drawResourcesPictureScreen() {

        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWith, gp.screenHeight);

        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 75F));
        String text = "";
        text = GamePanel.switchLanguage("Picture resources", "Kép források", "Sources des Images");
        int x = getXforCenteredText(text);
        int y = gp.tileSize*3;

        // SHADOW
        g2.setColor(Color.black);
        g2.drawString(text, x+3, y+3);
        // MAIN COLOR
        g2.setColor(new Color(165, 124, 0));
        g2.drawString(text, x, y);

        // RESOURCES
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));

        text = GamePanel.switchLanguage("Devil", "Ördög", "Diable");
        text += " - OpenClipart-Vectors (pixabay.com)";
        x = getXforCenteredText(text);
        y += gp.tileSize*1.75;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        text = GamePanel.switchLanguage("Forest (Background)", "Erdő (Háttér)", "Fôret (Arrière-plan)");
        text += " - jwvein (pixabay.com)";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        text = GamePanel.switchLanguage("Door", "Ajtó", "Porte");
        text += " - sipa (pixabay.com)";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        text = GamePanel.switchLanguage("Knight", "Lovag", "Chevalier");
        text += " - Janson_G (pixabay.com)";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        text = GamePanel.switchLanguage("Orc", "Ork", "Orc");
        text += " - tunechick83 (pixabay.com)";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        text = GamePanel.switchLanguage("Wolfman", "Farkasember", "Loup-garou");
        text += " - Viergacht (pixabay.com)";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        text = GamePanel.switchLanguage("Icon", "Ikon", "Icône");
        text += " - Hodie Kun (icon-icons.com)";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
    }
    public void drawResourcesTranslationsScreen() {

        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWith, gp.screenHeight);

        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60F));
        String text = "";
        text = GamePanel.switchLanguage("Language translations", "Fordítások",
                "Traduction des languages");
        int x = getXforCenteredText(text);
        int y = gp.tileSize*3;

        // SHADOW
        g2.setColor(Color.black);
        g2.drawString(text, x+3, y+3);
        // MAIN COLOR
        g2.setColor(new Color(165, 124, 0));
        g2.drawString(text, x, y);

        // RESOURCES
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 45F));

        text = "Balogh Petra";
        x = getXforCenteredText(text);
        y += gp.tileSize*2.5;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
    }
    public void drawKohi() {

        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWith, gp.screenHeight);

        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 150F));
        String text = "Kohi";
        int x = getXforCenteredText(text);
        int y = gp.tileSize*7;

        // SHADOW
        g2.setColor(Color.black);
        g2.drawString(text, x+3, y+3);
        // MAIN COLOR
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }
    public void drawSubWindowBlack(int x, int y, int width, int height) {

        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(0, 0, 0);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
    }

    public void drawSubWindow(int x, int y, int width, int height, String text) {

        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+37, width-10, height-10, 25, 25);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20F));
        g2.drawString(text, x+18, y+64);
    }

    public int getXforCenteredText(String text) {

        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        return gp.screenWith/2 - length/2;
    }

}
