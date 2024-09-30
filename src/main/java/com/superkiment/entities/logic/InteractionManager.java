package com.superkiment.entities.logic;

import java.util.function.Consumer;

import com.superkiment.main.Console;
import com.superkiment.main.OpenMonsterHunter;

/**
 * The manager that is responsible for making the parent interact with other
 * interactables
 */
public class InteractionManager {
    private Entity parent;
    public Interactable entityInteractable = null;

    /**
     * True if the Interactable entity is active for an interaction. Example : a
     * door that activates only with a switch is active only if the switch is
     * pulled.
     */
    private boolean isActiveInteraction = true;

    /**
     * The action to perform when the interaction is made by the entity.
     */
    private Consumer<Entity> actionInteraction = null;

    public void setActionInteraction(Consumer<Entity> action) {
        actionInteraction = action;
    }

    public InteractionManager(Entity parent) {
        this.parent = parent;
        this.actionInteraction = (args) -> {
            Console.console.write("Interaction not set yet : " + parent.getClassName(), true);
        };
    }

    /**
     * Try setting the interactable entity.
     * 
     * @param potential the entity to interact with
     * @return true if the entity is interactable and is set as the interactable
     *         entity, false otherwise
     */
    public boolean trySetInteractable(Entity potential) {
        if (!(potential instanceof Interactable))
            return false;

        entityInteractable = (Interactable) potential;
        return true;
    }

    public void Interact() {
        if (entityInteractable == null)
            return;

        if (!isInteractable(entityInteractable))
            return;

        try {
            // entityInteractable.getInteractionManager().doAction(parent);
            OpenMonsterHunter.game.connexion.EnvoiInteraction(entityInteractable, parent);
        } catch (Exception e) {
            System.out.println("Erreur Interaction");
        }
    }

    public void InteractWith(Interactable potential) {
        if (potential != null && isInteractable(potential)) {
            try {
                System.out.println("Interacting with " + potential.getClass().getName());
                potential.getInteractionManager().doAction(parent);
            } catch (Exception e) {
                System.err.println(e);
                // Console.console.write("Erreur Interaction", false);
            }
        }
    }

    /**
     * Makes the interaction inside of the Consumer actionInteraction happen
     */
    private void doAction(Entity entity) {
        if (actionInteraction != null)
            actionInteraction.accept(entity);
    }

    /**
     * Checks the distance and activeness
     * 
     * @return true if the interaction is doable
     */
    public boolean isInteractable(Interactable interactable) {
        if (interactable == null || parent.pos == interactable.getPos())
            return false;

        return true;
    }
}
