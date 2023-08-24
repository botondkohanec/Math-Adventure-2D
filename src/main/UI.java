package main;

import entity.NPC_Princess;
import object.OBJ_Heart;
import object.OBJ_Key;
import tile.TileManager;
import java.awt.*;
import java.awt.image.BufferedImage;
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
                if(gp.ui.commandNum == 1) GamePanel.gameState = GamePanel.GameState.LANGUAGE_STATE;
                if(gp.ui.commandNum == 2) System.exit(0);
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

        // RESOURCES STATE
        if(GamePanel.gameState == GamePanel.GameState.RESOURCES_STATE) {

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
        switch (GamePanel.language) {

            case ENG: text = "Math Adventure 2D";
                break;
            case HUN: text = "Matek Kaland 2D";
                break;
            case FR: text = "Aventures de Maths en 2D";
                break;
            default: text = "Math Adventure 2D";
                break;
        }
        int x = getXforCenteredText(text);
        int y = gp.tileSize*3;

        // SHADOW
        g2.setColor(Color.black);
        g2.drawString(text, x+3, y+3);
        // MAIN COLOR
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);

        // CHARACTER IMAGE
        x = gp.screenWith/2 - gp.tileSize;
        y += gp.tileSize*2;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);

        // MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));

        switch (GamePanel.language) {

            case ENG: text = "NEW GAME";
                break;
            case HUN: text = "ÚJ JÁTÉK";
                break;
            case FR: text = "NOUVEAU JEU";
                break;
            default: text = "NEW GAME";
                break;
        }
        x = getXforCenteredText(text);
        y += gp.tileSize*3.5;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }

        switch (GamePanel.language) {

            case ENG: text = "LANGUAGE";
                break;
            case HUN: text = "NYELV";
                break;
            case FR: text = "FRANCIA";
                break;
            default: text = "LANGUAGE";
                break;
        }
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }

        switch (GamePanel.language) {

            case ENG: text = "QUIT";
                break;
            case HUN: text = "KILÉPÉS";
                break;
            case FR: text = "QUITTER";
                break;
            default: text = "QUIT";
                break;
        }
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);
        if(commandNum == 2) {
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

        switch (GamePanel.language) {

            case ENG: text = "Easy";
                break;
            case HUN: text = "Könnyű";
                break;
            case FR: text = "Facile";
                break;
            default: text = "Easy";
                break;
        }
        x = getXforCenteredText(text);
        y = gp.tileSize*3;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }

        switch (GamePanel.language) {

            case ENG: text = "Medium";
                break;
            case HUN: text = "Közepes";
                break;
            case FR: text = "Moyen";
                break;
            default: text = "Medium";
                break;
        }
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }

        switch (GamePanel.language) {

            case ENG: text = "Hard";
                break;
            case HUN: text = "Nehéz";
                break;
            case FR: text = "Difficile";
                break;
            default: text = "Hard";
                break;
        }
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(new Color(28, 53, 45));
        g2.drawString(text, x, y);
        if(commandNum == 2) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }

        switch (GamePanel.language) {
            case ENG: text = "Mathematician";
                break;
            case HUN: text = "Matematikus";
                break;
            case FR: text = "Mathématicien";
                break;
            default: text = "Mathematician";
                break;
        }
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
        switch (GamePanel.language) {

            case ENG: text = "PAUSE";
                break;
            case HUN: text = "SZÜNET";
                break;
            case FR: text = "PAUSE";
                break;
            default: text = "PAUSED";
                break;
        }
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text, x, y);
    }
    public void drawCombatScreen() {

        g2.setColor(Color.red);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80F));
        String text = "";
        switch (GamePanel.language) {

            case ENG: text = "COMBAT";
                break;
            case HUN: text = "HARC";
                break;
            case FR: text = "COMBAT";
                break;
            default: text = "COMBAT";
                break;
        }
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

        switch (GamePanel.language) {
            case ENG: text = "GAME OVER";
                break;
            case HUN: text = "VÉGE A JÁTÉKNAK";
                break;
            case FR: text = "FIN DE JEU";
                break;
            default: text = "GAME OVER";
                break;
        }
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

        switch (GamePanel.language) {

            case ENG: text = "Retry";
                break;
            case HUN: text = "Újra";
                break;
            case FR: text = "De Nouveau";
                break;
            default: text = "Retry";
                break;
        }
        x = getXforCenteredText(text);
        y += gp.tileSize*4;
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
        }

        // Back to the title screen
        switch (GamePanel.language) {

            case ENG: text = "Quit";
                break;
            case HUN: text = "Kilép";
                break;
            case FR: text = "Quitter";
                break;
            default: text = "Quit";
                break;
        }
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

        if(GamePanel.language == GamePanel.Language.HUN) {

            g2.drawString("Szint", x, y);
            g2.drawString("" + entity.Player.level, x + 165, y);
            y += 50;

            g2.drawString("HP", x, y);
            g2.drawString("" + entity.Player.hp+"/"+ entity.Player.maxHP, x + 165, y);
            y += 50;

            g2.drawString("Sebzés", x, y);
            g2.drawString("46-55", x + 165, y);
            y += 50;

            g2.drawString("Páncélzat", x, y);
            g2.drawString("" + entity.Player.armor, x + 165, y);
            y += 50;

            g2.drawString("Sebesség", x, y);
            g2.drawString("" + gp.player.speed, x + 165, y);
            y += 50;

            g2.drawString("Tapasztalat", x, y);
            g2.drawString("" + entity.Player.exp, x + 165, y);
            y += 50;

            g2.drawString("Köv. szint", x, y);
            g2.drawString("" + entity.Player.nextLevel, x + 165, y);
            y += 50;

            g2.drawString("Matérme", x, y);
            g2.drawString("" + entity.Player.coin, x + 165, y);
            y += 60;

            g2.drawString("Fegyver", x, y);
            g2.drawImage(swordImageUp, x + 136, y - 40, 3 * gp.tileSize / 2, 3 * gp.tileSize / 2, null);
        }
        else if(GamePanel.language == GamePanel.Language.FR) {

            g2.drawString("Francia", x, y);
            g2.drawString("" + entity.Player.level, x + 165, y);
            y += 50;

            g2.drawString("HP", x, y);
            g2.drawString("" + entity.Player.hp+"/"+ entity.Player.maxHP, x + 165, y);
            y += 50;

            g2.drawString("Francia", x, y);
            g2.drawString("46-55", x + 165, y);
            y += 50;

            g2.drawString("Francia", x, y);
            g2.drawString("" + entity.Player.armor, x + 165, y);
            y += 50;

            g2.drawString("Francia", x, y);
            g2.drawString("" + gp.player.speed, x + 165, y);
            y += 50;

            g2.drawString("Francia", x, y);
            g2.drawString("" + entity.Player.exp, x + 165, y);
            y += 50;

            g2.drawString("Francia", x, y);
            g2.drawString("" + entity.Player.nextLevel, x + 165, y);
            y += 50;

            g2.drawString("Francia", x, y);
            g2.drawString("" + entity.Player.coin, x + 165, y);
            y += 60;

            g2.drawString("Francia", x, y);
            g2.drawImage(swordImageUp, x + 136, y - 40, 3 * gp.tileSize / 2, 3 * gp.tileSize / 2, null);
        }
        else {
            g2.drawString("Level", x, y);
            g2.drawString("" + entity.Player.level, x + 165, y);
            y += 50;

            g2.drawString("HP", x, y);
            g2.drawString("" + entity.Player.hp+"/"+ entity.Player.maxHP, x + 165, y);
            y += 50;

            g2.drawString("Attack", x, y);
            g2.drawString("46-55", x + 165, y);
            y += 50;

            g2.drawString("Armor", x, y);
            g2.drawString("" + entity.Player.armor, x + 165, y);
            y += 50;

            g2.drawString("Speed", x, y);
            g2.drawString("" + gp.player.speed, x + 165, y);
            y += 50;

            g2.drawString("Experience", x, y);
            g2.drawString("" + entity.Player.exp, x + 165, y);
            y += 50;

            g2.drawString("Next Level", x, y);
            g2.drawString("" + entity.Player.nextLevel, x + 165, y);
            y += 50;

            g2.drawString("Mathcoin", x, y);
            g2.drawString("" + entity.Player.coin, x + 165, y);
            y += 60;

            g2.drawString("Weapon", x, y);
            g2.drawImage(swordImageUp, x + 136, y - 40, 3 * gp.tileSize / 2, 3 * gp.tileSize / 2, null);
        }

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

        if(GamePanel.language == GamePanel.Language.HUN) {

            g2.drawString("Hang", x, y);
            if(commandNum == 0) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
            y += 50;

            g2.drawString("Zene", x, y);
            if(commandNum == 1) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
            y += 50;

            g2.drawString("Teljes képernyő", x, y);
            if(commandNum == 2) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
            y += 50;

            g2.drawString("Irányítás", x, y);
            if(commandNum == 3) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
            y += 75;

            g2.drawString("Kilépés a játékból", x, y);
            if(commandNum == 4) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
            y += 80;

            g2.drawString("Vissza", x, y);
            if(commandNum == 5) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
        }
        else if(GamePanel.language == GamePanel.Language.FR) {

            g2.drawString("Francia", x, y);
            if(commandNum == 0) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
            y += 50;

            g2.drawString("Francia", x, y);
            if(commandNum == 1) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
            y += 50;

            g2.drawString("Francia", x, y);
            if(commandNum == 2) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
            y += 50;

            g2.drawString("Francia", x, y);
            if(commandNum == 3) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
            y += 75;

            g2.drawString("Francia", x, y);
            if(commandNum == 4) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
            y += 80;

            g2.drawString("Francia", x, y);
            if(commandNum == 5) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
        }
        else {
            g2.drawString("Sound", x, y);
            if(commandNum == 0) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
            y += 50;

            g2.drawString("Music", x, y);
            if(commandNum == 1) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
            y += 50;

            g2.drawString("Full screen", x, y);
            if(commandNum == 2) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
            y += 50;

            g2.drawString("Control", x, y);
            if(commandNum == 3) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
            y += 75;

            g2.drawString("End game", x, y);
            if(commandNum == 4) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
            y += 80;

            g2.drawString("Back", x, y);
            if(commandNum == 5) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
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

        if(GamePanel.language == GamePanel.Language.HUN) {

            g2.drawString("Mozgás: ASDW, nyilak", x, y);
            y += 50;

            g2.drawString("Beszéd: E", x, y);
            y += 50;

            g2.drawString("Státusz: C", x, y);
            y += 50;

            g2.drawString("Játék megállítása: P", x, y);
            y += 50;

            g2.drawString("Menü: ESC", x, y);
            y += 50;

            g2.drawString("Enter: ENTER, SPACE", x, y);
            y += 60;

            g2.drawString("Vissza", x, y);
            if(commandNum == 0) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
        }
        else if(GamePanel.language == GamePanel.Language.FR) {

            g2.drawString("Francia: ASDW or <-->..", x, y);
            y += 50;

            g2.drawString("Francia: E", x, y);
            y += 50;

            g2.drawString("Francia: C", x, y);
            y += 50;

            g2.drawString("Francia: P", x, y);
            y += 50;

            g2.drawString("Francia: ESC", x, y);
            y += 50;

            g2.drawString("Francia: ENTER, SPACE", x, y);
            y += 60;

            g2.drawString("Francia", x, y);
            if(commandNum == 0) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
        }
        else {
            g2.drawString("Moving: ASDW, <-->..", x, y);
            y += 50;

            g2.drawString("Speaking: E", x, y);
            y += 50;

            g2.drawString("Status: C", x, y);
            y += 50;

            g2.drawString("Pause: P", x, y);
            y += 50;

            g2.drawString("Menu: ESC", x, y);
            y += 50;

            g2.drawString("Enter: ENTER, SPACE", x, y);
            y += 60;

            g2.drawString("Back", x, y);
            if(commandNum == 0) {
                g2.drawImage(swordImage, x-gp.tileSize-20, y-gp.tileSize+5, 60, 60, null);
            }
        }
    }


    public void drawVictoryScreen() {

        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 75F));
        String text = "";

        switch (GamePanel.language) {

            case ENG: text = "VICTORY!";
                break;
            case HUN: text = "GYŐZELEM!";
                break;
            case FR: text = "VICTOIRE!";
                break;
            default: text = "VICTORY!";
                break;
        }
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
        switch (GamePanel.language) {

            case ENG: text = "The game is based on";
                break;
            case HUN: text = "A játék alapja";
                break;
            case FR: text = "Les jeux se fondent sur";
                break;
            default: text = "The game is based on";
                break;
        }
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
        switch (GamePanel.language) {

            case ENG: text = "Music & Audio";
                break;
            case HUN: text = "Zene és Hang";
                break;
            case FR: text = "Musique et Son";
                break;
            default: text = "Music & Audio";
                break;
        }
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
        switch (GamePanel.language) {

            case ENG: text = "Picture resources";
                break;
            case HUN: text = "Kép források";
                break;
            case FR: text = "Sources des Images";
                break;
            default: text = "Picture resources";
                break;
        }
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

        switch (GamePanel.language) {

            case ENG: text = "Devil";
                break;
            case HUN: text = "Ördög";
                break;
            case FR: text = "Diable";
                break;
            default: text = "Devil";
                break;
        }
        text += " - OpenClipart-Vectors (pixabay.com)";
        x = getXforCenteredText(text);
        y += gp.tileSize*1.75;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        switch (GamePanel.language) {

            case ENG: text = "Forest (Background)";
                break;
            case HUN: text = "Erdő (Háttér)";
                break;
            case FR: text = "Fôret (Arrière-plan)";
                break;
            default: text = "Forest (Background)";
                break;
        }
        text += " - jwvein (pixabay.com)";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        switch (GamePanel.language) {

            case ENG: text = "Door";
                break;
            case HUN: text = "Ajtó";
                break;
            case FR: text = "Porte";
                break;
            default: text = "Door";
                break;
        }
        text += " - sipa (pixabay.com)";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        switch (GamePanel.language) {

            case ENG: text = "Knight";
                break;
            case HUN: text = "Lovag";
                break;
            case FR: text = "Chevalier";
                break;
            default: text = "Knight";
                break;
        }
        text += " - Janson_G (pixabay.com)";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        switch (GamePanel.language) {

            case ENG: text = "Orc";
                break;
            case HUN: text = "Ork";
                break;
            case FR: text = "Orc";
                break;
            default: text = "Orc";
                break;
        }
        text += " - tunechick83 (pixabay.com)";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        switch (GamePanel.language) {

            case ENG: text = "Wolfman";
                break;
            case HUN: text = "Farkasember";
                break;
            case FR: text = "Loup-garou";
                break;
            default: text = "Wolfman";
                break;
        }
        text += " - Viergacht (pixabay.com)";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        switch (GamePanel.language) {

            case ENG: text = "Icon";
                break;
            case HUN: text = "Ikon";
                break;
            case FR: text = "Icône";
                break;
            default: text = "Icon";
                break;
        }
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
        switch (GamePanel.language) {

            case ENG: text = "Language translations";
                break;
            case HUN: text = "Fordítások";
                break;
            case FR: text = "Traduction des languages";
                break;
            default: text = "Language translations";
                break;
        }
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
