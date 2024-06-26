package com.superkiment.entities.logic;

import com.superkiment.entities.Player;
import com.superkiment.main.OpenMonsterHunter;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.net.Client;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityManager {
    /**
     * List of stored and updated entities
     */
    ArrayList<Entity> entities;

    /**
     * List of players stored in 'entities'
     */
    ArrayList<Player> players;

    /**
     * Hash to get a Player from a Client
     */
    HashMap<Client, Player> clientToPlayers;

    /**
     * Hash to get a Client from a Player
     */
    HashMap<Player, Client> playersToClient;

    /**
     * Hash to get an entity from an ID
     */
    HashMap<String, Entity> idToEntity;

    public EntityManager() {
        entities = new ArrayList<Entity>();
        players = new ArrayList<Player>();
        clientToPlayers = new HashMap<Client, Player>();
        playersToClient = new HashMap<Player, Client>();
        idToEntity = new HashMap<String, Entity>();
    }

    // ADD
    /**
     * Add an entity to the manager from an instanciated entity.
     * 
     * @param entity
     */
    public void addEntity(Entity entity) {
        entity.setEntityManager(this);
        entities.add(entity);
        idToEntity.put(entity.ID, entity);
        System.out.println("Added entity " + entity.ID);
    }

    /**
     * Add an entity to the manager from a JSON.
     * 
     * @param entity's JSON
     */
    public void addEntity(JSONObject json) {

        try {
            Entity e = null;
            Class arrayClass = Class.forName(json.getString("className"));
            e = (Entity) arrayClass.getDeclaredConstructor().newInstance();
            e.UpdateFromJSON(json);
            addEntity(e);
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
    public void addPlayer(Player player, Client client) {
        addEntity(player);
        players.add(player);
        clientToPlayers.put(client, player);
        playersToClient.put(player, client);
    }

    /**
     * Add a player linked to a client from JSON data
     * 
     * @param data   the player data as JSON
     * @param client
     * @return the player as Player
     */
    public Player addPlayer(JSONObject data, Client client) {
        Player p = JSONToPlayer(data);
        addPlayer(p, client);
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
        addEntity(p);
        System.out.println("added controllable player");
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
        player.setEntityManager(null);
        entities.remove(player);
        players.remove(player);
        clientToPlayers.remove(playersToClient.get(player));
        playersToClient.remove(player);
    }

    /**
     * Remove an entity from the world
     * 
     * @param entity
     */
    public void removeEntity(Entity entity) {
        entity.setEntityManager(null);
        entities.remove(entity);
        // idToEntity.remove(entity.ID);
    }

    // GET
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(Client c) {
        return clientToPlayers.get(c);
    }

    public Client getClient(Player p) {
        return playersToClient.get(p);
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public Entity getEntity(String id) {
        Entity found = idToEntity.get(id);

        System.out.println(found.ID);

        return found;
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

        Player p = new Player(json.getString("name"), pos);

        return p;
    }

    /**
     * 
     * @param client
     * @return true if the client is connected to a player, false otherwise
     */
    public boolean isClientAsPlayer(Client client) {
        return clientToPlayers.containsKey(client);
    }

    // VOIDS
    /**
     * Removes the players from the world where the clients are no longer connected.
     */
    public void RemoveDisconnectedPlayers() {
        for (Client client : playersToClient.values()) {
            if (client.ip() == null) {
                removePlayer(clientToPlayers.get(client));
            }
        }
    }

    //
    // Ajoute les entités ajoutées
    // Supprime les entités absentes

    /**
     * Ajoute les entités ajoutées, supprime les entités absentes.
     * TODO : optimize and minify
     * 
     * @param data data complète de
     * 
     */
    public void addIfInexistant(JSONObject data) {

        //
        // Copy les elements de la liste
        ArrayList<Entity> entitiesToRemove = new ArrayList<Entity>();
        for (Entity e : entities) {
            entitiesToRemove.add(e);
        }

        //
        //
        for (Object keyObj : data.keys().toArray()) {
            String objClassName = (String) keyObj;
            JSONArray array = data.getJSONArray(objClassName);

            try {

                Class arrayClass = Class.forName(objClassName);

                //
                // Pour chaque entité
                for (int i = 0; i < array.size(); i++) {
                    Entity newEntity = null;
                    JSONObject json = array.getJSONObject(i);

                    //
                    // Construire un player ou une entité
                    if (objClassName.equals(Player.class.getName())) {
                        newEntity = (Player) arrayClass.getDeclaredConstructor(String.class, PVector.class)
                                .newInstance(json.getString("name"), new PVector(100, 100));
                    } else {
                        newEntity = (Entity) arrayClass.getDeclaredConstructor().newInstance();
                    }
                    newEntity.ID = json.getString("ID");

                    //
                    // Vérifier l'existance
                    boolean exists = objClassName.equals(Player.class.getName())
                            && ((Player) newEntity).name.equals(OpenMonsterHunter.game.controlledPlayer.name);

                    // TODO optimiser ça : (en faisant un tableau de IDs par exemple)
                    // Si l'entité existe, l'enlever de la liste des entités à supprimer
                    if (!exists) {
                        Entity matchingEntity = idToEntity.get(newEntity.ID);
                        System.out.println(
                                "newEntity id : '" + newEntity.ID + "' , matching id : '" + matchingEntity + "'");
                        if (matchingEntity != null) {
                            exists = true;
                            entitiesToRemove.remove(matchingEntity);
                            break;
                        }

                    }

                    //
                    // S'il existe pas, ajouter
                    if (!exists) {
                        addEntity(newEntity);
                        System.out.println("Ajouté " + newEntity.getClass().getName());
                    }
                }
            } catch (Exception e) {
                System.out.println("Pas trouvé : " + e);
            }
        }

        //
        // Enlever les entites disparues
        // TODO Ptet trouver une autre méthode

        for (Entity e : entitiesToRemove) {
            entities.remove(e);
        }
    }

    //
    // Update les positions de toutes les entités en fonction des données passées
    /**
     * Updates positions from the data passed as JSON
     * 
     * @param data
     */
    public void updatePositions(JSONObject data) {

        for (Object keyObj : data.keys().toArray()) {
            String key = (String) keyObj;
            JSONArray array = data.getJSONArray(key);

            // TODO optimiser ça : (en faisant un tableau de IDs par exemple)

            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);

                for (Entity entity : entities) {
                    if (entity.ID.equals(obj.getString("ID"))) {
                        entity.pos.set(obj.getFloat("pos.x"), obj.getFloat("pos.y"));

                    }
                }
            }
        }
    }
}
