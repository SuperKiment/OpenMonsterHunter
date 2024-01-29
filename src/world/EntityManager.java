package world;

import logic.*;
import processing.core.PVector;
import processing.data.JSONObject;
import processing.net.Client;

import java.util.*;

public class EntityManager {
	World world;
	ArrayList<Entity> entities;
	ArrayList<Player> players;
	HashMap<Client, Player> clientToPlayers;
	HashMap<Player, Client> playersToClient;

	EntityManager(World w) {
		world = w;
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
		// TODO C'est le monde qui donne la position
		PVector pos = new PVector(50, 50);

		Player p = new Player(data.getString("name"), pos);

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
}
