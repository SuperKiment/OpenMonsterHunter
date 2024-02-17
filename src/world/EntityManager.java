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
		// TODO C'est le monde qui donne la position
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

	public void addIfInexistant(JSONObject data) {

		for (Object keyObj : data.keys().toArray()) {
			String key = (String) keyObj;
			JSONArray array = data.getJSONArray(key);

			try {

				Class arrayClass = Class.forName(key);

				for (int i = 0; i < array.size(); i++) {
					Entity obj = null;
					JSONObject json = array.getJSONObject(i);

					if (key.equals("logic.Player")) {
						obj = (Player) arrayClass.getDeclaredConstructor(String.class, PVector.class)
								.newInstance(json.getString("name"), new PVector(100, 100));
					} else {
						obj = (Entity) arrayClass.getDeclaredConstructor().newInstance();
					}
					obj.ID = json.getString("ID");

					boolean exists = false;
					// TODO optimiser ça : (en faisant un tableau de IDs par exemple)
					for (Entity entity : entities) {
						if (entity.ID.equals(obj.ID)) {
							exists = true;
							break;
						}
					}

					if (key.equals("logic.Player")
							&& ((Player) obj).name.equals(OpenMonsterHunter.game.controlledPlayer.name))
						exists = true;

					if (!exists) {
						addEntity(obj);
						System.out.println("Ajouté " + obj.getClass().getName());
					}
				}
			} catch (Exception e) {
				System.out.println("Pas trouvé : " + e);
			}
		}

//		for (int i = 0; i < data.getJSONArray("logic.Player").size(); i++) {
//			JSONObject playerJSON = data.getJSONArray("logic.Player").getJSONObject(i);
//			boolean exists = false;
//			for (Player p : players) {
//				if (p.name.equals(playerJSON.getString("name"))) {
//					exists = true;
//					break;
//				}
//			}
//
//			if (!exists) {
//				addPlayer(playerJSON, null);
//			}
//
//		}

	}
}
