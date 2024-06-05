package com.superkiment.entities;

import com.superkiment.entities.logic.Entity;
import com.superkiment.entities.logic.Interactable;
import com.superkiment.entities.logic.InteractionManager;

import processing.core.PVector;;

public class Dog extends Entity implements Interactable {

    private InteractionManager interactionManager;

    @Override
    public void Update() {
        super.Update();
    }

    @Override
    public InteractionManager getInteractionManager() {
        return interactionManager;
    }

    @Override
    public PVector getPos() {
        return this.pos;
    }

}