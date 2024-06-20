package com.superkiment.entities;

import com.superkiment.entities.logic.Entity;
import com.superkiment.entities.logic.Interactable;
import com.superkiment.entities.logic.InteractionManager;

import processing.core.PVector;
import processing.data.JSONObject;

public class Player extends Entity implements Interactable {

    /**
     * The Player name, given on the first connexion
     */
    public String name = "NoName";

    /**
     * 
     * @param name the player's name
     * @param p    the world position
     */
    public Player(String name, PVector p) {
        super();
        pos = new PVector(p.x, p.y);
        dir = new PVector(0, 0);
        this.name = name;
    }

    @Override
    public void Update() {
        super.Update();

        pos.add(PVector.mult(dir, speed));

        // Get the closest entitiy TODO: OPTIMIZE WTF
        Entity closestEntity = null;
        float distance = 999999;
        // System.out.println("dist");
        for (Entity e : this.entityManager.getEntities()) {
            if (e == this)
                continue;

            float dist = PVector.sub(pos, e.pos).mag();
            if (dist < distance) {
                closestEntity = e;
                distance = dist;
            }
        }

        if (closestEntity != null) {
            // System.out.println(closestEntity.getClassName());
            interactionManager.trySetInteractable(closestEntity);
        }
    }

    @Override
    public JSONObject getJSON() {
        JSONObject json = super.getJSON();

        json.put("name", name);

        return json;
    }

    @Override
    public void UpdateFromJSON(JSONObject json) {
        try {
            pos.x = json.getFloat("pos.x");
            pos.y = json.getFloat("pos.y");
            dir.x = json.getFloat("dir.x");
            dir.y = json.getFloat("dir.y");
            speed = json.getFloat("speed");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Send a command to the Player to move or other. Do not forget keyReleased
     * 
     * @param key the pressed key
     */
    public void keyPressed(char key) {
        if (key == 'd')
            dir.x = 1;
        if (key == 'q')
            dir.x = -1;
        if (key == 's')
            dir.y = 1;
        if (key == 'z')
            dir.y = -1;

        dir.setMag(1);
    }

    /**
     * Send a command to the Player to stop moving or other.
     * 
     * @param key the pressed key
     */
    public void keyReleased(char key) {
        if (key == 'd')
            dir.x = 0;
        if (key == 'q')
            dir.x = 0;
        if (key == 's')
            dir.y = 0;
        if (key == 'z')
            dir.y = 0;

        dir.setMag(1);
    }

    @Override
    public InteractionManager getInteractionManager() {
        return this.interactionManager;
    }

    @Override
    public PVector getPos() {
        return this.pos;
    }
}
