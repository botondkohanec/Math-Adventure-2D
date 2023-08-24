package main;

import combat.Combat;
import entity.Entity;
import entity.NPC_Orc;
import entity.NPC_WolfMan;
import object.OBJ_Apple;
import object.OBJ_Key;

public class CollisionChecker {

    GamePanel gp;


    public CollisionChecker(GamePanel gp) {

        this.gp = gp;
    }

    public void checkTile(Entity entity) {

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX/gp.tileSize;
        int entityRightCol = entityRightWorldX/gp.tileSize;
        int entityTopRow = entityTopWorldY/gp.tileSize;
        int entityBottomRow = entityBottomWorldY/gp.tileSize;

        int tileNum1;
        int tileNum2;

        switch(entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            case "right":
                entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
            default: break;
        }
    }

    public int checkObject(Entity entity, boolean player) {

        int index = 999;

        for(int i = 0; i < gp.obj.length; i++) {

            if(gp.obj[i] != null) {

                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                // Get the object's solid area position
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if(entity.solidArea.intersects(gp.obj[i].solidArea)) {

                            if(gp.obj[i].collision) { entity.collisionOn = true; }
                            if(player) { index = i; }

                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if(entity.solidArea.intersects(gp.obj[i].solidArea)) {

                            if(gp.obj[i].collision) { entity.collisionOn = true; }
                            if(player) { index = i; }

                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if(entity.solidArea.intersects(gp.obj[i].solidArea)) {

                            if(gp.obj[i].collision) { entity.collisionOn = true; }
                            if(player) { index = i; }

                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if(entity.solidArea.intersects(gp.obj[i].solidArea)) {

                            if(gp.obj[i].collision) { entity.collisionOn = true; }
                            if(player) { index = i; }

                        }
                        break;

                    default: break;
                }


                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
            }
        }

        return index;
    }

    // NPC OR MONSTER
    public int checkEntity(Entity entity, Entity[] target) {

        int index = 999;

        for(int i = 0; i < target.length; i++) {

            if(target[i] != null) {

                // Get entity's solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                // Get the target's solid area position
                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                switch (entity.direction) {

                    case "up":
                        entity.solidArea.y -= entity.speed;
                        if(entity.solidArea.intersects(target[i].solidArea)) {

                            if(startCombat(target, i)) {
                                break;
                            }

                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        if(entity.solidArea.intersects(target[i].solidArea)) {

                            if(startCombat(target, i)) {
                                break;
                            }

                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if(entity.solidArea.intersects(target[i].solidArea)) {

                            if(startCombat(target, i)) {
                                break;
                            }

                            entity.collisionOn = true;
                            index = i;
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if(entity.solidArea.intersects(target[i].solidArea)) {

                            if(startCombat(target, i)) {
                                break;
                            }

                            entity.collisionOn = true;
                            index = i;
                        }
                        break;

                    default: break;
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                if(target[i] != null) {

                    target[i].solidArea.x = target[i].solidAreaDefaultX;
                    target[i].solidArea.y = target[i].solidAreaDefaultY;
                }
            }
        }

        return index;

    }

    public void checkPlayer(int index) {

        // Get entity's solid area position
        gp.npc[index].solidArea.x = gp.npc[index].worldX + gp.npc[index].solidArea.x;
        gp.npc[index].solidArea.y = gp.npc[index].worldY + gp.npc[index].solidArea.y;
        // Get the player's solid area position
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        switch (gp.npc[index].direction) {

            case "up":
                gp.npc[index].solidArea.y -= gp.npc[index].speed;
                if(gp.npc[index].solidArea.intersects(gp.player.solidArea)) {

                    if(startCombat(gp.npc, index)) {
                        break;
                    }

                    gp.npc[index].collisionOn = true;
                }
                break;
            case "down":
                gp.npc[index].solidArea.y += gp.npc[index].speed;
                if(gp.npc[index].solidArea.intersects(gp.player.solidArea)) {

                    if(startCombat(gp.npc, index)) {
                        break;
                    }

                    gp.npc[index].collisionOn = true;
                }
                break;
            case "left":
                gp.npc[index].solidArea.x -= gp.npc[index].speed;
                if(gp.npc[index].solidArea.intersects(gp.player.solidArea)) {

                    if(startCombat(gp.npc, index)) {
                        break;
                    }

                    gp.npc[index].collisionOn = true;
                }
                break;
            case "right":
                gp.npc[index].solidArea.x += gp.npc[index].speed;
                if(gp.npc[index].solidArea.intersects(gp.player.solidArea)) {

                    if(startCombat(gp.npc, index)) {
                        break;
                    }

                    gp.npc[index].collisionOn = true;
                }
                break;

            default: break;
        }

        if(gp.npc[index] != null) {

            gp.npc[index].solidArea.x = gp.npc[index].solidAreaDefaultX;
            gp.npc[index].solidArea.y = gp.npc[index].solidAreaDefaultY;
        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
    }

    public boolean startCombat(Entity[] target, int i) {

        if(target[i] instanceof NPC_Orc || target[i] instanceof NPC_WolfMan) {

            Combat.NPCcombatIndex = i;

            GamePanel.gameState = GamePanel.GameState.COMBAT_STATE;
            if(target[i] instanceof NPC_Orc) {

                GamePanel.combatNPC = GamePanel.CombatNPC.ORC;
                AssetSetter.setOrcNumber(AssetSetter.getOrcNumber() - 1);
            }
            else if(target[i] instanceof NPC_WolfMan) {

                GamePanel.combatNPC = GamePanel.CombatNPC.WOLF;
                AssetSetter.setWolfManNumber(AssetSetter.getWolfManNumber() - 1);
            }

            return true;
        }

        return false;
    }

}
