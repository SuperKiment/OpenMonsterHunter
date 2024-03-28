package world;

import java.util.ArrayList;

import main.Console;
import main.Game;
import main.OpenMonsterHunter;
import processing.data.JSONObject;
import processing.net.Client;

public class ConnectionToWorld {
	main.OpenMonsterHunter omh;
	public Client client;

	public ConnectionToWorld(main.OpenMonsterHunter omh, String address, Game game) {
		this.omh = omh;

		client = new Client(omh, address, 5204);

		JSONObject dataPlayer = new JSONObject();
		dataPlayer.put("name", omh.playerName);
		JSONObject bonjourJSON = World.createRequest(World.BONJOUR_DU_CLIENT, dataPlayer, omh.playerName);

		client.write(bonjourJSON.toString() + World.DELIMITER_ENTETE);
		System.out.println("Mon player :");
		omh.delay(200);

		String dataString = client.readString();
		JSONObject reponse = JSONObject.parse(dataString);

		game.connexion = this;

		System.out.println("reponse :");
		System.out.println(reponse);
		System.out.println(reponse.getString("type"));

		while (reponse.getString("type") == null) {

			dataString = client.readString();
			reponse = JSONObject.parse(dataString);

			System.out.println("reponse :");
			System.out.println(reponse);
			System.out.println(reponse.getString("type"));
		}
		try {
			if (reponse.getString("type").equals(World.BONJOUR_DU_SERVER)) {
				omh.setControllablePlayer(reponse.getJSONObject("data"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public boolean isConnected() {
		return client != null && client.ip() != null;
	}

	public void Update() {

		JSONObject[] datas = RecuperationDonnees();

		for (JSONObject data : datas) {
			switch (data.getString("type")) {
			case World.UPDATE_WORLD_STATE_ENTITIES:
				TraiterDonnees(data.getJSONObject("data"));
				break;
			case World.CONSOLE_INPUT_FOR_EVERYONE:
				System.out.println(data);
				JSONObject input = data.getJSONObject("data");
				Console.console.write(input.getString("sender") + " : " + input.getString("text"), true);
				break;
			}
		}

		EnvoiDonneesPlayer();

	}

	private void EnvoiDonneesPlayer() {
		if (OpenMonsterHunter.game.controlledPlayer != null)
			client.write(
					World.createRequest(World.UPDATE_PLAYER_DATA, OpenMonsterHunter.game.controlledPlayer.getJSON(),
							omh.playerName).toString() + World.DELIMITER_ENTETE);
	}

	public void EnvoiDonneesNouvelleEntite(JSONObject json) {
		if (OpenMonsterHunter.game.controlledPlayer != null)
			client.write(World.createRequest(World.NEW_ENT_FROM_PLAYER, json, omh.playerName).toString()
					+ World.DELIMITER_ENTETE);
	}

	public void EnvoiConsoleInput(String input) {
		if (input.isEmpty())
			return;

		JSONObject json = new JSONObject();
		json.setString("text", input);

		client.write(
				World.createRequest(World.NEW_CONSOLE_INPUT, json, omh.playerName).toString() + World.DELIMITER_ENTETE);
	}

	private JSONObject[] RecuperationDonnees() {

		ArrayList<JSONObject> objects = new ArrayList<JSONObject>();

		if (client.available() > 0) {
			String fullData = client.readString();

			// Split par le délimiter
			String[] parts = fullData.split(World.DELIMITER_ENTETE);
			for (String part : parts) {

				JSONObject data = null;
				try {
					data = JSONObject.parse(part);
				} catch (Exception e) {
					System.out.println("Pas réussi à parse !");
				}

				if (data != null) {
					omh.push();
					omh.fill(255);
					omh.textSize(10);
					omh.text(data.toString(), 50, 150);
					omh.pop();
				}

				objects.add(data);
			}
		}

		JSONObject[] res = new JSONObject[objects.size()];

		for (int i = 0; i < objects.size(); i++) {
			res[i] = objects.get(i);
		}

		return res;
	}

	private void TraiterDonnees(JSONObject data) {
		if (data == null)
			return;

		OpenMonsterHunter.game.TraiterData(data);

	}
}
