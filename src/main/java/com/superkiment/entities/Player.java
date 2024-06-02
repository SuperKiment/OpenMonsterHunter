package com.superkiment.entities;

import processing.core.PVector;
import processing.data.JSONObject;

public class Player extends Entity {

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
}
