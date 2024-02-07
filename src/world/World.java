package world;

import java.util.HashMap;

import logic.Entity;
import logic.Player;
import processing.core.*;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.net.*;

public class World extends PApplet {

	final static String DELIMITER_ENTETE = ":::::";

	final static String BONJOUR_DU_CLIENT = "buongiorno";
	final static String BONJOUR_DU_SERVER = "yeepii";
	final static String UPDATE_PLAYER_DATA = "coucoujupdate";
	final static String SERVER_TO_PLAYER_FIRST_DATA = "heyy tes la";

	private Server server;
	public String name = "NoName";
	private boolean render = false;

	private EntityManager entityManager;

	public static void main(String[] args) {
		try {
			PApplet.main("world.World");
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public World() {
		this("Server", true);
	}

	public World(String name, boolean render) {
		super();
		this.name = name;
		this.render = render;

		entityManager = new EntityManager();
	}

	public void settings() {
		size(300, 800);
		noSmooth();

	}

	public void setup() {
		server = new Server(this, 5204);
		frameRate(60);
	}

	public void draw() {

		if (render)
			Render();

		entityManager.RemoveDisconnectedPlayers();

		TraiterClients();

		for (Client c : server.clients) {
			if (c != null) {
				c.write(getJSON().toString() + "\n");
			}
		}

	}

	private void Render() {
		background(0);
		fill(255);
		textSize(15);
		text("Nombre de clients : " + server.clientCount, 50, 50);
		text("Nom : " + name, 50, 70);

		int compt = 0;
		push();
		for (Client c : server.clients) {
			if (c != null) {
				try {
					translate(0, 100);
					Player p = entityManager.clientToPlayers.get(c);
					text(c.ip() + " : " + p.name + " / " + p.pos, 10, 20 * compt++);
				} catch (Exception e) {

				}
			}
		}
		pop();
	}

	private void TraiterClients() {
		// Get the next available client
		Client client = server.available();

		if (client != null) {
			String clientData = client.readString();

			for (String data : clientData.split(DELIMITER_ENTETE)) {
				TraiterRequete(data, client);
			}

		}
	}

	private void TraiterRequete(String fullData, Client client) {
		// println("fullData : " + fullData);

		JSONObject requete = JSONObject.parse(fullData);

		// println("requete : " + requete);
		switch (requete.getString("type")) {
		case BONJOUR_DU_CLIENT:
			Player p = entityManager.addPlayer(requete.getJSONObject("data"), client);
			client.write(createRequest(BONJOUR_DU_SERVER, p.getJSON(), "server").toString());

			break;
		case UPDATE_PLAYER_DATA:
			entityManager.clientToPlayers.get(client).UpdateFromJSON(requete.getJSONObject("data"));
			break;
		default:
			println("Jsp comment traiter : " + fullData);
			break;
		}

	}

//	public HashMap<String, String> SplitDataToHashMap(String data) {
//		HashMap<String, String> splitData = new HashMap<String, String>();
//
//		for (String infos : data.split(DELIMITER_INFOS)) {
//			String[] donnee = infos.split(DELIMITER_DONNEE);
//			splitData.put(donnee[0], donnee[1]);
//		}
//
//		return splitData;
//	}

	public void keyPressed() {
		if (key == 'h') {
			for (Client client : server.clients) {
				if (client != null)
					server.disconnect(client);
			}
			surface.setVisible(false);
			dispose();
		}
	}

	private JSONObject getJSON() {
		JSONObject json = new JSONObject();

		HashMap<String, JSONArray> entities = new HashMap<String, JSONArray>();

		for (Entity e : entityManager.getEntities()) {
			String className = e.getClass().getName();

			if (!entities.containsKey(className))
				entities.put(className, new JSONArray());

			entities.get(className).append(e.getJSON());
		}

		entities.forEach((key, value) -> {
			json.put(key, entities.get(key));
		});

		return json;
	}

	public static JSONObject createRequest(String type, JSONObject data, String sender) {
		JSONObject json = new JSONObject();

		json.put("type", type);
		json.put("data", data);
		json.put("sender", sender);

		return json;

	}
}
