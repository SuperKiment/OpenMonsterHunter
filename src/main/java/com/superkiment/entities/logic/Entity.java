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

    /**
     * The text showing above the entity, what the entity is saying
     */
    public SayingBox sayingBox;

    /**
     * An EntityJSONUpdater instance used to update the entity's properties based on
     * incoming JSON.
     */
    public EntityJSONUpdater entityJSONUpdater;

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
        entityJSONUpdater = new EntityJSONUpdater(this);
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

        obj.setString(JSONFieldName.POSITION_X.getValue(), pos.x + "");
        obj.setString(JSONFieldName.POSITION_Y.getValue(), pos.y + "");
        obj.setString(JSONFieldName.DIRECTION_X.getValue(), remanantDir.x + "");
        obj.setString(JSONFieldName.DIRECTION_Y.getValue(), remanantDir.y + "");

        obj.setString(JSONFieldName.TEXT_SAYING.getValue(), sayingBox.getSayingText());
        obj.setString(JSONFieldName.SPEED.getValue(), speed + "");

        obj.setString(JSONFieldName.ID.getValue(), ID);
        obj.setString(JSONFieldName.CLASS_NAME.getValue(), this.getClass().getName());
        return obj;
    }

    public JSONObject getWhatHasChangedJSON() {
        return this.entityJSONUpdater.getWhatHasChangedJSON();
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
            // pos.x = json.getFloat(JSONFieldName.POSITION_X.getValue());
            // pos.y = json.getFloat(JSONFieldName.POSITION_Y.getValue());
            // remanantDir.x = json.getFloat(JSONFieldName.DIRECTION_X.getValue());
            // remanantDir.y = json.getFloat(JSONFieldName.DIRECTION_Y.getValue());
            // speed = json.getFloat(JSONFieldName.SPEED.getValue());
            // ID = json.getString(JSONFieldName.ID.getValue());

            this.entityJSONUpdater.UpdateFromJSON(json);

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
