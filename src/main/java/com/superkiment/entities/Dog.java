package com.superkiment.entities;

import com.superkiment.entities.logic.Entity;
import com.superkiment.entities.logic.Interactable;
import com.superkiment.entities.logic.InteractionManager;
import processing.core.PVector;

public class Dog extends Entity implements Interactable {

    public Dog() {
        super();
        this.interactionManager.setActionInteraction((interacting) -> {
            System.out.println("DOGG");
        });

    }

    @Override
    public void Update() {
        super.Update();
    }

    @Override
    public InteractionManager getInteractionManager() {
        return this.interactionManager;
    }

    @Override
    public PVector getPos() {
        return this.pos;
    }

    @Override
    public Entity getEntity() {
        return this;
    }

}