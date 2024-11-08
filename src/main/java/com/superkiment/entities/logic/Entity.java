package com.superkiment.entities.logic;

import com.superkiment.globals.Time;

import processing.core.PVector;
import processing.data.JSONObject;

import java.util.ArrayList;

/**
 * Base entity class for creating entities like Dog, Player, etc.
 * 
 * @author <a href="https://github/superkiment/">SuperKiment's GitHub</a>
 */
public class Entity {
    /**
     * Entity's base speed, used to calculate the magnitude of the velocity vector.
     */
    public float speed = 0.3f;

    /**
     * Vectors for position, direction and direction when nothing is touched
     */
    public PVector pos, dir, remanantDir;

    /**
     * Unique identifier for this entity in the world
     */
    public String ID = "";

    /**
     * List of hitboxes for this entity, passive and physics.
     */
    public ArrayList<Hitbox> hitboxes;

    /**
     * The manager where this entity is stored.
     */
    protected EntityManager entityManager;

    /**
     * The manager managing the entity's interactions. Used with the interface
     * InteractionManager
     */
    protected InteractionManager interactionManager;

    public SayingBox sayingBox;

    public Entity() {
        String characters = "azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBN1234567890&éèâêô@àù£";
        try {
            for (int i = 0; i < 20; i++) {
                int rand = (int) Math.floor(Math.random() * characters.length());
                char ch = characters.charAt(rand);

                ID += ch;
            }
        } catch (Exception e) {
            ID = "failed to generate";
        }

        pos = new PVector();
        dir = new PVector();
        remanantDir = new PVector();

        hitboxes = new ArrayList<Hitbox>();
        hitboxes.add(new Hitbox(this, new PVector(0, 0), 20));

        interactionManager = new InteractionManager(this);
        sayingBox = new SayingBox(this);
    }

    /**
     * Main method for updating the entity : moving, collision.
     */
    public void Update() {
        Deplacement();
        CheckCollision();
    }

    /**
     * Main method for moving the entity
     */
    private void Deplacement() {
        PVector dirTemp = dir.copy();
        dirTemp.setMag(speed * Time.moyenneTime);
        pos.add(dirTemp);

        if (dir.mag() != 0) {
            remanantDir.set(dir);
        }
    }

    /**
     * Main method for checking collisions
     */
    private void CheckCollision() {

        ArrayList<Entity> allEntities = entityManager.getEntities();
        for (int i = 0; i < allEntities.size(); i++) {
            Entity entity = allEntities.get(i);

            if (entity == this)
                continue;

            for (Hitbox hitboxOther : entity.hitboxes) {
                for (Hitbox hitboxThis : this.hitboxes) {

                    // On récup les positions calculées qu'une fois
                    com.superkiment.utils.Pair<Boolean, PVector[]> pair = hitboxThis.isCollisionWith(hitboxOther);

                    // Puis on teste
                    if (pair.getFirst()) {
                        // System.out.println("COLLISION " + this.pos + " / " + entity.pos);

                        // Et on lui donne els positions calculées
                        hitboxThis.doAction(entity, hitboxOther, pair.getSecond());
                    }
                }
            }
        }
    }

    /**
     * Get the entity's json information
     * 
     * @return the entity's json
     */
    public JSONObject getJSON() {
        JSONObject obj = new JSONObject();

        obj.setFloat(JSONFieldName.POSITION_X.getValue(), pos.x);
        obj.setFloat(JSONFieldName.POSITION_Y.getValue(), pos.y);
        obj.setFloat(JSONFieldName.DIRECTION_X.getValue(), remanantDir.x);
        obj.setFloat(JSONFieldName.DIRECTION_Y.getValue(), remanantDir.y);

        obj.setString(JSONFieldName.CLASS_NAME.getValue(), this.getClass().getName());

        obj.setString(JSONFieldName.TEXT_SAYING.getValue(), sayingBox.getSayingText());

        obj.setFloat(JSONFieldName.SPEED.getValue(), speed);

        obj.setString(JSONFieldName.ID.getValue(), ID);
        return obj;
    }

    public String getClassName() {
        System.out.println("Nom : " + this.getClass().getName().split("entities.")[1]);
        String[] tabl = this.getClass().getName().split("entities.");
        return tabl[1];
    }

    /**
     * Update the entity's information from a JSON
     * 
     * @param json
     */
    public void UpdateFromJSON(JSONObject json) {
        try {
            pos.x = json.getFloat(JSONFieldName.POSITION_X.getValue());
            pos.y = json.getFloat(JSONFieldName.POSITION_Y.getValue());
            remanantDir.x = json.getFloat(JSONFieldName.DIRECTION_X.getValue());
            remanantDir.y = json.getFloat(JSONFieldName.DIRECTION_Y.getValue());
            speed = json.getFloat(JSONFieldName.SPEED.getValue());
            ID = json.getString(JSONFieldName.ID.getValue());

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //
    // SETTERS
    //

    public void setDir(float x, float y) {
        dir.set(x, y);
    }

    public void setDir(PVector p) {
        dir.set(p.x, p.y);
    }

    public void setPos(float x, float y) {
        pos.set(x, y);
    }

    public void setPos(PVector p) {
        pos.set(p.x, p.y);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
