package world;

import processing.core.*;
import processing.net.*;

public class World extends PApplet {

	private Server server;
	public String name = "NoName";
	private boolean render = false;
	
	public static void main(String[] args) {
		try {
			PApplet.main("world.World");
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public World() {
		super();
		this.name = "Server";
		this.render = true;
	}
	
	public World(String name, boolean render) {
		super();
		this.name = name;
		this.render = render;
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
		if (render) {
			background(0);
			fill(255);
			textSize(15);
			text("Nombre de clients : " + server.clientCount, 50, 50);
			text("Nom : " + name, 50, 70);

			for (Client c : server.clients) {

			}
		}

		// Get the next available client
		Client client = server.available();

		if (client != null) {
			String clientData = client.readString();

			for (String data : clientData.split("\n")) {
				TraiterRequete(data, client);
			}

		}
	}

	private void TraiterRequete(String data, Client client) {
		if (data.equals("exit\n")) {
			client.write("You will be disconnected now.\n");
			println(client.ip() + "has been disconnected");
			server.disconnect(client);
		}
	}

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
}
