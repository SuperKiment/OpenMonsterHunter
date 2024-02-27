package world;

import logic.*;
import main.OpenMonsterHunter;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.net.Client;

import java.util.*;

public class EntityManager {
	ArrayList<Entity> entities;
	ArrayList<Player> players;
	HashMap<Client, Player> clientToPlayers;
	HashMap<Player, Client> playersToClient;

	public EntityManager() {
		entities = new ArrayList<Entity>();
		players = new ArrayList<Player>();
		clientToPlayers = new HashMap<Client, Player>();
		playersToClient = new HashMap<Player, Client>();
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}

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

	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}

	public void addPlayer(Player player, Client client) {
		entities.add(player);
		players.add(player);
		clientToPlayers.put(client, player);
		playersToClient.put(player, client);
	}

	public Player addPlayer(JSONObject data, Client client) {
		Player p = JSONToPlayer(data);
		addPlayer(p, client);
		return p;
	}

	public void removePlayer(Player player) {
		System.out.println("Removed player " + player.name);
		entities.remove(player);
		players.remove(player);
		clientToPlayers.remove(playersToClient.get(player));
		playersToClient.remove(player);
	}

	public Player addControllablePlayer(JSONObject json) {
		Player p = JSONToPlayer(json);
		addEntity(p);
		System.out.println("added controllable player");
		return p;
	}

	public Player JSONToPlayer(JSONObject json) {
		PVector pos = new PVector(50, 50);

		Player p = new Player(json.getString("name"), pos);

		return p;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public Player getPlayer(Client c) {
		return clientToPlayers.get(c);
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

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
			String key = (String) keyObj;
			JSONArray array = data.getJSONArray(key);

			try {

				Class arrayClass = Class.forName(key);

				//
				// Pour chaque entité
				for (int i = 0; i < array.size(); i++) {
					Entity obj = null;
					JSONObject json = array.getJSONObject(i);

					//
					// Construire un player ou une entité
					if (key.equals(Player.class.getName())) {
						obj = (Player) arrayClass.getDeclaredConstructor(String.class, PVector.class)
								.newInstance(json.getString("name"), new PVector(100, 100));
					} else {
						obj = (Entity) arrayClass.getDeclaredConstructor().newInstance();
					}
					obj.ID = json.getString("ID");

					//
					// Vérifier l'existance
					boolean exists = false;

					if (key.equals(Player.class.getName())
							&& ((Player) obj).name.equals(OpenMonsterHunter.game.controlledPlayer.name))
						exists = true;

					// TODO optimiser ça : (en faisant un tableau de IDs par exemple)
					if (!exists) {
						for (Entity entity : entities) {
							if (entity.ID.equals(obj.ID)) {
								exists = true;
								entitiesToRemove.remove(entity);
								break;
							}
						}
					}

					//
					// S'il existe pas, ajouter
					if (!exists) {
						addEntity(obj);
						System.out.println("Ajouté " + obj.getClass().getName());
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
