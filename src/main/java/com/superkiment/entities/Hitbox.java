package com.superkiment.entities;

import com.superkiment.utils.Pair;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.function.Consumer;

public class Hitbox {
    /**
     * The hitbox type, which shape it has (Rectangle, Circle, Triangle)
     */
    hitboxType type = null;

    /**
     * The hitbox role in the entity : Trigger, Physics.
     */
    hitboxActionType actionType = hitboxActionType.PHYSICS;

    /**
     * The position of the hitbox relative to the entity's world position
     */
    PVector pos;

    /**
     * Value to define the hitbox's shape
     */
    float range = 10, largeur = 10, hauteur = 10;

    /**
     * The hitbox's parent entity
     */
    Entity parent;

    /**
     * The action to execute when the hitbox hits an entity, used only when the
     * hitbox is a trigger.
     */
    Consumer<Entity> action = null;

    /**
     * Base construct
     *
     * @param e   the parent entity
     * @param pos the position relative to the entity
     */
    private Hitbox(Entity e, PVector pos) {
        this.parent = e;
        this.pos = new PVector(pos.x, pos.y);
    }

    /**
     * Construct hitbox as CERCLE
     *
     * @param e     the parent entity
     * @param pos   the position relative to the entity
     * @param range the range of the circle
     */
    public Hitbox(Entity e, PVector pos, float range) {
        this(e, pos);
        type = hitboxType.CERCLE;
        this.range = range;
    }

    /**
     * Construct hitbox as RECTANGLE
     *
     * @param e    the parent entity
     * @param pos  the position relative to the entity
     * @param larg the width
     * @param haut the height
     */
    public Hitbox(Entity e, PVector pos, float larg, float haut) {
        this(e, pos);
        type = hitboxType.RECTANGLE;
        this.largeur = larg;
        this.hauteur = haut;
    }

    /**
     * Static PApplet push() method for all hitboxes. DO NOT FORGET pop() !!
     *
     * @param p the PApplet used to draw
     */
    public static void PushStyle(PApplet p) {
        p.pushStyle();
        p.pushMatrix();
        p.fill(0, 0);
        p.stroke(255, 0, 0, 255);
        p.strokeWeight(0.5f);
        p.rectMode(PApplet.CENTER);
    }

    public void setAction(Consumer<Entity> consumer) {
        this.action = consumer;
    }

    /**
     * Do the hitbox's action
     * 
     * @param entity      the entity touched
     * @param otherHitbox the other hitbox
     * @param vects       an array of PVectors : [0 : this hitbox's world position ;
     *                    1 : other hitbox's world position]
     */
    public void doAction(Entity entity, Hitbox otherHitbox, PVector[] vects) {
        PVector actualPos = vects[0];
        PVector otherActualPos = vects[1];

        if (actionType != hitboxActionType.PHYSICS) {
            if (action != null) {
                action.accept(entity);
            }
        } else {
            if (this.type == hitboxType.CERCLE && otherHitbox.type == hitboxType.CERCLE) {
                PVector dirOtherToMe = PVector.sub(actualPos, otherActualPos);
                float enfoncement = this.range + otherHitbox.range - dirOtherToMe.mag() * 2;

                // System.out.println(this.parent.getClass().getName() + " pousse " +
                // otherHitbox.parent.getClass().getName());
                PVector depl = dirOtherToMe.copy();
                depl.setMag(enfoncement / 2);
                this.parent.pos.add(depl);
                otherHitbox.parent.pos.add(PVector.mult(depl, -1));
            }
        }
    }

    /**
     * Render the hitbox on the PApplet
     * 
     * @param p the PApplet to be drawn on
     */
    public void Render(PApplet p) {
        switch (type) {
            case RECTANGLE:
                p.rect(pos.x, pos.y, largeur, hauteur);
                break;
            case CERCLE:
                p.ellipse(pos.x, pos.y, range, range);
                break;
            default:
                p.ellipse(pos.x, pos.y, 10, 10);
                break;
        }
    }

    /**
     * Get the world position of the hitbox from the parent position and angle
     * 
     * @return the world position of the hitbox
     */
    public PVector getActualPos() {
        PVector actualPos = new PVector(parent.pos.x, parent.pos.y);
        PVector posRotated = new PVector(this.pos.x, this.pos.y);
        posRotated.rotate(parent.dir.heading());

        actualPos.add(posRotated);

        return actualPos;
    }

    /**
     * 
     * @param other the other hitbox
     * @return a Pair (Boolean - PVector[]) of :
     *         <ul>
     *         <li>a boolean : true if the hitboxes collide</li>
     *         <li>an array of PVectors : an array of PVectors : [0 : this hitbox's
     *         world position ; 1 : other hitbox's world position]</li>
     *         </ul>
     */
    public Pair<Boolean, PVector[]> isCollisionWith(Hitbox other) {
        boolean collision = false;
        PVector actualPos = this.getActualPos();
        PVector otherActualPos = other.getActualPos();

        switch (other.type) {
            case CERCLE:
                if (PVector.sub(actualPos, otherActualPos).mag() * 2 < other.range + this.range) {
                    collision = true;
                }
                break;
            case RECTANGLE:
                break;
            case TRIANGLE:
                break;
            default:
                break;
        }

        PVector[] vects = { actualPos, otherActualPos };

        return new Pair<Boolean, PVector[]>(collision, vects);
    }

    /**
     * A hitbox's shape (Rectangle, Circle, etc.)
     */
    enum hitboxType {
        RECTANGLE, CERCLE, TRIANGLE
    }

    /**
     * A hitbox's action (Physics, Trigger, etc.)
     */
    enum hitboxActionType {
        PHYSICS, TRIGGER
    }
}
