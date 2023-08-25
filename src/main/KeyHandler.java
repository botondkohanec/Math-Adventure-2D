package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean speakPressed = false;
    public boolean checkDrawTime = false;

    public int i = 0, j = 0;


    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {/*defined only because of mandatory*/}

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if( GamePanel.gameState == GamePanel.GameState.LANGUAGE_STATE   ||
            GamePanel.gameState == GamePanel.GameState.TITLE_STATE      ||
            GamePanel.gameState == GamePanel.GameState.DIFFICULTY_STATE ||
            GamePanel.gameState == GamePanel.GameState.GAME_OVER_STATE   ||
            GamePanel.gameState == GamePanel.GameState.STATUS_STATE     ||
            GamePanel.gameState == GamePanel.GameState.MENU_STATE       ||
            GamePanel.gameState == GamePanel.GameState.CONTROL_STATE) {
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                gp.ui.enterPressed = true;
            }
        }

        // LANGUAGE STATE
        if(GamePanel.gameState == GamePanel.GameState.LANGUAGE_STATE) {

            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                GamePanel.playSE(20);
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0) gp.ui.commandNum = 2;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                GamePanel.playSE(20);
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 2) gp.ui.commandNum = 0;
            }
        }

        // TITLE STATE
        else if(GamePanel.gameState == GamePanel.GameState.TITLE_STATE) {

            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                GamePanel.playSE(20);
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0) gp.ui.commandNum = 2;
                if(gp.ui.commandNum > 2) gp.ui.commandNum = 0;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                GamePanel.playSE(20);
                gp.ui.commandNum++;
                if(gp.ui.commandNum < 0) gp.ui.commandNum = 2;
                if(gp.ui.commandNum > 2) gp.ui.commandNum = 0;
            }
        }

        // DIFFICULTY STATE
        else if(GamePanel.gameState == GamePanel.GameState.DIFFICULTY_STATE) {

            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                GamePanel.playSE(20);
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0) gp.ui.commandNum = 3;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                GamePanel.playSE(20);
                gp.ui.commandNum++;
                if(gp.ui.commandNum > 3) gp.ui.commandNum = 0;
            }
        }

        // PLAY STATE
        else if(GamePanel.gameState == GamePanel.GameState.PLAY_STATE) {

            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP)    upPressed = true;
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN)  downPressed = true;
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT)  leftPressed = true;
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = true;

            if(code == KeyEvent.VK_E) {
                speakPressed = true;
            }

            if(code == KeyEvent.VK_P) {
                GamePanel.gameState = GamePanel.GameState.PAUSE_STATE;
            }

            if(code == KeyEvent.VK_C) {
                GamePanel.gameState = GamePanel.GameState.STATUS_STATE;
            }

            if(code == KeyEvent.VK_ESCAPE) {
                GamePanel.gameState = GamePanel.GameState.MENU_STATE;
            }


            // DEBUG
            if (code == KeyEvent.VK_T) {

                if (!checkDrawTime) checkDrawTime = true;
                else if (checkDrawTime) checkDrawTime = false;
            }
        }

        // PAUSE STATE
        else if(GamePanel.gameState == GamePanel.GameState.PAUSE_STATE) {
            if (code == KeyEvent.VK_P) {
                GamePanel.gameState = GamePanel.GameState.PLAY_STATE;
            }
        }

        // DIALOGUE STATE
        else if(GamePanel.gameState == GamePanel.GameState.DIALOGUE_STATE) {
            if (code == KeyEvent.VK_ENTER) {GamePanel.gameState = GamePanel.GameState.PLAY_STATE;}
        }

        // COMBAT STATE
        else if(GamePanel.gameState == GamePanel.GameState.COMBAT_STATE) {
                if (code == KeyEvent.VK_ESCAPE) {System.exit(0);}
            }

        // GAME OVER STATE
        else if(GamePanel.gameState == GamePanel.GameState.GAME_OVER_STATE) {

            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {

                gp.ui.commandNum--;
                if (gp.ui.commandNum < 0) gp.ui.commandNum = 1;
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {

                gp.ui.commandNum++;
                if (gp.ui.commandNum > 1) {gp.ui.commandNum = 0;}
            }
        }

        // STATUS STATE
        else if(GamePanel.gameState == GamePanel.GameState.STATUS_STATE) {

            if (code == KeyEvent.VK_C) {
                GamePanel.gameState = GamePanel.GameState.PLAY_STATE;
            }
            if ( (code == KeyEvent.VK_W || code == KeyEvent.VK_UP)    && j > 0) {
                GamePanel.playSE(20);
                j--;
            }
            if ( (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN)  && j < 5) {
                GamePanel.playSE(20);
                j++;
            }
            if ( (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT)  && i > 0) {
                GamePanel.playSE(20);
                i--;
            }
            if ( (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) && i < 5) {
                GamePanel.playSE(20);
                i++;
            }
        }

        // MENU STATE
        else if(GamePanel.gameState == GamePanel.GameState.MENU_STATE) {

            if (code == KeyEvent.VK_ESCAPE) {
                GamePanel.gameState = GamePanel.GameState.PLAY_STATE;
                gp.ui.commandNum = 0;
            }
            if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) {
                gp.ui.commandNum--;
                if(gp.ui.commandNum < 0) gp.ui.commandNum = 5;
                if(gp.ui.commandNum > 5) gp.ui.commandNum = 0;
                GamePanel.playSE(20);
            }
            if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) {
                gp.ui.commandNum++;
                if(gp.ui.commandNum < 0) gp.ui.commandNum = 5;
                if(gp.ui.commandNum > 5) gp.ui.commandNum = 0;
                GamePanel.playSE(20);
            }
            if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) {
                if(gp.ui.commandNum == 0 || gp.ui.commandNum == 1) {
                    GamePanel.playSE(20);
                    if(gp.ui.commandNum == 0 && GamePanel.se.volumeScale > 0) {
                        GamePanel.se.volumeScale--;
                    }
                    if(gp.ui.commandNum == 1 && GamePanel.music.volumeScale > 0) {
                        GamePanel.music.volumeScale--;
                        GamePanel.music.chechVolume();
                    }
                }
            }
            if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) {
                if(gp.ui.commandNum == 0 || gp.ui.commandNum == 1) {
                    GamePanel.playSE(20);
                    if(gp.ui.commandNum == 0 && GamePanel.se.volumeScale < 5) {
                        GamePanel.se.volumeScale++;
                    }
                    if(gp.ui.commandNum == 1 && GamePanel.music.volumeScale < 5) {
                        GamePanel.music.volumeScale++;
                        GamePanel.music.chechVolume();
                    }
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        if( GamePanel.gameState == GamePanel.GameState.LANGUAGE_STATE   ||
            GamePanel.gameState == GamePanel.GameState.TITLE_STATE      ||
            GamePanel.gameState == GamePanel.GameState.DIFFICULTY_STATE ||
            GamePanel.gameState == GamePanel.GameState.GAME_OVER_STATE   ||
            GamePanel.gameState == GamePanel.GameState.STATUS_STATE     ||
            GamePanel.gameState == GamePanel.GameState.CONTROL_STATE) {
            if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {

                gp.ui.enterPressed = false;
                gp.ui.commandNum = 0;
            }
        }


        if(GamePanel.gameState == GamePanel.GameState.PLAY_STATE) {
            gp.ui.enterPressed = false;
            gp.ui.commandNum = 0;
        }



        // PLAY STATE
        if (code == KeyEvent.VK_W || code == KeyEvent.VK_UP) upPressed = false;
        if (code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN) downPressed = false;
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_LEFT) leftPressed = false;
        if (code == KeyEvent.VK_D || code == KeyEvent.VK_RIGHT) rightPressed = false;

        if (code == KeyEvent.VK_E) speakPressed = false;
    }

}
