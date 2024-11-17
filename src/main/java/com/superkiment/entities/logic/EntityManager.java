package com.superkiment.entities.logic;

import com.superkiment.entities.Player;
import com.superkiment.main.OpenMonsterHunter;
import com.superkiment.world.PlayerClient;

import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityManager {

    public ArrayList<PlayerClient> clients;

    public EntityStorage entityStorage;

    public EntityManager() {
        entityStorage = new EntityStorage(this);

        clients = new ArrayList<PlayerClient>();
    }

    // ADD
    /**
     * Add an entity to the manager from an instanciated entity.
     * Try to use this function only from outside the class
     * 
     * @param entity
     */
    public void addEntity(Entity entity) {
        entityStorage.addEntity(entity);
    }

    /**
     * Add an entity to the manager from a JSON.
     * 
     * @param entity's JSON
     */
    public void addEntity(JSONObject json) {

        try {
            Entity e = null;
            Class<?> arrayClass = Class.forName(json.getString(JSONFieldName.CLASS_NAME.getValue()));
            e = (Entity) arrayClass.getDeclaredConstructor().newInstance();
            e.UpdateFromJSON(json);
            entityStorage.addEntity(e);
        } catch (Exception exe) {
            System.out.println("Echec de création par JSON : " + exe);
        }
    }

    /**
     * Add a player linked to a client
     * 
     * @param player
     * @param client
     */
    // public void addPlayer(Player player, Client client) {
    // entityStorage.addEntity(player);
    // entityStorage.addPlayer(player);
    // clientToPlayers.put(client, player);
    // playersToClient.put(player, client);
    // }

    /**
     * Add a player linked to a client from JSON data
     * 
     * @param data   the player data as JSON
     * @param client
     * @return the player as Player
     */
    public Player addPlayer(JSONObject data, PlayerClient client) {
        Player p = JSONToPlayer(data);

        entityStorage.addEntity(p);
        entityStorage.addPlayer(p);

        client.setPlayer(p);
        clients.add(client);
        return p;
    }

    /**
     * Adds the controllable player to the world not as a player but as entity
     * 
     * @param json the player data as JSON
     * @return the player as Player
     */
    public Player addControllablePlayer(JSONObject json) {
        Player p = JSONToPlayer(json);
        entityStorage.addEntity(p);
        System.out.println("added controllable player : " + p.getJSON());
        // System.out.println("added controllable player " + p.ID);
        return p;
    }

    // REMOVE
    /**
     * Remove a player and a client from the world
     * 
     * @param player
     */
    public void removePlayer(Player player) {
        System.out.println("Removed player " + player.name);
        entityStorage.removeEntity(player);
        entityStorage.removePlayer(player);

        for (PlayerClient playerClient : clients) {
            if (playerClient.getPlayer() == player) {
                clients.remove(playerClient);
            }
        }
    }

    /**
     * Remove an entity from the world
     * 
     * @param entity
     */
    public void removeEntity(Entity entity) {
        entityStorage.removeEntity(entity);
    }

    // GET
    public ArrayList<Player> getPlayers() {
        return entityStorage.players;
    }

    public Player getPlayer(PlayerClient c) {
        return c.getPlayer();
    }

    public PlayerClient getClient(Player p) {
        for (PlayerClient playerClient : clients) {
            if (playerClient.getPlayer() == p) {
                return playerClient;
            }
        }

        return null;
    }

    public ArrayList<Entity> getEntities() {
        return entityStorage.entities;
    }

    // AUTRES
    /**
     * Transforms a JSON representation into a Player object
     * 
     * @param json
     * 
     * @return a Player object
     */
    public Player JSONToPlayer(JSONObject json) {
        PVector pos = new PVector(50, 50);

        Player p = new Player(json.getString(JSONFieldName.PLAYER_NAME.getValue()), pos);
        System.out.println(json);
        p.ID = json.getString(JSONFieldName.PLAYER_NAME.getValue());

        return p;
    }

    /**
     * 
     * @param client
     * @return true if the client is connected to a player, false otherwise
     */
    public boolean isClientAsPlayer(PlayerClient client) {
        return client.getPlayer() == null;
    }

    // VOIDS
    /**
     * Removes the players from the world where the clients are no longer connected.
     */
    public void RemoveDisconnectedPlayers() {
        for (PlayerClient client : clients) {
            if (client.ip() == null) {
                removePlayer(client.getPlayer());
            }
        }
    }

    //
    // Ajoute les entités ajoutées
    // Supprime les entités absentes

    /**
     * Ajoute les entités ajoutées, supprime les entités absentes.
     * 
     * @param data data complète de
     * 
     */
    public void addOrRemoveEntityFromJSONData(JSONObject data) {

        //
        // Copy les elements de la liste
        ArrayList<Entity> entitiesToRemove = new ArrayList<Entity>();
        for (Entity e : this.getEntities()) {
            entitiesToRemove.add(e);
        }

        //
        // For all classes in the JSON object
        for (Object classNameObj : data.keys().toArray()) {
            String className = (String) classNameObj;

            processJSONEntitiesArray(className, data.getJSONArray(className), entitiesToRemove);
        }

        //
        // Enlever les entites disparues
        // TODO Ptet trouver une autre méthode
        for (Entity e : entitiesToRemove) {
            this.getEntities().remove(e);
        }
    }

    //
    // Update les positions de toutes les entités en fonction des données passées
    /**
     * Updates positions from the data passed as JSON
     * 
     * @param data
     */
    public void updateEntityInfos(JSONObject data) {

        for (Object keyObj : data.keys().toArray()) {
            String key = (String) keyObj;
            JSONArray array = data.getJSONArray(key);

            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);

                Entity entity = entityStorage.getEntityFromID(obj.getString(JSONFieldName.ID.getValue()));

                if (entity != null) {
                    if (entity == OpenMonsterHunter.game.controlledPlayer)
                        continue;

                    entity.sayingBox.setSayingText(obj.getString(JSONFieldName.TEXT_SAYING.getValue()));
                    entity.pos.set(obj.getFloat(JSONFieldName.POSITION_X.getValue()),
                            obj.getFloat(JSONFieldName.POSITION_Y.getValue()));
                }

                // for (Entity entity : entities) {
                // if (entity.ID.equals(obj.getString("ID"))) {

                // }
                // }
            }
        }
    }

    private void processJSONEntitiesArray(String className, JSONArray array, ArrayList<Entity> entitiesToRemove) {

        Class<?> arrayClass = null;

        try {
            arrayClass = Class.forName(className);
        } catch (Exception e) {
            return;
        }

        //
        // Pour chaque entité
        for (int i = 0; i < array.size(); i++) {

            Entity newEntity = createEntityFromJSON(className, arrayClass, array.getJSONObject(i));

            //
            // Vérifier l'existance de l'entité par 2 barrières :
            boolean exists = false;

            exists = className.equals(Player.class.getName())
                    && ((Player) newEntity).name.equals(OpenMonsterHunter.game.controlledPlayer.name);

            if (!exists) {
                Entity entity = entityStorage.getEntityFromID(newEntity.ID);
                if (entity != null) {
                    exists = true;
                    entitiesToRemove.remove(entity);
                }

            }

            //
            // S'il existe pas, ajouter l'entité
            if (!exists) {
                entityStorage.addEntity(newEntity);
                System.out.println("Ajouté " + newEntity.getClass().getName());
            }
        }

    }

    private Entity createEntityFromJSON(String className, Class<?> arrayClass, JSONObject jsonFromArray) {
        Entity newEntity = null;

        //
        // Construire un player ou une entité
        if (className.equals(Player.class.getName())) {
            newEntity = instanciatePlayer(arrayClass, jsonFromArray);
        } else {
            newEntity = instanciateEntity(arrayClass);
        }
        newEntity.ID = jsonFromArray.getString(JSONFieldName.ID.getValue());

        return newEntity;
    }

    private Player instanciatePlayer(Class<?> arrayClass, JSONObject jsonFromArray) {
        try {
            return (Player) arrayClass.getDeclaredConstructor(String.class, PVector.class)
                    .newInstance(jsonFromArray.getString(JSONFieldName.PLAYER_NAME.getValue()),
                            new PVector(100, 100));
        } catch (Exception e) {
            return null;
        }
    }

    private Entity instanciateEntity(Class<?> arrayClass) {
        try {
            return (Entity) arrayClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}
