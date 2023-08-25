package entity;

import main.GamePanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public class NPC_King extends Entity{


    public NPC_King(GamePanel gp) {

        super(gp);
        direction = "down";
        getImage();
        setDialogue();
    }

    @Override
    public void getImage() {

        down1 = setUp("/npc/king");
    }

    public void setDialogue() {

        String text1 = "", text2 = "";
        text1 = GamePanel.switchLanguage("""
                    My daughter has been kidnapped.\nPlease, bring her back, I will give her
                    and the half of my kingdom to you.
                    """,
                """
                    Elrabolták a leányomat! Kérlek hozd\nvissza Őt, és akkor odaadom neked
                    feleségül, s vele fele királyságomat.
                    """,
                """
                    Ma fille est enlevée. Récupéres-la\ns'il te plaît, je vais te la donner et
                    la moitié de mon royaume.
                    """
                );
        dialogues[0] = text1;
        text2 = GamePanel.switchLanguage("Rescue my daughter, please!",
                "Kérlek szabadítsd ki a leányomat!",
                "Libères ma fille, s'il te plaît!");
        dialogues[1] = text2;
    }

    @Override
    public void update() {
        // Standing still
    }

    @Override
    public void draw(Graphics2D g2) {

        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        BufferedImage image = down1;
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

    @Override
    public void speak() {

        if(dialogues[dialogueIndex] == null) dialogueIndex = 0;

        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;
    }

}
