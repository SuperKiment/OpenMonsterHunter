package com.superkiment.entities.logic;

import processing.core.PVector;

public interface Interactable {
    /**
     * Get the manager that manages all interactions of one entity
     * <p>
     * Works like that to be able to create some sort of Ender Chest : multiple
     * entities can redirect to one interaction manager
     * </p>
     * 
     * @return the entity's interaction manager
     */
    public InteractionManager getInteractionManager();

    /**
     * Get the entity's world position
     * 
     * @return a PVector representing the world position of the entity
     */
    public PVector getPos();

    public Entity getEntity();
}