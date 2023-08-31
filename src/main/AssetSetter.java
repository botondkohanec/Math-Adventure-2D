package main;

import entity.*;
import object.*;

public final class AssetSetter {

    GamePanel gp;

    public static final String GEO = "Geometry";
    public static final String KEY = "Key";
    private static int orcNumber = 0;
    private static int wolfManNumber = 0;


    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public static int getOrcNumber() {return orcNumber;}

    public static int getWolfManNumber() {return wolfManNumber;}

    public static void setOrcNumber(int orcNumber) {AssetSetter.orcNumber = orcNumber;}

    public static void setWolfManNumber(int wolfManNumber) {AssetSetter.wolfManNumber = wolfManNumber;}

    public void setObject() {

        // 0, 1 - Boom_OBJs

        // 2 - ... Entities objects

        for(int i = 0; i < gp.obj.length; i++) gp.obj[i] = null;

        if(gp.mapNum == 1) {

            gp.obj[12] = new OBJ_Door(12, gp, KEY);
            gp.obj[12].worldX = 17 * gp.tileSize;
            gp.obj[12].worldY = 22 * gp.tileSize;

            gp.obj[13] = new OBJ_Bomb(13, gp);
            gp.obj[13].worldX = 29 * gp.tileSize;
            gp.obj[13].worldY = 21 * gp.tileSize;

            gp.obj[13] = new OBJ_Key(13, gp);
            gp.obj[13].worldX = 17 * gp.tileSize;
            gp.obj[13].worldY = 35 * gp.tileSize;

            gp.obj[14] = new OBJ_Door(14, gp, GEO);
            gp.obj[14].worldX = 17 * gp.tileSize;
            gp.obj[14].worldY = 26 * gp.tileSize;

            gp.obj[15] = new OBJ_Door(15, gp, KEY);
            gp.obj[15].worldX = 14 * gp.tileSize;
            gp.obj[15].worldY = 20 * gp.tileSize;

            gp.obj[16] = new OBJ_Apple(16, gp);
            gp.obj[16].worldX = 27 * gp.tileSize;
            gp.obj[16].worldY = 21 * gp.tileSize;

            gp.obj[17] = new OBJ_Bomb(17, gp);
            gp.obj[17].worldX = 22 * gp.tileSize;
            gp.obj[17].worldY = 23 * gp.tileSize;

            gp.obj[18] = new OBJ_Door(18, gp, KEY);
            gp.obj[18].worldX = 17 * gp.tileSize;
            gp.obj[18].worldY = 30 * gp.tileSize;
        }

        else if(gp.mapNum == 2) {

            gp.obj[42] = new OBJ_Door(42, gp, KEY);
            gp.obj[42].worldX = (45+9) * gp.tileSize;
            gp.obj[42].worldY = (27+5) * gp.tileSize;

            gp.obj[43] = new OBJ_Key(43, gp);
            gp.obj[43].worldX = (26+9) * gp.tileSize;
            gp.obj[43].worldY = (9+5) * gp.tileSize;

            gp.obj[44] = new OBJ_Door(44, gp, GEO);
            gp.obj[44].worldX = (16+9) * gp.tileSize;
            gp.obj[44].worldY = (28+5) * gp.tileSize;

            gp.obj[45] = new OBJ_Door(45, gp, KEY);
            gp.obj[45].worldX = (29+9) * gp.tileSize;
            gp.obj[45].worldY = (15+5) * gp.tileSize;

            gp.obj[46] = new OBJ_Apple(46, gp);
            gp.obj[46].worldX = (27+9) * gp.tileSize;
            gp.obj[46].worldY = (22+5) * gp.tileSize;

            gp.obj[47] = new OBJ_Key(47, gp);
            gp.obj[47].worldX = (47+9) * gp.tileSize;
            gp.obj[47].worldY = (7+5) * gp.tileSize;

            gp.obj[48] = new OBJ_Door(48, gp, KEY);
            gp.obj[48].worldX = (24+9) * gp.tileSize;
            gp.obj[48].worldY = (10+5) * gp.tileSize;

            gp.obj[49] = new OBJ_Bomb(49, gp);
            gp.obj[49].worldX = (17+9) * gp.tileSize;
            gp.obj[49].worldY = (24+5) * gp.tileSize;

            gp.obj[50] = new OBJ_GoldApple(50, gp);
            gp.obj[50].worldX = (41+9) * gp.tileSize;
            gp.obj[50].worldY = (26+5) * gp.tileSize;

            gp.obj[51] = new OBJ_Door(51, gp, KEY);
            gp.obj[51].worldX = (24+9) * gp.tileSize;
            gp.obj[51].worldY = (24+5) * gp.tileSize;

            gp.obj[52] = new OBJ_Door(52, gp, GEO);
            gp.obj[52].worldX = (8+9) * gp.tileSize;
            gp.obj[52].worldY = (24+5) * gp.tileSize;

            gp.obj[53] = new OBJ_Boots(53, gp);
            gp.obj[53].worldX = (2+9) * gp.tileSize;
            gp.obj[53].worldY = (4+5) * gp.tileSize;

            gp.obj[88] = new OBJ_Integral(88, gp);
            gp.obj[88].worldX = (30+9) * gp.tileSize;
            gp.obj[88].worldY = (9+5) * gp.tileSize;
        }
    }

    public void setNPC() {

        if(gp.mapNum == 1) {

            gp.npc[2] = new NPC_Boss(gp);
            gp.npc[2].index = 11;
            gp.npc[2].worldX = gp.tileSize*32;
            gp.npc[2].worldY = gp.tileSize*4;

            setNPCOrc(3, 17, 24);
            setNPCOrc(4, 32, 10);
            setNPCOrc(5, 22, 37);
            setNPCOrc(6, 22, 31);
            setNPCOrc(7, 22, 22);
            setNPCOrc(8, 22, 21);

            gp.npc[9] = new NPC_King(gp);
            gp.npc[9].index = 9;
            gp.npc[9].worldX = gp.tileSize*17;
            gp.npc[9].worldY = gp.tileSize*36;

            gp.npc[10] = new NPC_Princess(gp);
            gp.npc[10].index = 10;
            gp.npc[10].worldX = gp.tileSize*32;
            gp.npc[10].worldY = gp.tileSize*1;

            setNPCOrc(11, 14, 15);
        }

        else if(gp.mapNum == 2) {

            gp.npc[2] = new NPC_Boss(gp);
            gp.npc[2].index = 2;
            gp.npc[2].worldX = gp.tileSize*(44+9);
            gp.npc[2].worldY = gp.tileSize*(26+5);

            setNPCWolfMan(3,17+9, 25+5);
            if(GamePanel.difficulty != GamePanel.Difficulty.EASY) {

                setNPCWolfMan(4, 32+9, 10+5);
                setNPCWolfMan(5, 22+9, 11+5);
                setNPCOrc(6, 17+9, 25+5);
                setNPCOrc(7, 17+9, 25+5);
                setNPCOrc(10, 32+9, 10+5);
                setNPCOrc(11, 32+9, 10+5);
                setNPCOrc(14, 22+9, 11+5);
                setNPCOrc(15, 22+9, 11+5);
                setNPCOrc(18, 44+9, 26+5);
                setNPCOrc(19, 44+9, 26+5);
                setNPCOrc(24, 2+9, 4+5);
                setNPCOrc(25, 2+9, 4+5);
                setNPCOrc(30, 44+9, 23+5);
                setNPCOrc(31, 44+9, 23+5);
            }
            setNPCOrc(8, 17+9, 25+5);
            setNPCOrc(9, 17+9, 25+5);
            setNPCOrc(12, 32+9, 10+5);
            setNPCOrc(13, 32+9, 10+5);
            setNPCOrc(16, 22+9, 11+5);
            setNPCOrc(17, 22+9, 11+5);
            setNPCOrc(20, 44+9, 26+5);
            setNPCOrc(21, 44+9, 26+5);
            setNPCOrc(22, 44+9, 26+5);
            setNPCOrc(23, 44+9, 26+5);
            setNPCOrc(26, 2+9, 4+5);
            setNPCOrc(27, 2+9, 4+5);
            setNPCOrc(28, 22+9, 11+5);
            setNPCOrc(29, 22+9, 11+5);
            setNPCOrc(32, 44+9, 23+5);
            setNPCOrc(33, 44+9, 23+5);
            setNPCOrc(34, 44+9, 23+5);
            setNPCOrc(35, 44+9, 23+5);

            gp.npc[36] = new NPC_King(gp);
            gp.npc[36].index = 36;
            gp.npc[36].worldX = gp.tileSize*(28+9);
            gp.npc[36].worldY = gp.tileSize*(9+5);

            gp.npc[37] = new NPC_Princess(gp);
            gp.npc[37].index = 37;
            gp.npc[37].worldX = gp.tileSize*(48+9);
            gp.npc[37].worldY = gp.tileSize*(26+5);

            setNPCWolfMan(38, 14+9, 15+5);
            setNPCOrc(39, 14+9, 15+5);
            setNPCOrc(40, 14+9, 15+5);
            setNPCOrc(41, 14+9, 15+5);
        }
    }

    public void setNPCOrc(int arrayIndex, int worldX, int worldY) {

        // INITIALIZATION
        gp.npc[arrayIndex] = new NPC_Orc(gp);
        gp.npc[arrayIndex].index = arrayIndex;
        orcNumber ++;

        // POSITION
        gp.npc[arrayIndex].worldX = gp.tileSize * worldX;
        gp.npc[arrayIndex].worldY = gp.tileSize * worldY;
    }

    public void setNPCWolfMan(int arrayIndex, int worldX, int worldY) {

        // INITIALIZATION
        gp.npc[arrayIndex] = new NPC_WolfMan(gp);
        gp.npc[arrayIndex].index = arrayIndex;
        wolfManNumber ++;

        // POSITION
        gp.npc[arrayIndex].worldX = gp.tileSize * worldX;
        gp.npc[arrayIndex].worldY = gp.tileSize * worldY;
    }

}
