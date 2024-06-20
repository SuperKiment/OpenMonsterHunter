package com.superkiment.entities;

import com.superkiment.entities.logic.Entity;
import com.superkiment.entities.logic.Interactable;
import com.superkiment.entities.logic.InteractionManager;
import com.superkiment.main.Console;

import processing.core.PVector;;

public class Dog extends Entity implements Interactable {

    public Dog() {
        super();
        this.interactionManager.setActionInteraction((args) -> {
            Console.console.write("Dog saying : I'm a dog !!", false);
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

}