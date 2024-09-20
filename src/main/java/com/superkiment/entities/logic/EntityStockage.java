package com.superkiment.entities.logic;

import com.superkiment.entities.Player;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used to manage all entities in the game. It also contains a map
 * of entities
 * indexed by their unique ID.
 */
public class EntityStockage {
    EntityManager entityManager;

    /**
     * List of stored and updated entities
     */
    private ArrayList<Entity> entities;

    /**
     * List of players stored in 'entities'
     */
    private ArrayList<Player> players;

    public HashMap<String, Entity> idToEntity = new HashMap<String, Entity>();

    EntityStockage(EntityManager entityManager) {
        this.entityManager = entityManager;
        entities = new ArrayList<Entity>();
        players = new ArrayList<Player>();
        idToEntity = new HashMap<String, Entity>();

    }

    /**
     * Add an entity to the manager from an instanciated entity.
     * 
     * @param entity
     */
    public void add(Entity entity) {
        entity.setEntityManager(entityManager);
        entities.add(entity);
        idToEntity.put(entity.ID, entity);
    }

    /**
     * Adds a player to the manager from an instanciated player
     * 
     * @param player
     */
    public void add(Player player) {
        add((Entity) player);
        players.add(player);
    }

    /**
     * Removes a player from the manager from an instanciated player
     * 
     * @param player
     */
    public void remove(Player player) {
        players.remove(player);
    }

    /**
     * Removes an entity from the manager from an instanciated player
     * 
     * @param entity
     */
    public void remove(Entity entity) {
        entity.setEntityManager(null);
        entities.remove(entity);
        idToEntity.remove(entity.ID);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public Entity getEntityFromID(String id) {
        return idToEntity.get(id);
    }
}
