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

		if (client.available() > 0) {
			omh.push();
			omh.fill(255);
			omh.textSize(10);
			omh.text(client.readString(), 50, 150);
			omh.pop();
		}

		// EnvoiDonneesPlayer();
		EnvoiDonneesPlayer();
	}

	public void EnvoiDonneesPlayer() {
//		client.write(null);
		// TODO Envoyer de vraies données
		client.write(World.createRequest(World.UPDATE_PLAYER_DATA, OpenMonsterHunter.game.controlledPlayer.getJSON(),
				omh.playerName).toString());

	}

	public void EnvoiDonnees(String entete, HashMap<Object, Object> data) {
		String strData = "";

		client.write(entete + World.DELIMITER_ENTETE + strData);
	}
}
