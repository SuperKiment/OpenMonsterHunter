package world;

import java.util.HashMap;

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
		while (client.available() == 0) {
		}

		String dataString = client.readString();
		JSONObject reponse = JSONObject.parse(dataString);

		game.connexion = this;

		System.out.println("reponse :");
		System.out.println(reponse.getString("type"));
		try {
			if (reponse.getString("type").equals(World.BONJOUR_DU_SERVER)) {
				omh.setControllablePlayer(reponse.getJSONObject("data"));
			}
		} catch (Exception e) {

		}

		// TODO Traiter les données récup avec SERVER TO PLAYER etc.
	}

	public boolean isConnected() {
		return client != null && client.ip() != null;
	}

	public void Update() {

		JSONObject dataIn = RecuperationDonnees();
		TraiterDonnees(dataIn, omh.game.entityManager);

		// EnvoiDonneesPlayer();
		EnvoiDonneesPlayer();
	}

	private void EnvoiDonneesPlayer() {
//		client.write(null);
		client.write(World.createRequest(World.UPDATE_PLAYER_DATA, OpenMonsterHunter.game.controlledPlayer.getJSON(),
				omh.playerName).toString());

	}

	private JSONObject RecuperationDonnees() {
		if (client.available() > 0) {
			JSONObject data = null;
			try {
				data = JSONObject.parse(client.readString());
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

			return data;

		}

		return null;

	}

	private void TraiterDonnees(JSONObject data, EntityManager ent) {
		if (data == null)
			return;

//TODO traiter les données et les envoyer dans game. Ou alors tout envoyer
		// dans game et c'est lui qui trie
		for (int i = 0; i < data.getJSONArray("logic.Player").size(); i++) {
			JSONObject playerJSON = data.getJSONArray("logic.Player").getJSONObject(i);
			System.out.println("Data : " + i);
			System.out.println(playerJSON);
		}

	}
}
