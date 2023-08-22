package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.ArrayList;


public class Sound {

    Clip clip;
    URL[] soundURL = new URL[30];
    public ArrayList<Clip> openedClips = new ArrayList<>();


    public Sound() {

        soundURL[0] = getClass().getResource("/sound/music.wav");
        soundURL[1] = getClass().getResource("/sound/key.wav");
        soundURL[2] = getClass().getResource("/sound/gameover_new.wav");
        soundURL[3] = getClass().getResource("/sound/miss.wav");
        soundURL[4] = getClass().getResource("/sound/medieval_fanfare.wav");
        soundURL[5] = getClass().getResource("/sound/ready.wav");
        soundURL[6] = getClass().getResource("/sound/orcBlade.wav");
        soundURL[7] = getClass().getResource("/sound/door.wav");
        soundURL[8] = getClass().getResource("/sound/slash.wav");
        soundURL[9] = getClass().getResource("/sound/defeat.wav");
        soundURL[10] = getClass().getResource("/sound/apple.wav");
        soundURL[11] = getClass().getResource("/sound/boom.wav");
        soundURL[12] = getClass().getResource("/sound/wolfman_attack.wav");
        soundURL[13] = getClass().getResource("/sound/deadwolf.wav");
        soundURL[14] = getClass().getResource("/sound/laugh.wav");
        soundURL[15] = getClass().getResource("/sound/bossmusic.wav");
        soundURL[16] = getClass().getResource("/sound/leather.wav");
        soundURL[17] = getClass().getResource("/sound/unlock.wav");
        soundURL[18] = getClass().getResource("/sound/g_door.wav");
        soundURL[19] = getClass().getResource("/sound/math_coin.wav");
        soundURL[20] = getClass().getResource("/sound/switch.wav");
    }

    public void setFile(int i) {

        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            openedClips.add(clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {clip.start();}

    public void loop() {clip.loop(Clip.LOOP_CONTINUOUSLY);}

    public void stop() {clip.stop();}

}
