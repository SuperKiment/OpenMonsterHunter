package world;

import processing.net.Client;

public class ConnectionToWorld {
	main.OpenMonsterHunter omh;
	public Client client;

	public ConnectionToWorld(main.OpenMonsterHunter omh, String address) {
		this.omh = omh;

		client = new Client(omh, address, 5204);

		client.write("Player name : " + omh.playerName + "\n");
		client.write("Player namfeze : " + omh.playerName + "\n");
		client.write("Player nzehhterame : " + omh.playerName + "\n");
	}
	
	public boolean isConnected() {
		return client != null && client.ip() != null;
	}
}
