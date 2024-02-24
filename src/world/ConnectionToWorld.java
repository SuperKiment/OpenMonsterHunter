package world;

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

		TraiterDonnees(RecuperationDonnees());

		// EnvoiDonneesPlayer();
		EnvoiDonneesPlayer();
		
		//TODO Envoyer un ajout d'entité (tir par exemple)
	}

	private void EnvoiDonneesPlayer() {
//		client.write(null);
		//TODO Ajouter un délimiteur de requete pour en envoyer plein d'un coup
		if (OpenMonsterHunter.game.controlledPlayer != null)
			client.write(World.createRequest(World.UPDATE_PLAYER_DATA,
					OpenMonsterHunter.game.controlledPlayer.getJSON(), omh.playerName).toString());

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

	private void TraiterDonnees(JSONObject data) {
		if (data == null)
			return;

		omh.game.TraiterData(data);

	}
}
