package com.superkiment.entities.logic;

import com.superkiment.entities.Player;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityStorage {
    EntityManager entityManager;

    /**
     * List of stored and updated entities
     */
    public ArrayList<Entity> entities;

    /**
     * List of players stored in 'entities'
     */
    public ArrayList<Player> players;

    public HashMap<String, Entity> idToEntity = new HashMap<String, Entity>();

    EntityStorage(EntityManager entityManager) {
        this.entityManager = entityManager;
        entities = new ArrayList<Entity>();
        players = new ArrayList<Player>();
        idToEntity = new HashMap<String, Entity>();
    }

    public Entity getEntityFromID(String id) {
        return idToEntity.get(id);
    }

    public void removeEntityFromID(String id) {
        idToEntity.remove(id);
    }

    /**
     * Add an entity to the manager from an instanciated entity.
     * 
     * @param entity
     */
    public void addEntity(Entity e) {
        e.setEntityManager(entityManager);

        entities.add(e);
        idToEntity.put(e.ID, e);
    }

    public void addPlayer(Player p) {
        this.players.add(p);
    }

    /**
     * Remove an entity from the storage
     * 
     * @param entity
     */
    public void removeEntity(Entity entity) {
        entity.setEntityManager(null);
        entities.remove(entity);
        this.removeEntityFromID(entity.ID);
    }

    public void removePlayer(Player p) {
        this.players.remove(p);
    }

    public void PrintAllEntities() {
        System.out.println("-----------All entities :");
        for (Entity entity : entities) {
            System.out.println(entity.ID + " : " + entity.getClass().getName() + " " + entity.pos);
        }
        System.out.println("-----------");
    }

}
