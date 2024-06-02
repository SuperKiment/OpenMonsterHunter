package com.superkiment.main;

import com.superkiment.entities.Dog;
import com.superkiment.entities.Entity;
import com.superkiment.entities.Player;

import java.util.HashMap;

/**
 * The class that renders entities. It has only one instance of itself.
 */
public class RenderManager {

    /**
     * The only instance of this class
     */
    public static RenderManager renderManager;

    /**
     * Links a class to a render action to the PApplet
     */
    private final HashMap<Class, RenderAction> classToActions;

    /**
     * The PApplet to be drawn on.
     */
    private final processing.core.PApplet pap;

    public RenderManager(processing.core.PApplet p) {
        classToActions = new HashMap<Class, RenderAction>();
        this.pap = p;

        // TODO Faire les renders
        // Entité de base
        classToActions.put(Entity.class, new RenderAction(this.pap) {
            public void Action(Entity e) {
                pap.pushStyle();
                pap.pushMatrix();
                pap.translate(e.pos.x, e.pos.y);
                pap.rotate(e.remanantDir.heading());
                pap.ellipse(0, 0, 20, 20);
                pap.ellipse(-10, 0, 10, 10);

                pap.popMatrix();
                pap.popStyle();
            }
        });

        // Player
        classToActions.put(Player.class, new RenderAction(this.pap) {
            public void Action(Entity e) {
                pap.pushStyle();
                pap.pushMatrix();
                pap.translate(e.pos.x, e.pos.y);
                pap.rotate(e.remanantDir.heading());
                pap.ellipse(0, 0, 20, 20);
                pap.ellipse(0, 10, 10, 10);
                pap.ellipse(0, -10, 10, 10);

                pap.popMatrix();
                pap.popStyle();
            }
        });

        // Dog
        classToActions.put(Dog.class, new RenderAction(this.pap) {
            public void Action(Entity e) {
                pap.ellipse(e.pos.x, e.pos.y, 20, 20);
                pap.ellipse(e.pos.x, e.pos.y, 5, 5);

            }
        });
    }

    /**
     * Takes an entity and draws the corresponding actions to the PApplet
     * 
     * @param entity the entity to be drawn
     */
    public void Render(Entity entity) {
        try {
            // System.out.println("sheeesh");

            classToActions.get(entity.getClass()).Action(entity);
        } catch (Exception e) {
            System.out.println(e);
            classToActions.get(Entity.class).Action(entity);
        }
    }
    // ========================================

    // ========================================
    /**
     * A class that stores a rendering action. Instanciated like that :
     * <p>
     * new RenderAction(The PApplet) {
     * <p>
     * public void Action(Entity e) {
     * <p>
     * pap.ellipse(e.pos.x, e.pos.y, 20, 20);
     * <p>
     * pap.ellipse(e.pos.x, e.pos.y, 5, 5);
     * </p>
     * </p>
     * 
     * }
     * </p>
     * }
     * </p>
     */
    private class RenderAction {
        processing.core.PApplet pap;

        public RenderAction(processing.core.PApplet p) {
            pap = p;
        }

        public void Action(Entity e) {
        }
    }
}
