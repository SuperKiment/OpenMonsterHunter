package com.superkiment.main;

import com.superkiment.entities.Dog;
import com.superkiment.entities.Entity;
import com.superkiment.entities.Player;

import java.util.HashMap;

public class RenderManager {

    public static RenderManager renderManager;
    private final HashMap<Class, RenderAction> classToActions;
    private final processing.core.PApplet pap;

    public RenderManager(processing.core.PApplet p) {
        classToActions = new HashMap<Class, RenderAction>();
        this.pap = p;

        // TODO Faire les renders
        // Entité de base
        classToActions.put(Entity.class, new RenderAction(p) {
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
        classToActions.put(Player.class, new RenderAction(p) {
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
        classToActions.put(Dog.class, new RenderAction(p) {
            public void Action(Entity e) {
                pap.ellipse(e.pos.x, e.pos.y, 20, 20);
                pap.ellipse(e.pos.x, e.pos.y, 5, 5);

            }
        });
    }

    public void Render(Entity entity) {
        try {
            //System.out.println("sheeesh");

            classToActions.get(entity.getClass()).Action(entity);
        } catch (Exception e) {
            System.out.println(e);
            classToActions.get(Entity.class).Action(entity);
        }
    }
    // ========================================

    // ========================================
    private class RenderAction {
        processing.core.PApplet pap;

        public RenderAction(processing.core.PApplet p) {
            pap = p;
        }

        public void Action(Entity e) {
        }
    }
}
